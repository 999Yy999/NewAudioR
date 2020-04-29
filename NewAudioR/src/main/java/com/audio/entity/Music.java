package com.audio.entity;

public class Music {
	private int idmusicinfo;
	private String title;
	private String artist;
	private String album;
	private String filedir;
	private String infodir;
	
	public String getInfodir() {
		return infodir;
	}
	public void setInfodir(String infodir) {
		this.infodir = infodir;
	}
	public int getIdmusicinfo() {
		return idmusicinfo;
	}
	public void setIdmuiscinfo(int idmusicinfo) {
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
