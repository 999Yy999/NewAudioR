package com.audio.util.spectrogram;

import org.jtransforms.fft.FloatFFT_1D;

import java.util.ArrayList;

public class Spectrogram {
    public final ArrayList<float[]> stft;
    public final float[] freq;
    public final float[] time;

    private final FloatFFT_1D fft;
    private final float[] fftData;
    public Spectrogram(float[] data, @SuppressWarnings("SameParameterValue") int windowsType, int windowSize, int overlap, float fs) {
        super();
        int dataLen = data.length;
        int stepSize = windowSize - overlap;   //hopsize
        stft = new ArrayList<>();              //Ƶ������Ϊ ���ڴ�С512��stft�е����鳤��Ϊ512��  //�������ݼ�ʵ���鲿���һ������ ���power[256], Ƶ��Ҳ������һ������freq[256] 
        freq = new float[windowSize / 2];      // �����ο�˹�ز���Ƶ��,fft���ڴ�СN��512, Ƶ�ʵĿ��ܷ�Χ������һ�룬��һ��Ƶ���ǲ�׼ȷ
        time = new float[dataLen / stepSize];  // 
        fft = new FloatFFT_1D(windowSize);     //* n=512
        fftData = new float[windowSize * 2];

        Window window = new Window(windowsType, windowSize);       //����HANN���ں�����ֵ

        for(int i = 0; i < dataLen; ) {                // Ϊÿһ���������ڽ��мӴ������㡢����FFT������stft
            if(i + windowSize > data.length){          // ��������һ�����ڣ�80000/256=312...128
                break;
            }
            float[] win;
            try {
                win = window.window(data, i);          //�ѼӴ�
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            //calc fft
            calcFFT(win);   //����+FFT(�������fftData��)
            //add
            addSTFT();      //����STFT
            i += stepSize;
        }
        calcFreq(fs);     //����Ƶ��
        calcTime(fs, stepSize);  //����ʱ�䣨֡����
    }

    private void calcFFT(float[] win){
        for(int i = 0; i < win.length ; i ++){       //����
            fftData[i * 2] = win[i];
            fftData[i * 2 + 1] = 0;
        }
        
        fft.complexForward(fftData);     //*Computes 1D forward DFT of complex data leaving the result in fftData.
    }

    private void addSTFT(){
        float[] half = new float[fftData.length / 2];   // 512
        System.arraycopy(fftData,0,half,0,fftData.length / 2); // des:half[0,511]
        stft.add(half);                      // src:fftData[0]-fftData[511]   
    }

    private void calcFreq(float fs){
        for(int i = 0; i < freq.length; i++){
            freq[i] = fs * i / freq.length;
        }
    }

    private void calcTime(float fs, int stepSize){
        for(int i = 0; i < time.length; i++) {
            time[i] = stepSize * i / fs;
        }
    }
}
