package com.audio.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class ClipAudioFile {
	ReadAudioFile readAudioFile=new ReadAudioFile();
	public float[] clipFile(String filename, int durtime, int pos, int fre) throws Exception{
		int fs=8000;
		AudioInputStream stream;
		AudioInputStream nstream;
	
		File file=new File(filename);
		if (fre==0){
			float data[] = readAudioFile.readFile(file);
			return data;
		}
		String nfilename="D:\\音乐素材\\whitenoise\\"+"white"+fre+".wav";
		
		File nfile=new File(nfilename);
		
        try {
            stream = AudioSystem.getAudioInputStream(file);  // 从提供的 File 获得音频输入流
            nstream = AudioSystem.getAudioInputStream(nfile);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        AudioFormat format = stream.getFormat();         // 获得 此音频输入流中声音数据的  音频格式
        AudioFormat nformat = nstream.getFormat();
        
        if(format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED){
            throw new Exception("Encoding must be PCM_SIGNED!");
        }
        if(format.getSampleRate() != 8000){          // 获取样本速率,即每秒样本数
            throw new Exception("SampleRate must be 8000!");
        }
        if(nformat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED){
            throw new Exception("nEncoding must be PCM_SIGNED!");
        }
        if(nformat.getSampleRate() != 8000){         // 获取样本速率,即每秒样本数
            throw new Exception("nSampleRate must be 8000!");
        }
        
        int len = (int) stream.getFrameLength();    // 获得流的长度，以示例帧为单位  sample_rate: 一秒采样8000个帧֡
        int nlen= (int) nstream.getFrameLength();
        int[] clippos=new int[6];
        clippos[1]=0+fs; clippos[5]=len-fs*20; 
        clippos[3]=len/2; clippos[2]=(clippos[3]+clippos[1])/2;
        clippos[4]=(clippos[3]+clippos[5])/2;
        System.out.println("--------pos:"+pos+",pos1:"+ clippos[1]+",pos2:"+clippos[2]+",pos3:"+clippos[3]+",pos4:"+clippos[4]+",pos5:"+clippos[5]);
        
        System.out.println("sample:len="+len+",available="+stream.available());   //len/sample_rate = 时长（单位：秒）
        System.out.println("noise:len="+nlen+",available="+nstream.available());
        
        float[] dataL = new float[durtime*fs];    // 左右声道数据
        float[] dataR = new float[durtime*fs];
        
        float[] ndataL = new float[durtime*fs];    
        float[] ndataR = new float[durtime*fs];
        
        ByteBuffer buf = ByteBuffer.allocate(4 * durtime*fs);   // 字节缓冲  4*len???    float等于4byte？？？      ans:一帧=4字节
        byte[] bytes = new byte[4 * durtime*fs];
        try {
            //noinspection ResultOfMethodCallIgnored
            stream.read(bytes);                        // 读取一定数量的字节,将其存储在 bytes中
            buf.put(bytes);           // 将给定的源 bytes数组的所有内容传输到缓冲区buf中
            buf.rewind();              // 倒带缓冲区（回到开始位置？
            System.out.println("--------start:"+clippos[pos]+",end:"+(clippos[pos]+durtime*fs));
            
            int j=0;
            for (int i = 0; i < len; i++){
            	buf.order(ByteOrder.LITTLE_ENDIAN);
            	if (i>=clippos[pos] && i<clippos[pos]+durtime*fs){
            		dataL[j] = buf.getShort() / 32768f; 
            		dataR[j] = buf.getShort() / 32768f;
            		j++;
            	}
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        ByteBuffer buf1 = ByteBuffer.allocate(4 * durtime*fs); 
        byte[] bytes1 = new byte[4 * durtime*fs];
        try {
            //noinspection ResultOfMethodCallIgnored
            nstream.read(bytes1);                        
            buf1.put(bytes1);          
            buf1.rewind();             
            int j=0;
            for(int i = clippos[pos]; i < clippos[pos]+durtime*fs; i++){
            	buf1.order(ByteOrder.LITTLE_ENDIAN);      // 修改字节顺序,little_endian 小端（多字节值的字节顺序是从最低有效位到最高的）
                ndataL[j] = buf1.getShort() / 32768f;     // short --> float // 读取buf当前位置(开始位置)之后的两个字节，根据当前的字节顺序将它们组成 short值，然后将该位置增加 2。
                ndataR[j] = buf1.getShort() / 32768f;     // 32768：short的取值范围
                j++;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        int k=0;
        float[] data = new float[durtime*fs];
        for(int i = 0; i < dataL.length; i++){ 
            dataL[i] = dataL[i] + ndataL[i];      // // 傅里叶变换必须仅在一个通道上应用,取均值, stereo -> mono
            dataR[i] = dataR[i] + ndataR[i];   
        }
        for(int i = 0; i < dataL.length; i++){ 
            data[k++]=(dataL[i]+dataR[i])/2;  
        }
        System.out.println("---------data--start---------");
        for (float d:data){
        	System.out.print(d+",");
        }
        System.out.println("---------data---end--------");
        return data;
	}
}
