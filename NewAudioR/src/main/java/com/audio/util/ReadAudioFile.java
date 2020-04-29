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
            stream = AudioSystem.getAudioInputStream(file);  // ���ṩ�� File �����Ƶ������
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        AudioFormat format = stream.getFormat();        // ��� ����Ƶ���������������ݵ�  ��Ƶ��ʽ

        if(format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED){
            throw new Exception("Encoding must be PCM_SIGNED!");
        }
        if(format.getSampleRate() != 8000){         // ��ȡ��������,��ÿ��������
            throw new Exception("SampleRate must be 8000!");
        }
        
        int len = (int) stream.getFrameLength();   // ������ĳ��ȣ���ʾ��֡Ϊ��λ  sample_rate: һ�����8000��֡
        System.out.println("len="+len+",available="+stream.available());   //len/sample_rate = ʱ������λ���룩
         
        float[] dataL = new float[len];    // ������������
        float[] dataR = new float[len];

        
        ByteBuffer buf = ByteBuffer.allocate(4 * len);   // �ֽڻ���  4*len???    float����4byte������      ans:һ֡=4�ֽ�
        byte[] bytes = new byte[4 * len];
        try {
            //noinspection ResultOfMethodCallIgnored
            stream.read(bytes);                        // ��ȡһ���������ֽ�,����洢�� bytes��
            buf.put(bytes);           // ��������Դ bytes������������ݴ��䵽������buf��
            buf.rewind();             // �������������ص���ʼλ�ã�
            
            for(int i = 0; i < len; i++){
            	buf.order(ByteOrder.LITTLE_ENDIAN);      // �޸��ֽ�˳��,little_endian С�ˣ����ֽ�ֵ���ֽ�˳���Ǵ������Чλ����ߵģ�
                dataL[i] = buf.getShort() / 32768f;      // short --> float // ��ȡbuf��ǰλ��(��ʼλ��)֮��������ֽڣ����ݵ�ǰ���ֽ�˳��������� shortֵ��Ȼ�󽫸�λ������ 2��
                dataR[i] = buf.getShort() / 32768f;      // 32768��short��ȡֵ��Χ
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        float[] data = new float[len];
        for(int i = 0; i < len; i++){ 
            data[i] = dataL[i] + dataR[i];      // // ����Ҷ�任�������һ��ͨ����Ӧ��,ȡ��ֵ, stereo -> mono
            data[i] /= 2;
        }
        
        getTabs(file.getName());
        return data;
        //int fs = 8000;
        //fingerprint = new Fingerprint(data, fs);
    }
	    
}
