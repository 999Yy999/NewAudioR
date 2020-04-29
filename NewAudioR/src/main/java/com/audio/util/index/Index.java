package com.audio.util.index;

import com.audio.util.Hash;
import com.audio.util.fingerprint.Fingerprint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Index {                    //client.java没有用到Index           //not used
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
        for(int i = 0; i < linkHash.length; i ++){     //client端 hash,time
            linkHashMap.put(linkHash[i],linkTime[i]);
        }

        /*ResultSet rs = sqlDB.searchAll(linkHash);    //与159个hash值相同的结果集 :1655个指纹 

        try {
            while(rs.next()){                //server端
                int hash = rs.getInt(2);
                int id = rs.getInt(3);
                int time = rs.getInt(4);     //锚点的t1
                
                //Hits hits = new Hits(id, linkHashMap.get(hash) - time);
                Integer count;
                //if(hashMap.containsKey(hits))
                Long idHash = idHash(id,linkHashMap.get(hash) - time); //时间差sample time-database time
                count = hashMap.get(idHash);
                if(count == null) count = 0;
                hashMap.put(idHash,count + 1);  //idhash已存在，则value更新, 统计时间差，cnt越大相似度越高
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
        return (long) ((id << 16) + time + (1 << 15));     //左移==乘2  右移==除2
    }

    public static int Hash2id(Long idHash){
        return (int)(idHash >> 16);
    }
}
