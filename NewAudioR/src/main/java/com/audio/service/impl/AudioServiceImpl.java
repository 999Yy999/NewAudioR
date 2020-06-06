package com.audio.service.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
//import org.tritonus.sampled.convert.PCM2PCMConversionProvider;

import com.audio.entity.HashTable;
import com.audio.entity.Music;
import com.audio.entity.PageBean;
import com.audio.mapper.AudioMapper;
import com.audio.service.AudioService;
import com.audio.util.ClipAudioFile;
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
    Music music=new Music();
  //D:\Z_毕设\Others Project\Audio-Fingerprinting-master\songs\泡沫\泡沫4s.wav
  
    
    @Override
	public List<HashMap<String, Object>> helpsearch(String filename, String artist, String album) {
    	// 先识别出所有相似歌曲（代码冗余了，改），并添加相似度值，并初次筛掉hit<5的音乐
    	float data[];
    	File file=new File(filename);
    	ReadAudioFile readFile = new ReadAudioFile(); 
    	try {
			data=readFile.readFile(file);
			Fingerprint fp=new Fingerprint(data, fs);
			setHashTime(fp);
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	int[] id = searchall(linkTime, linkHash, minHit);
    	if (id==null){
			System.out.println("--------------------------时间太短 或 噪声太大，未在数据库中找到相同指纹!--");
			return null;
		}
    	System.out.println("----------------------------idsize="+id.length+",");
    	/*for (int i=0; i<id.length; i++){
			System.out.println("----------------------------id="+id[i]+",");
		}*/
		List<HashMap<String,Object>> audios = audioMapper.getAudiosByIDs(id);
		//audios里面按照id排序了，不是原来的顺序
		System.out.println("audios.size:"+audios.size());
		for (int i=0; i < audios.size(); i++){
			long idmusicinfo = (long) audios.get(i).get("idmusicinfo");
			//System.out.println("id:"+idmusicinfo);
			String similar=(String) audios.get(i).get("infodir");
			//System.out.println("----------before-infodir:"+audios.get(i).get("infodir"));
			for (int k=0; k<id.length; k++){
				if (Hash2id(infoIds.get(k).getKey()) == idmusicinfo && audios.get(i).get("infodir").toString().isEmpty()){
					String info = infoIds.get(k).getValue().toString();
					audios.get(i).put("infodir", info);
					//System.out.println("id:"+idmusicinfo+",相似度:"+info);
				}
			}
		}
    	// 再根据歌手、专辑来二次筛选
		List<Music> musicList = new ArrayList();
		List<HashMap<String,Object>>  audiosListTop3 = new ArrayList();
		Music music;
		//List<HashMap<String,Object>> audios2 = audioMapper.searchAudiosByConditions(id, artist, album);
		for (int i=0; i < audios.size(); i++){
			long idmusicinfo=(long) audios.get(i).get("idmusicinfo");
			String ititle=(String) audios.get(i).get("title");
			String iartist=(String) audios.get(i).get("artist");
			String ialbum=(String) audios.get(i).get("album");
			String ifiledir=(String) audios.get(i).get("filedir");
			String iinfodir=(String) audios.get(i).get("infodir");
			System.out.println("添加相似度后：id:"+idmusicinfo+",title:"+ititle+",artist:"+iartist+",album:"+ialbum+",filedir:"+ifiledir+",infodir:"+iinfodir);
			if (iartist.indexOf(artist)!=-1 || ialbum.indexOf(album)!=-1){
				System.out.println("im here~");
				music=new Music(idmusicinfo, ititle, iartist, ialbum, ifiledir, iinfodir);
				musicList.add(music);
			}
		}
		System.out.println("---------musiclist.size:"+musicList.size());
		for (int i=0; i<musicList.size(); i++){
			musicList.get(i).showMusicInfo();
		}
    	// 从中选出相似度高的前三首歌曲
		musicList.sort( new Comparator<Music>(){                
			public int compare(Music arg0, Music arg1) {
			//这里是根据ID来排序，所以它为空的要剔除掉                    
			//if(arg0.getId()==null || arg1.getId()==null) return 0;
			//return arg0.getInfodir().compareTo(arg1.getInfodir());   
				//return arg1.getInfodir().compareTo(arg0.getInfodir());   //升序
				return Integer.parseInt(arg1.getInfodir())-Integer.parseInt(arg0.getInfodir()); //降序
			}                      
		});  
		System.out.println("");
		for (int i=0; i<musicList.size(); i++){
			musicList.get(i).showMusicInfo();
		}
		
		HashMap<String,Object> hash;
		
		//for (int i=0; i<musicList.size(); i++){
		for (int i=0; i<3; i++){
			hash = new HashMap<>();
			hash.put("idmusicinfo", musicList.get(i).getIdmusicinfo());
			hash.put("title", musicList.get(i).getTitle());
			hash.put("artist", musicList.get(i).getArtist());
			hash.put("album", musicList.get(i).getAlbum());
			hash.put("filedir", musicList.get(i).getFiledir());
			hash.put("infodir", musicList.get(i).getInfodir());
			
			System.out.println("排序后：id:"+musicList.get(i).getIdmusicinfo()+",title:"+musicList.get(i).getTitle()+",artist:"+musicList.get(i).getArtist()+",album:"+musicList.get(i).getAlbum()+",filedir:"+musicList.get(i).getFiledir()+",infodir:"+musicList.get(i).getInfodir());
			audiosListTop3.add(hash);
		}
		System.out.println("audiosListTop3.size():"+audiosListTop3.size());
		//for (int i=0; i<musicList.size(); i++){
		for (int i=0; i<3; i++){
			System.out.println("hash添加后：id:"+audiosListTop3.get(i).get("idmusicinfo")+",title:"+audiosListTop3.get(i).get("title")+",artist:"+audiosListTop3.get(i).get("artist")+",album:"+audiosListTop3.get(i).get("album")+",filedir:"+audiosListTop3.get(i).get("filedir")+",infodir:"+audiosListTop3.get(i).get("infodir"));
		}
		
		return audiosListTop3;
	}
    
    public int[] searchall(int[] linkTime, int[] linkHash, int minHit){
        HashMap<Integer,Integer> linkHashMap = new HashMap<>(linkHash.length);
        System.out.println("---------fingerprint parameter length:"+linkHash.length);
        for(int i = 0; i < linkHash.length; i ++){             //client端 hash,time
            linkHashMap.put(linkHash[i],linkTime[i]);
        }
        hashMap = new HashMap<>(400000);
        if (linkHash.length==0){
        	return null;
        }
        List<HashTable> audios= audioMapper.searchAllAudios(linkHash);
        for (int i=0; i<audios.size(); i++){
        	int hash = audios.get(i).getHash();
            long id = audios.get(i).getId();
            int time = audios.get(i).getTime();     //t1
            Integer count;
            Long idHash = idHash(id,linkHashMap.get(hash) - time);   // sample time - database time
            count = hashMap.get(idHash);
            if(count == null) count = 0;
            hashMap.put(idHash,count + 1);      //idHash已存在时，更新value值，统计时间差，cnt越大，相似度越高
        }
        //装的id和相似度值，升序排序
        infoIds = new ArrayList<HashMap.Entry<Long, Integer>>(hashMap.entrySet());
        Collections.sort(infoIds, new Comparator<HashMap.Entry<Long, Integer>>() {   
            public int compare(HashMap.Entry<Long, Integer> o1, HashMap.Entry<Long, Integer> o2) {      
                return (o2.getValue() - o1.getValue()); 
            }
        }); 
        System.out.println("---------infoids.size:"+infoIds.size());
        //System.out.println("----------maxID="+maxId+"----------------maxCount="+maxCount);
        /*for (int i = 0; i < infoIds.size(); i++) {
            String what = infoIds.get(i).toString();
            Long what1=infoIds.get(i).getKey();
            Integer what2=infoIds.get(i).getValue();
        }*/
        
        //int[] ids=new int[3];
        int[] ids = new int[500];   //设定最多返回500个id(可能重复)
        for (int i=0; i<500; i++){
        	if (infoIds.get(i).getValue()<5){break;}     //相似度小于5，则停止加入ids
        	maxId=infoIds.get(i).getKey();          //idhash
        	//System.out.println("---------(IDhash,相似度,id):"+"("+maxId+","+infoIds.get(i).getValue()+","+Hash2id(maxId)+")");
        	ids[i]=Hash2id(maxId);   //id
        }
        
        return ids;
    }
    
    
	@Override
	public List<HashMap<String,Object>> search(String filename, float[] data1) {
		File file=new File(filename);
		float data[];
		
		ReadAudioFile readFile = new ReadAudioFile(); 
		try {
			if (data1!=null){data=data1;}             //测试时截取音乐片段直接传的data浮点型数组过来
			else data=readFile.readFile(file);        //通过音乐文件名获取音频浮点型数据
			Fingerprint fp=new Fingerprint(data, fs);
			setHashTime(fp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int[] id = search(linkTime, linkHash, minHit);
		if (id==null){
			System.out.println("--------------------------时间太短 或 噪声太大，未在数据库中找到相同指纹!--");
			return null;
		}
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
        System.out.println("---------fingerprint parameter length:"+linkHash.length);
        for(int i = 0; i < linkHash.length; i ++){             //client端 hash,time
        	//System.out.println("-------linkHash["+i+"]"+"="+linkHash[i]);
            linkHashMap.put(linkHash[i],linkTime[i]);
        }
        hashMap = new HashMap<>(400000);
        if (linkHash.length==0){
        	return null;
        }
        List<HashTable> audios= audioMapper.searchAllAudios(linkHash);
        for (int i=0; i<audios.size(); i++){
        	int hash = audios.get(i).getHash();
            long id = audios.get(i).getId();
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
        /*
        for (int i = 0; i < infoIds.size(); i++) {
            String what = infoIds.get(i).toString();
            Long what1=infoIds.get(i).getKey();
            Integer what2=infoIds.get(i).getValue();
            //System.out.println(what+","+what1+","+what2);
        }*/
        
        int[] ids=new int[3];
        for (int i=0; i<3; i++){
        	maxId=infoIds.get(i).getKey();
        	ids[i]=Hash2id(maxId);
        }
        
        return ids;
    }

	
	public void setHashTime(Fingerprint fp){
		ArrayList<Fingerprint.Link> linkList =  fp.getLinkList();
		//System.out.println("---------fingerprint length:"+linkList.size());
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
            
            long id=readFile.music.getIdmusicinfo();  //记录下id (mybatis把自增主键绑定到了music对象的idmusicinfo里去)
           
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
	
	 public static Long idHash(long id, int time){
	        return (long) ((id << 16) + time + (1 << 15));    //左移==乘2  右移==除2
	 }

    public static int Hash2id(Long idHash){
        return (int)(idHash >> 16);
    }

	@Override
	public List<HashMap<String, Object>> listAll() {
		List<HashMap<String,Object>> audios= audioMapper.getAllAudios();
		
		return audios;
	}
	
	/*@Override
	public void startrecord(){
		???what??
	}*/

	@Override
	public String[] test(String time, int fre, int sample) {
		int maxmusicdata=1003;               //音乐库最大歌曲数
		int success1=0, success2=0, failure1=0, failure2=0;    //记录器
		String path="D:\\音乐素材\\wav";
		File file = new File(path);
        File[] files = file.listFiles();    //目录里的文件
		double[] zql = new double[2];                //两个准确率
		String filename = null;             //选中的样本文件名
		// 准备sample个样本
		Random random=new Random();
		for (int i=0; i<sample; i++) {
			System.out.println("-----------i:"+i);
			//随机选择一个音频文件
			int id=random.nextInt(maxmusicdata);           //[0,9]
			if (files!=null&&files.length > 0) {
                filename=files[id].getName();    //我等你到三十五岁}}我等你到三十五岁}}晃儿.wav
            } 
			System.out.println("filename:"+filename);
			//分隔符提取歌曲属性
			getTabs(filename);
			filename=path+"\\"+filename;
			//随机确定截取音频时长
			int mode = 0;
			int n1 = 0,n2 = 0;    //[n1,n2]随机数
			int durtime;
			switch (time){
			case "mode1": mode=1; break;
			case "mode2": mode=2; break;
			case "mode3": mode=3; break;
			}
			if (mode==1){n1=1; n2=4;}
			if (mode==2){n1=4; n2=10;}
			if (mode==3){n1=10; n2=20;}
			//durtime=random.nextInt() * (n2-n1)+n1;
			durtime=random.nextInt(n2-n1)+n1;
			System.out.println("----------durtime:"+durtime);
			//随机确定截取位置
			int pos=random.nextInt(5)+1;   //[1,5]
			//开始截取+叠加 返回处理好的数据data
			float data[]=cut2short(filename, durtime, pos, fre);
			//search测试混合信号
			List<HashMap<String, Object>> audios=search(filename,data);
			// 测试结果与源文件(名称+歌手+专辑)对比
			int flag=0;  //判断是否在top3
			if (audios==null) {continue;}
			System.out.println("-------------audios.size()="+audios.size());
			if (audios.size()==1){
				String title=(String) audios.get(0).get("title");
				String album=(String) audios.get(0).get("album");
				String artist=(String) audios.get(0).get("artist");
				System.out.println("----original music: title:"+music.getTitle()+",artist:"+music.getArtist()+",album:"+music.getAlbum());
				System.out.println("----reuslt music: title:"+title+",artist:"+artist+",album:"+album);
				if (music.getTitle().equals(title) && music.getArtist().equals(artist) && music.getAlbum().equals(album)){
					success1++;
					success2++;
					System.out.println("here1-------------success12++");
				}else{
					failure1++;
					failure2++;
					System.out.println("here2-------------failure12++");
				}
			}
			else{
				int maxrate=0;
				String maxartist = null,maxalbum = null,maxtitle = null;
				for (int k = 0; k < audios.size(); k++) {
					int j=0;
					//long idmusicinfo = 0;
					String title = null,artist=null,album=null;
					int samerate;
					
					Map<String, Object> map = audios.get(k);
					Iterator iterator = map.keySet().iterator();
					title=(String) map.get("title");
					while (iterator.hasNext()) {
						String string = (String) iterator.next();
						//System.out.println("string:"+string);
						if (j==1){
							artist=map.get(string).toString();
						}
						if (j==2){
							album=map.get(string).toString();
						}
						if (j==5){
							title=map.get(string).toString();
							System.out.println("title:"+title);
						}
						if (j==4){
							samerate=Integer.parseInt((String) map.get(string));
							System.out.println("samerate:"+samerate);
							//找出相似度最大的
							if (samerate>maxrate){
								maxrate=samerate;
								maxartist=artist;
								//System.out.println("-------------maxartist赋值成功"+maxartist);
								maxalbum=album;
								//System.out.println("-------------maxartist赋值成功"+maxalbum);
								maxtitle=title;  //上面为它赋值了，这段代码改下，不需要用迭代器，直接用属性当键取值
								//System.out.println("-------------maxartist赋值成功"+maxtitle);
							}
						}
						j++;
					}
					System.out.println("---------maxrate="+maxrate);
					System.out.println("----original music: title:"+music.getTitle()+",artist:"+music.getArtist()+",album:"+music.getAlbum());
					System.out.println("----reuslt music: title:"+title+",artist:"+artist+",album:"+album);
					if (flag==0 && music.getTitle().equals(title) && music.getArtist().equals(artist) && music.getAlbum().equals(album)){
						flag=1;
						success2++;
						System.out.println("here3-------------success2++");
					}
				}
				if (flag==0) {failure2++; System.out.println("-------------failure2++");}
				System.out.println("-----------maxtitle:"+maxtitle+",maxartist:"+maxartist+",maxalbum:"+maxalbum);
				if (music.getTitle().equals(maxtitle) && music.getArtist().equals(maxartist) && music.getAlbum().equals(maxalbum)){
					System.out.println("here4-------------success1++");
					success1++;
				}else {failure1++; System.out.println("here5-------------failure1++");}
			}
			System.out.println("end-------------success1:"+success1+",success2:"+success2+",failure1:"+failure1+",failure2:"+failure2);
		}
			
		
		// 6.计数
		// 7.计算准确率
		zql[0]=success1*1.0/sample;
		zql[1]=success2*1.0/sample;
		
		DecimalFormat df = new DecimalFormat("0.0000");
		String[] str = new String[2]; 
		str[0] = df.format(zql[0]);
		str[1] = df.format(zql[1]);
		System.out.println(str[0]+","+str[1]);
		
		
		/*BigDecimal bg1 = new BigDecimal(zql[0]);
		zql[0] = bg1.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		BigDecimal bg2 = new BigDecimal(zql[1]);
		zql[1] = bg2.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();*/
		
		System.out.println("----------zql1="+zql[0]+" = "+success1+"/"+sample);
		System.out.println("----------zql2="+zql[1]+" = "+success2+"/"+sample);
		
		return str;
		//HashMap<String, Object> zqlmap = new HashMap<>();
		//zqlmap.put("zql1", zql[0]);
		//zqlmap.put("zql2", zql[1]);
		
		//return zqlmap;
	}
	
	// 2.模拟生成fre频率的白噪声
	// 3.叠加成混合信号
	//将sample个样本按mode要求剪辑，存入文件，返回文件名称
	float[] cut2short(String filename, int durtime, int pos, int fre){
		float[] data=null;
		ClipAudioFile clipfile=new ClipAudioFile();
		try {
			//截取+叠加
			System.out.println("--------filename:"+filename+",durtime:"+durtime+",pos:"+pos+",fre:"+fre+"----------");
			data=clipfile.clipFile(filename, durtime, pos, fre);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	/*
	//获得不重复的[0-n)的随机数
	//获取随机数，效率很高的一种方法
	public static int[] getRandomNumber(int n){
	    int[] x = new int[n];
	    for(int i = 0; i < n; i++)
	    {
	      x[i] = i;
	    }
	    Random r = new Random();
	    for(int i = 0; i < n; i++)
	    {
	      int in = r.nextInt(n - i) + i;
	      int t = x[in];
	      x[in] = x[i];
	      x[i] = t;
	    }
	    System.out.println(Arrays.toString(x));
	    return x;
	 }
	
	//获得随机数的文件
	//获取随机文件顺序
	private static String[] getRandomFile(String path, int[] x) {
		int k=0,j=0;
		String[] randomfile = new String[x.length];
        try {
            File file = new File(path);
            File[] files = file.listFiles();
            for (int rnum : x) {
	            if (files!=null&&files.length > 0) {
	                randomfile[j++]=files[rnum].getName();
	            } else {
	                return null;
	            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return randomfile;
    }

	*/
	
	private void getTabs(String filename){
		String[] strings = filename.split("}}");
		if(strings.length < 3){
			music.setTitle(filename);
            music.setAlbum("");
            music.setArtist("");
            return;
        }
        music.setTitle(strings[0]);
        music.setAlbum(strings[1]);
        //remove .wav
        int len = strings[2].length();
        music.setArtist(strings[2].substring(0,len - 4));
	}
	
	@Override
	public void startrecord() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PageBean<HashMap<String, Object>> listMusicByPage(int pc, int ps) {
		//获取表格的总的记录数， 用于计算共多少页计算的方法（算法）在实体类PageBean中
		int count = audioMapper.getTotalCount();
		System.out.println("cnt="+count);
		//创建分页实体，封装相关的数据
		PageBean<HashMap<String,Object>> pb = new PageBean<HashMap<String,Object>>();
		pb.setPc(pc);//当前页数 比如 第一页 为 1,第二页 为 2 
		pb.setPs(ps);//每页记录数 比如每页显示 5 行
		pb.setTr(count);//总记录数  比如  总共有为10 行
		//获取带条件的查询的当前页的数据
		List<HashMap<String,Object>> users = audioMapper.getMusicByPage(pc,ps);
		//将查询的数据封装到分页实体类
		pb.setBeanList(users);
		
		return pb;
	}

	@Override
	public List<HashMap<String, Object>> deleteMusicById(Integer id) {
		// 删除音乐表中记录
		audioMapper.deleteAudio1(id);
		//删除指纹表中记录
		audioMapper.deleteAudio2(id);
		//返回最新数据
		List<HashMap<String, Object>> audios = audioMapper.getAllAudios();
		return audios;
	}
}


