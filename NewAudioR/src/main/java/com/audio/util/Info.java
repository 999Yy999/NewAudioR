package com.audio.util;

import com.audio.util.Hash;
import com.audio.util.fingerprint.Fingerprint;
import com.audio.util.fingerprint.Fingerprint.Link;

                                     //not used
public class Info {                  //search.javaû�õ�Info, insert�����õ�
	public final int hash;
    public final int id;
    public final int time;

	public int getHash() {
		return hash;
	}

	public int getId() {
		return id;
	}

	public int getTime() {
		return time;
	}

	public Info(int id,Fingerprint.Link link) {
        super();
        this.id = id;
        this.time = link.start.intTime;     //t1
        this.hash = Hash.hash(link);        //
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
