package com.audio.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.audio.entity.Music;
import com.audio.entity.PageBean;

@Service
public interface AudioService {
	
	public List<HashMap<String,Object>> insert(String filedir);

	public List<HashMap<String, Object>> search(String filename, float[] data1);

	public List<HashMap<String, Object>> listAll();

	public void startrecord();
	
	public String[] test(String time, int fre, int sample);
	//public HashMap<String, Object> test(String time, int fre, int sample);

	public PageBean<HashMap<String, Object>> listMusicByPage(int pc, int ps);

	public List<HashMap<String, Object>> deleteMusicById(Integer id);

	//List<HashMap<String, Object>> search(String filename, float[] data1);
	
}
