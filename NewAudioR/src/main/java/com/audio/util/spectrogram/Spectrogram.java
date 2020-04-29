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
        stft = new ArrayList<>();              //频率是因为 窗口大小512，stft中的数组长度为512，  //两个数据即实部虚部组成一个整体 算出power[256], 频率也是两个一个整体freq[256] 
        freq = new float[windowSize / 2];      // 根据奈奎斯特采样频率,fft窗口大小N：512, 频率的可能范围是它的一半，后一半频率是不准确
        time = new float[dataLen / stepSize];  // 
        fft = new FloatFFT_1D(windowSize);     //* n=512
        fftData = new float[windowSize * 2];

        Window window = new Window(windowsType, windowSize);       //设置HANN窗口函数的值

        for(int i = 0; i < dataLen; ) {                // 为每一个滑动窗口进行加窗、补零、计算FFT、放入stft
            if(i + windowSize > data.length){          // 处理最后的一个窗口，80000/256=312...128
                break;
            }
            float[] win;
            try {
                win = window.window(data, i);          //已加窗
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            //calc fft
            calcFFT(win);   //补零+FFT(结果存在fftData中)
            //add
            addSTFT();      //计算STFT
            i += stepSize;
        }
        calcFreq(fs);     //计算频率
        calcTime(fs, stepSize);  //计算时间（帧？）
    }

    private void calcFFT(float[] win){
        for(int i = 0; i < win.length ; i ++){       //补零
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
