package com.audio.util;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.tools.javac.util.List;

public class FileHandle {
	private String dir;
	File file;
	public FileHandle(String filedir){
		file = new File(filedir);
		dir=filedir;
	}
	
	public ArrayList<String> getName(){
		String[] name;
        if(file.isDirectory()){
            name = file.list();
            System.out.println("isDirectory!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }else {
        	System.out.println("Not a directory! please input again.");
        	return null;
        }
        
        ArrayList<String> nameList=new ArrayList<String>();

        //处理文件，移交insert给下一层
        for (String aName : name) {
            if(!aName.substring(aName.length() - 4).equals(".wav"))
                continue;
            String filename = dir + "\\" + aName;
            nameList.add(filename);
        }
        return nameList;
	}
}
