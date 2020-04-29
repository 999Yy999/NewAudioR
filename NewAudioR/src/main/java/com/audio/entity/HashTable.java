package com.audio.entity;

import com.audio.util.Hash;
import com.audio.util.fingerprint.Fingerprint;

public class HashTable {
	private int hashtableid;
	private int hash;
	private int id;
	private int time;
	
	public HashTable(){
		super();
	}
	
	public HashTable(int id,Fingerprint.Link link){
		super();
        this.id = id;
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
	public int getId() {
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
