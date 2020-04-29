package com.audio.util.index;

import com.audio.util.Hash;
import com.audio.util.fingerprint.Fingerprint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Index {                    //client.javaû���õ�Index           //not used
    private final HashMap<Long,Integer> hashMap;
    //private MysqlDB sqlDB;
    public Index() {
        super();
        hashMap = new HashMap<>(400000);
    }

    /*public void loadDB(MysqlDB mysql){
        sqlDB = mysql;
    }*/

    private long maxId = -1;
    private int maxCount = -1;
    public int search(Fingerprint fp, @SuppressWarnings("SameParameterValue") int minHit){
        ArrayList<Fingerprint.Link> linkList =  fp.getLinkList();
        //System.out.println("size="+linkList.size());
        int[] linkHash = new int[linkList.size()];   //159
        int[] linkTime = new int[linkList.size()];
        for(int i = 0; i < linkHash.length; i ++){
            linkHash[i] = Hash.hash(linkList.get(i));
            linkTime[i] = linkList.get(i).start.intTime;
            //System.out.println(linkHash[i]);
        }

        return search(linkTime, linkHash,minHit);    
    }

    @SuppressWarnings("WeakerAccess")
    public int search(int[] linkTime, int[] linkHash, int minHit){
        HashMap<Integer,Integer> linkHashMap = new HashMap<>(linkHash.length);
        for(int i = 0; i < linkHash.length; i ++){     //client�� hash,time
            linkHashMap.put(linkHash[i],linkTime[i]);
        }

        /*ResultSet rs = sqlDB.searchAll(linkHash);    //��159��hashֵ��ͬ�Ľ���� :1655��ָ�� 

        try {
            while(rs.next()){                //server��
                int hash = rs.getInt(2);
                int id = rs.getInt(3);
                int time = rs.getInt(4);     //ê���t1
                
                //Hits hits = new Hits(id, linkHashMap.get(hash) - time);
                Integer count;
                //if(hashMap.containsKey(hits))
                Long idHash = idHash(id,linkHashMap.get(hash) - time); //ʱ���sample time-database time
                count = hashMap.get(idHash);
                if(count == null) count = 0;
                hashMap.put(idHash,count + 1);  //idhash�Ѵ��ڣ���value����, ͳ��ʱ��cntԽ�����ƶ�Խ��
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }*/

        hashMap.forEach((hash, integer) -> {
            if(integer > minHit && integer > maxCount){
                maxId = hash;
                maxCount = integer;
            }
        });

        return Hash2id(maxId);
    }

    public static Long idHash(int id, int time){
        return (long) ((id << 16) + time + (1 << 15));     //����==��2  ����==��2
    }

    public static int Hash2id(Long idHash){
        return (int)(idHash >> 16);
    }
}
