package com.audio.entity;

import com.audio.util.Hash;
import com.audio.util.fingerprint.Fingerprint;

public class HashTable {
	private int hashtableid;
	private int hash;
	private long id;
	private int time;
	
	public HashTable(){
		super();
	}
	
	public HashTable(long id2,Fingerprint.Link link){
		super();
        this.id = id2;
        this.time = link.start.intTime;     //t1
        this.hash = Hash.hash(link);        //
	}
	
	public int getHashtableid() {
		return hashtableid;
	}
	public void setHashtableid(int hashtableid) {
		this.hashtableid = hashtableid;
	}
	public int getHash() {
		return hash;
	}
	public void setHash(int hash) {
		this.hash = hash;
	}
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}
