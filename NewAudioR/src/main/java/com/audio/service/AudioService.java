package com.audio.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.audio.entity.Music;

@Service
public interface AudioService {
	
	public List<HashMap<String,Object>> insert(String filedir);

	public List<HashMap<String, Object>> search(String filename);

	public List<HashMap<String, Object>> listAll();

	public void startrecord();
	
}