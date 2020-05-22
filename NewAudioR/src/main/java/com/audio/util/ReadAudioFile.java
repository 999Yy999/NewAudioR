package com.audio.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import com.audio.entity.Music;

public class ReadAudioFile {
	public Music music;
	public ReadAudioFile() {
        super();
        music=new Music();
    }
	
	private void getTabs(String filename){
		String[] strings = filename.split("}}");
		if(strings.length < 3){
			music.setTitle(filename);
            music.setAlbum("");
            music.setArtist("");
            return;
        }
        music.setTitle(strings[0]);
        music.setAlbum(strings[1]);
        //remove .wav
        int len = strings[2].length();
        music.setArtist(strings[2].substring(0,len - 4));
	}
	
    public float[] readFile(File file) throws Exception{
        AudioInputStream stream;
        try {
            stream = AudioSystem.getAudioInputStream(file);  // 从提供的 File 获得音频输入流
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        AudioFormat format = stream.getFormat();        // 获得 此音频输入流中声音数据的  音频格式

        if(format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED){
            throw new Exception("Encoding must be PCM_SIGNED!");
        }
        if(format.getSampleRate() != 8000){         // 获取样本速率,即每秒样本数
            throw new Exception("SampleRate must be 8000!");
        }
        
        int len = (int) stream.getFrameLength();   // 获得流的长度，以示例帧为单位  sample_rate: 一秒采样8000个帧֡
        System.out.println("len="+len+",available="+stream.available());    //len/sample_rate = 时长（单位：秒）
         
        float[] dataL = new float[len];     // 左右声道数据
        float[] dataR = new float[len];

        
        ByteBuffer buf = ByteBuffer.allocate(4 * len);  // 字节缓冲  4*len???    float等于4byte？？？      ans:一帧=4字节
        byte[] bytes = new byte[4 * len];
        try {
            //noinspection ResultOfMethodCallIgnored
            stream.read(bytes);                        // 读取一定数量的字节,将其存储在 bytes中
            buf.put(bytes);           // 将给定的源 bytes数组的所有内容传输到缓冲区buf中
            buf.rewind();             // 倒带缓冲区（回到开始位置？
            
            for(int i = 0; i < len; i++){
            	buf.order(ByteOrder.LITTLE_ENDIAN);      // 修改字节顺序,little_endian 小端（多字节值的字节顺序是从最低有效位到最高的）
                dataL[i] = buf.getShort() / 32768f;      // short --> float// 读取buf当前位置(开始位置)之后的两个字节，根据当前的字节顺序将它们组成 short值，然后将该位置增加 2。
                dataR[i] = buf.getShort() / 32768f;      // 32768 short的取值范围
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        float[] data = new float[len];
        for(int i = 0; i < len; i++){ 
            data[i] = dataL[i] + dataR[i];      // // 傅里叶变换必须仅在一个通道上应用,取均值, stereo -> mono
            data[i] /= 2;
        }
        
        getTabs(file.getName());
        return data;
        //int fs = 8000;
        //fingerprint = new Fingerprint(data, fs);
    }
	    
}
