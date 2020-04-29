package com.audio.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.audio.entity.HashTable;
import com.audio.entity.Music;

public interface AudioMapper {

	public int addMusic(@Param("entity") Music music);
	public List<HashMap<String, Object>> getAllAudios();
	public int addHash(HashTable hashtable);
	public List<HashMap<String,Object>> getAudiosByIDs(int[] id);
	public List<HashTable> searchAllAudios(int[] linkHash);
	
	//public List<HashTable> searchAllAudios(Map linkHash);
}
