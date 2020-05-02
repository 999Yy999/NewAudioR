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
        int[] clippos=new int[5];
        clippos[0]=0+fs; clippos[4]=len-fs*20; 
        clippos[2]=len/2; clippos[1]=(clippos[3]+clippos[0])/2;
        clippos[3]=(clippos[2]+clippos[4])/2;
        
        System.out.println("len="+len+",available="+stream.available());   //len/sample_rate = ʱ������λ���룩
        
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
            
            for(int i = clippos[pos]; i < clippos[pos]+durtime*fs; i++){
            	buf.order(ByteOrder.LITTLE_ENDIAN);      // �޸��ֽ�˳��,little_endian С�ˣ����ֽ�ֵ���ֽ�˳���Ǵ������Чλ����ߵģ�
                dataL[i] = buf.getShort() / 32768f;      // short --> float // ��ȡbuf��ǰλ��(��ʼλ��)֮��������ֽڣ����ݵ�ǰ���ֽ�˳��������� shortֵ��Ȼ�󽫸�λ������ 2��
                dataR[i] = buf.getShort() / 32768f;      // 32768��short��ȡֵ��Χ
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
            buf1.put(bytes1);           // ��������Դ bytes������������ݴ��䵽������buf��
            buf1.rewind();             // �������������ص���ʼλ�ã�
            
            for(int i = clippos[pos]; i < clippos[pos]+durtime*fs; i++){
            	buf1.order(ByteOrder.LITTLE_ENDIAN);      // �޸��ֽ�˳��,little_endian С�ˣ����ֽ�ֵ���ֽ�˳���Ǵ������Чλ����ߵģ�
                ndataL[i] = buf1.getShort() / 32768f;      // short --> float // ��ȡbuf��ǰλ��(��ʼλ��)֮��������ֽڣ����ݵ�ǰ���ֽ�˳��������� shortֵ��Ȼ�󽫸�λ������ 2��
                ndataR[i] = buf1.getShort() / 32768f;      // 32768��short��ȡֵ��Χ
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        int k=0;
        float[] data = new float[durtime*fs];
        for(int i = clippos[pos]; i < clippos[pos]+durtime*fs; i++){ 
            dataL[i] = dataL[i] + ndataL[i];      // // ����Ҷ�任�������һ��ͨ����Ӧ��,ȡ��ֵ, stereo -> mono
            dataR[i] = dataR[i] + ndataR[i];   
        }
        for(int i = clippos[pos]; i < clippos[pos]+durtime*fs; i++){ 
            data[k++]=(dataL[i]+dataR[i])/2;  
        }
        
        return data;
	}
}
