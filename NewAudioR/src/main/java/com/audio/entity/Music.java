package com.audio.entity;

public class Music {
	private long idmusicinfo;
	private String title;
	private String artist;
	private String album;
	private String filedir;
	private String infodir;
	
	public Music(){}
	
	public Music(long id, String title, String artist, String album, String filedir, String infodir){
		this.idmusicinfo=id;
		this.title=title;
		this.artist=artist;
		this.album=album;
		this.filedir=filedir;
		this.infodir=infodir;
	}
	
	public void showMusicInfo(){
		System.out.println("id:"+this.getIdmusicinfo()+",title:"+this.getTitle()+",artist:"+this.getArtist()+",album:"+this.getAlbum()+",filedir:"+this.getFiledir()+",infodir:"+this.getInfodir());
	}
	
	public String getInfodir() {
		return infodir;
	}
	public void setInfodir(String infodir) {
		this.infodir = infodir;
	}
	public long getIdmusicinfo() {
		return idmusicinfo;
	}
	public void setIdmuiscinfo(long idmusicinfo) {
		this.idmusicinfo = idmusicinfo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getFiledir() {
		return filedir;
	}
	public void setFiledir(String filedir) {
		this.filedir = filedir;
	}
}
