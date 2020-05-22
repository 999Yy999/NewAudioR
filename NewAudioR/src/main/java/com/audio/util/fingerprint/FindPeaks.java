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
      for(int i = 0 ; i < nPeaks; i++){     //[0,3) 初始化
          power[i] = -500;
          locate[i] = -1;
      }

      float[] data_power = new float[len];   //11

      for(int i = 0; i < len; i ++){         //[0,11)
          data_power[i] = (float) (10 * Math.log10(data[2 * i] * data[2 * i]   //magnitude=pow(10,datapow/20)
                  + data[2 * i + 1] * data[2 * i + 1]));              //amplitude=magnitude/256
      }

      for(int k = 0; k < len; k ++){        //[0,11)  像是二维数组选择排序, 11个点   // 初筛最大值，和左右5个邻居比较  
          float pi = data_power[k];
          boolean add = true;
          for(int j = 0; j < neighborRange; j ++) {    //[0,5)   与附近5个点比较
              float pl,pr;
              if(k - j >= 0) {
                  pl = data_power[k - j];
              }else pl = pi - 1;

              if(k + j < len) {
                  pr = data_power[k + j];
              }else pr = pi - 1;
              
              if (pi < pl && pi < pr) {       //pi是极小值的话,可以break掉吧？
                  add = false;
              }
          }
          if(add) {add(pi, k);  /*System.out.println("pi="+pi+",k="+k);*/ }//选出11个峰值点
      }
  }

  private void add(float p, int loc){           //找出最大的三个点 直接插入排序？？？
      for(int i = 0; i < power.length; i++){
          if(power[i] < p){
              for(int j = power.length - 1; j > i; j --){  //像是在挪位置，pow内部是排好序的  降序排列
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
