package com.audio.service.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tritonus.sampled.convert.PCM2PCMConversionProvider;

import com.audio.entity.HashTable;
import com.audio.entity.Music;
import com.audio.mapper.AudioMapper;
import com.audio.service.AudioService;
import com.audio.util.FileHandle;
import com.audio.util.Hash;
import com.audio.util.ReadAudioFile;
import com.audio.util.fingerprint.Fingerprint;
import com.audio.util.index.Index;


@Service
public class AudioServiceImpl implements AudioService{
	@Autowired
	AudioMapper audioMapper;
	
	ArrayList<String> fileList;
	int fs=8000;
	int[] linkHash;
	int[] linkTime;
	int minHit=15;
	HashMap<Long,Integer> hashMap;
	List<HashMap.Entry<Long, Integer>> infoIds;
	long maxId = -1;
    int maxCount = -1;
	//D:\Z_毕设\Others Project\Audio-Fingerprinting-master\songs\泡沫\泡沫4s.wav
  
	@Override
	public List<HashMap<String,Object>> search(String filename) {
		File file=new File(filename);
		ReadAudioFile readFile = new ReadAudioFile(); 
		try {
			float data[]=readFile.readFile(file);
			Fingerprint fp=new Fingerprint(data, fs);
			setHashTime(fp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[] id = search(linkTime, linkHash, minHit);
		/*if (id[i]<0){
			System.out.println("--------------------------error!--id="+id);
			return null;
		}
		else{*/
		for (int i=0; i<3; i++){
			System.out.println("----------------------------id="+id[i]+",");
		}
		List<HashMap<String,Object>> audios = audioMapper.getAudiosByIDs(id);
		
		//audios里面按照id排序了，不是原来的顺序
		for (int i = 0; i < audios.size(); i++) {
			int j=0;
			long idmusicinfo = 0;
			String title;
			Map<String, Object> map = audios.get(i);
			Iterator iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				String string = (String) iterator.next();
				System.out.println("string:"+string);
				if (j==0){
					idmusicinfo=(long) map.get(string);
					//System.out.println("id:"+idmusicinfo);
				}
				/*if (j==5){
					title=map.get(string).toString();
					System.out.println("title:"+title);
				}*/
				if (j==4){
					for (int k=0; k<3; k++){
						String info = infoIds.get(k).getValue().toString();
						//System.out.println("1:xsd:"+info+"id:"+idmusicinfo+"string:"+string+"id2:"+infoIds.get(k).getKey());
						if (Hash2id(infoIds.get(k).getKey()) == idmusicinfo){
							//System.out.println("2:xsd:"+info+"id:"+idmusicinfo+"string:"+string+"id2:"+infoIds.get(k).getKey());
							map.put(string, info);
						}
					}
				}
				
				// 把相似度值放入备注中
				/*if (j==4){
					String info = infoIds.get(i).getValue().toString();
					System.out.println("info:"+info);
					for (int k=0; k<3; k++){
						if (idmusicinfo==id[k]){
							map.put(string, info);       //string:infodir info:26
						}
					}
				}*/
				j++;
			}
		}

		return audios;
	}
	
	public int[] search(int[] linkTime, int[] linkHash, int minHit){
        HashMap<Integer,Integer> linkHashMap = new HashMap<>(linkHash.length);
        for(int i = 0; i < linkHash.length; i ++){             //client端 hash,time
            linkHashMap.put(linkHash[i],linkTime[i]);
        }
        hashMap = new HashMap<>(400000);
        List<HashTable> audios= audioMapper.searchAllAudios(linkHash);
        for (int i=0; i<audios.size(); i++){
        	int hash = audios.get(i).getHash();
            int id = audios.get(i).getId();
            int time = audios.get(i).getTime(); 
            Integer count;
            Long idHash = idHash(id,linkHashMap.get(hash) - time);
            count = hashMap.get(idHash);
            if(count == null) count = 0;
            hashMap.put(idHash,count + 1);
        }

        /*hashMap.forEach((hash, integer) -> {
            if(integer > minHit && integer > maxCount){
                maxId = hash;
                maxCount = integer;
            }
        });*/
        //装的id和相似度值
        infoIds = new ArrayList<HashMap.Entry<Long, Integer>>(hashMap.entrySet());
        Collections.sort(infoIds, new Comparator<HashMap.Entry<Long, Integer>>() {   
            public int compare(HashMap.Entry<Long, Integer> o1, HashMap.Entry<Long, Integer> o2) {      
                return (o2.getValue() - o1.getValue()); 
                //return (o1.getKey()).toString().compareTo(o2.getKey());
            }
        }); 
        //System.out.println("----------maxID="+maxId+"----------------maxCount="+maxCount);
        for (int i = 0; i < infoIds.size(); i++) {
            String what = infoIds.get(i).toString();
            Long what1=infoIds.get(i).getKey();
            Integer what2=infoIds.get(i).getValue();
            //System.out.println(what+","+what1+","+what2);
        }
        
        int[] ids=new int[3];
        for (int i=0; i<3; i++){
        	maxId=infoIds.get(i).getKey();
        	ids[i]=Hash2id(maxId);
        }
        
        return ids;
    }

	
	public void setHashTime(Fingerprint fp){
		ArrayList<Fingerprint.Link> linkList =  fp.getLinkList();
        linkHash = new int[linkList.size()];   //159
        linkTime = new int[linkList.size()];
        for(int i = 0; i < linkHash.length; i ++){
        	linkHash[i] = Hash.hash(linkList.get(i));
            linkTime[i] = linkList.get(i).start.intTime;
        }
	}
	
	@Override
	public List<HashMap<String,Object>> insert(String filedir) {
		//处理目录文件
		FileHandle filehandle = new FileHandle(filedir);
		//获取目录文件名称
		fileList=filehandle.getName();    //C:\Users\yy\Desktop\songs\samples\Applause}}ARTPOP}}Lady GaGa.wav
		ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (String aName : fileList) {
            if(!aName.substring(aName.length() - 4).equals(".wav"))
                continue;
            String filename=aName;
            
            executorService.execute(() -> {
                System.out.println(filename);
                dbinsert(filename);
            });
        }

        executorService.shutdown();
		
		//audioMapper.addMusic(music);
		//获取最新数据，进入显示数据的页面
		List<HashMap<String,Object>> audios= audioMapper.getAllAudios();
		return audios;
	}
	
	public synchronized void dbinsert(String filename){  
		ReadAudioFile readFile = new ReadAudioFile();    //读取文件的音频流
        try {
    		File file = new File(filename);
            
    		float data[]=readFile.readFile(file);  //获取单声道采样率8000的音频数据
            readFile.music.setFiledir(filename);
            
            audioMapper.addMusic(readFile.music);   //插入数据到musicinfo表
            
            int id=readFile.music.getIdmusicinfo();  //记录下id (mybatis把自增主键绑定到了music对象的idmusicinfo里去)
           
            Fingerprint fp = new Fingerprint(data, fs);   
            
            for(Fingerprint.Link link : fp.getLinkList()){   //插入数据到hashtable表
            	HashTable hashtable=new HashTable(id,link);
            	audioMapper.addHash(hashtable);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
	}
	
	 public static Long idHash(int id, int time){
	        return (long) ((id << 16) + time + (1 << 15));     //左移==乘2  右移==除2
	 }

    public static int Hash2id(Long idHash){
        return (int)(idHash >> 16);
    }

	@Override
	public List<HashMap<String, Object>> listAll() {
		List<HashMap<String,Object>> audios= audioMapper.getAllAudios();
		
		return audios;
	}
	
	@Override
	public void startrecord(){
		
	}
}


