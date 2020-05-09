package com.audio.util;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class ClipAudioFile {
	public float[] clipFile(String filename, int durtime, int pos, int fre) throws Exception{
		int fs=8000;
		AudioInputStream stream;
		AudioInputStream nstream;
		String nfilename="D:\\Z_����\\��Ƶ�ز�\\whitenoise\\"+"white"+fre+".wav";
		File file=new File(filename);
		File nfile=new File(nfilename);
		
        try {
            stream = AudioSystem.getAudioInputStream(file);  // ���ṩ�� File �����Ƶ������
            nstream = AudioSystem.getAudioInputStream(nfile);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        AudioFormat format = stream.getFormat();        // ��� ����Ƶ���������������ݵ�  ��Ƶ��ʽ
        AudioFormat nformat = nstream.getFormat();
        
        if(format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED){
            throw new Exception("Encoding must be PCM_SIGNED!");
        }
        if(format.getSampleRate() != 8000){         // ��ȡ��������,��ÿ��������
            throw new Exception("SampleRate must be 8000!");
        }
        if(nformat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED){
            throw new Exception("nEncoding must be PCM_SIGNED!");
        }
        if(nformat.getSampleRate() != 8000){         // ��ȡ��������,��ÿ��������
            throw new Exception("nSampleRate must be 8000!");
        }
        
        int len = (int) stream.getFrameLength();   // ������ĳ��ȣ���ʾ��֡Ϊ��λ  sample_rate: һ�����8000��֡
        int nlen= (int) nstream.getFrameLength();
        int[] clippos=new int[6];
        clippos[1]=0+fs; clippos[5]=len-fs*20; 
        clippos[3]=len/2; clippos[2]=(clippos[3]+clippos[1])/2;
        clippos[4]=(clippos[3]+clippos[5])/2;
        System.out.println("--------pos:"+pos+",pos1:"+ clippos[1]+",pos2:"+clippos[2]+",pos3:"+clippos[3]+",pos4:"+clippos[4]+",pos5:"+clippos[5]);
        
        System.out.println("sample:len="+len+",available="+stream.available());   //len/sample_rate = ʱ������λ���룩
        System.out.println("noise:len="+nlen+",available="+nstream.available());
        
        float[] dataL = new float[durtime*fs];    // ������������
        float[] dataR = new float[durtime*fs];
        
        float[] ndataL = new float[durtime*fs];    // ������������
        float[] ndataR = new float[durtime*fs];
        
        ByteBuffer buf = ByteBuffer.allocate(4 * durtime*fs);   // �ֽڻ���  4*len???    float����4byte������      ans:һ֡=4�ֽ�
        byte[] bytes = new byte[4 * durtime*fs];
        try {
            //noinspection ResultOfMethodCallIgnored
            stream.read(bytes);                        // ��ȡһ���������ֽ�,����洢�� bytes��
            buf.put(bytes);           // ��������Դ bytes������������ݴ��䵽������buf��
            buf.rewind();             // �������������ص���ʼλ�ã�
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
        
        ByteBuffer buf1 = ByteBuffer.allocate(4 * durtime*fs);   // �ֽڻ���  4*len???    float����4byte������      ans:һ֡=4�ֽ�
        byte[] bytes1 = new byte[4 * durtime*fs];
        try {
            //noinspection ResultOfMethodCallIgnored
            nstream.read(bytes1);                        // ��ȡһ���������ֽ�,����洢�� bytes��
            buf1.put(bytes1);          // ��������Դ bytes������������ݴ��䵽������buf��
            buf1.rewind();             // �������������ص���ʼλ�ã�
            int j=0;
            for(int i = clippos[pos]; i < clippos[pos]+durtime*fs; i++){
            	buf1.order(ByteOrder.LITTLE_ENDIAN);      // �޸��ֽ�˳��,little_endian С�ˣ����ֽ�ֵ���ֽ�˳���Ǵ������Чλ����ߵģ�
                ndataL[j] = buf1.getShort() / 32768f;      // short --> float // ��ȡbuf��ǰλ��(��ʼλ��)֮��������ֽڣ����ݵ�ǰ���ֽ�˳��������� shortֵ��Ȼ�󽫸�λ������ 2��
                ndataR[j] = buf1.getShort() / 32768f;      // 32768��short��ȡֵ��Χ
                j++;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        int k=0;
        float[] data = new float[durtime*fs];
        for(int i = 0; i < dataL.length; i++){ 
            dataL[i] = dataL[i] + ndataL[i];      // // ����Ҷ�任�������һ��ͨ����Ӧ��,ȡ��ֵ, stereo -> mono
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
