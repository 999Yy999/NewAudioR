package com.audio.util;

import com.audio.util.fingerprint.Fingerprint;

public class Hash {
    public static int hash(Fingerprint.Link link){
        int dt = link.end.intTime - link.start.intTime; //#(by yecheng)
        int df = link.end.intFreq - link.start.intFreq + 300; //# 300(by yecheng)
        int freq = link.start.intFreq; //# 5000(by yecheng)

        return freq + 5000*(df + 600 * dt);
    }

    @SuppressWarnings("unused")
    public static int[] hash2link(int hash){
        int freq = hash % 5000;                 //21
        int df = (hash / 5000) % 600;           //300
        int dt = hash / 5000 / 600;             //41

        return new int[] {freq,df,dt};
    }
}
