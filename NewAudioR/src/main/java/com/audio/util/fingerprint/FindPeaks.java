package com.audio.util.fingerprint;

//Find local peaks in fft data.

@SuppressWarnings("WeakerAccess")
public class FindPeaks {
  final float[] power;
  final int[] locate;

  private final int nPeaks;


  public FindPeaks(int nPeaks){
      super();
      this.nPeaks = nPeaks;

      power = new float[nPeaks];
      locate = new int[nPeaks];
  }

  public void findComplexPeaks(float[] data, int neighborRange){
      int len = data.length / 2;            //22/2=11
      for(int i = 0 ; i < nPeaks; i++){     //[0,3) ��ʼ��
          power[i] = -500;
          locate[i] = -1;
      }

      float[] data_power = new float[len];   //11

      for(int i = 0; i < len; i ++){         //[0,11)
          data_power[i] = (float) (10 * Math.log10(data[2 * i] * data[2 * i]   //magnitude=pow(10,datapow/20)
                  + data[2 * i + 1] * data[2 * i + 1]));              //amplitude=magnitude/256
      }

      for(int k = 0; k < len; k ++){        //[0,11)  ���Ƕ�ά����ѡ������, 11����   // ��ɸ���ֵ��������5���ھӱȽ� 
          float pi = data_power[k];
          boolean add = true;
          for(int j = 0; j < neighborRange; j ++) {    //[0,5)   �븽��5����Ƚ�
              float pl,pr;
              if(k - j >= 0) {
                  pl = data_power[k - j];
              }else pl = pi - 1;

              if(k + j < len) {
                  pr = data_power[k + j];
              }else pr = pi - 1;
              
              if (pi < pl && pi < pr) {       //pi�Ǽ�Сֵ�Ļ�,����break���ɣ�
                  add = false;
              }
          }
          if(add) {add(pi, k);  /*System.out.println("pi="+pi+",k="+k);*/ }//ѡ��11����ֵ��
      }
  }

  private void add(float p, int loc){           //�ҳ����������� ֱ�Ӳ������򣿣���
      for(int i = 0; i < power.length; i++){
          if(power[i] < p){
              for(int j = power.length - 1; j > i; j --){  //������Ųλ�ã�pow�ڲ����ź����  ��������
                  power[j] = power[j-1];
                  locate[j] = locate[j-1];
              }
              power[i] = p;
              locate[i] = loc;
              break;
          }
      }
  }
/*
  private int inBand(float freq, float[] freqRange){
      int size = freqRange.length;
      if(freq < freqRange[0] || freq > freqRange[size - 1]) {
          return -1;
      }
      for(int i = 0; i < size - 1; i ++){
          if(freqRange[i + 1] > freq)
              return i;
      }
      return -1;
  }
*/
}
