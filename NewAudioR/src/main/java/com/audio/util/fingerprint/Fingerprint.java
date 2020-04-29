package com.audio.util.fingerprint;


import com.audio.util.spectrogram.Spectrogram;
import com.audio.util.spectrogram.Window;

import java.util.ArrayList;

/**
 * Created by hsyecheng on 2015/6/11.
 */
@SuppressWarnings("FieldCanBeLocal")
public class Fingerprint {
    private final int NPeaks = 3;       // 一个周期（窗口中？？）中每个子带的峰值点的个数
    private final int fftSize = 512;    // FFT的窗口大小N
    private final int overlap = 256;    // FFT的窗口重叠大小
    private final int C = 32;           // 一个周期包含多少个窗口？
    private final int peakRange = 5;    // 取峰值点时与多大范围的邻居比较

    private final ArrayList<Peak> peakList = new ArrayList<>();
    private final ArrayList<Link> linkList = new ArrayList<>();
    private final float[] freq;
    private final float[] time;

    private final float[] range_time = {1f, 3f};       // 取点对的时候的时间范围，单位为秒
    private final float[] range_freq = {-600f, 600f};  // 取点对的时候的频率范围，单位为频率
    //private final float[] melBand = MelFreq.MelBand(new float[] {150f, 550f, 950f, 1350f, 1750f});
    private final int[] Band = {11,22,35,50,69,91,117,149,187,231};  // 分成的子带，值对应FFT产生的数组索引

    private final float minFreq = 100;   // 最小频率
    private final float maxFreq = 2000;  // 最大频率
    private final float minPower = 0;    // 最小能量

    public Fingerprint(float[] data, float fs) {
        super();
        Spectrogram spectrogram = new Spectrogram(data, Window.HANN, fftSize, overlap, fs);
        ArrayList<float[]> stft = spectrogram.stft;
        freq = spectrogram.freq;
        time = spectrogram.time;
        
        ArrayList<Peak> tmp = new ArrayList<>(C * NPeaks);  //32*3 //一个周期内一个子带上的峰值点个数
        int size = stft.size();                 // 124
        int bandNum = Band.length - 1;          // 10-1?
        for (int b = 0; b < bandNum; b++) {     //[0,9) 纵向遍历每个子带
            for (int i = 0; i < size; i++) {    //[0,124) 横向遍历每一个窗口
                if (i != 0) {                   // 非第0个窗口  
                    if (i % C == 0 || i == size - 1) {  //一个周期开始的一个窗口(除开第0个窗口) or 最后一个窗口    //过滤集合元素，一个b走4遍此if
                    	//Filter
                        tmp.removeIf(peak -> {
                            float peakFreq = freq[peak.intFreq];
                            return peakFreq < minFreq || peakFreq > maxFreq;
                        });
                        tmp.removeIf(peak -> peak.power <= minPower);
                        
                        tmp.sort((o1, o2) ->
                                        Double.compare(o2.power, o1.power)       //按能量值降序排序
                        );
                        int end = tmp.size() < NPeaks ? tmp.size() : NPeaks;    //针对最后一个窗口
                        peakList.addAll(tmp.subList(0, end));   //[0,end)       //一个周期只取最多三个峰值点  3*9*4
                        
                        tmp.clear();
                    }
                }
                
                float[] fft = stft.get(i);          //stft[0]-->float[512]
                int len = Band[b + 1] - Band[b];    //22-11
                int start = Band[b] * 2;            //11*2
                len *= 2;                           //len=11*2
                float[] fft_band = new float[len];
                System.arraycopy(fft, start, fft_band, 0, len);  //src:fft[22~43]  dest:fft_band[0,21]
                FindPeaks find = new FindPeaks(NPeaks);
                find.findComplexPeaks(fft_band, peakRange);
                float[] power = find.power;
                int[] loc = find.locate;

                for (int j = 0; j < power.length; j++) {
                    loc[j] += Band[b];
                }

                for (int j = 0; j < NPeaks; j++) {
                    if (loc[j] == -1) {
                        continue;
                    }
                    Peak p = new Peak();
                    p.intFreq = loc[j];
                    p.intTime = i;
                    p.power = power[j];

                    tmp.add(p);
                }

                //if (true) {
                /*tmp.sort((o1, o2) ->
                                Double.compare(o2.power, o1.power)
                );
                int end = tmp.size() < NPeaks ? tmp.size() : NPeaks;
                peakList.addAll(tmp.subList(0, end));
                tmp.clear();*/
                //}
            }
        }
        peakList.sort((o1, o2) -> o1.intTime - o2.intTime);  //按时间（窗口号）升序排列
        link(true);
    }

    private int inBand(int intFreq){
        int size = Band.length;                        //10
        if(intFreq < Band[0] || intFreq > Band[size - 1]) {   //是否在[11,231]之间
            return -1;
        }
        for(int i = 0; i < size - 1; i ++){       //判断在哪个子带里
            if(Band[i + 1] > intFreq)
                return i;                          //1
        }
        return -1;
    }

    private void link(@SuppressWarnings("SameParameterValue") boolean band) {
        int n = peakList.size();         //51
        for (int i = 0; i < n; i++) {    //[0,51)
            Peak p1 = peakList.get(i);   
            if (p1 == null) {
                continue;
            }

            //time start|end
            int tStart;
            int tEnd;
            int k;
            for (k = i + 1; k < n; k++) {        //[i+1,51) , 找到一个比p1的时长多1s以上的点
                float t = time[p1.intTime];
                float t2 = time[peakList.get(k).intTime];
                if (t2 - t >= range_time[0])
                    break;
            }                                    //k=10
            tStart = k;               //10
            for (; k < n; k++) {                 //[10,51) , 找到一个比p1的时长多3s以上的点
                float t = time[p1.intTime];
                float t2 = time[peakList.get(k).intTime];
                if (t2 - t >= range_time[1])
                    break;           
            } 
            tEnd = k;                 //34
            
            //freq start|end
            float fstart, fend;                          //f[56.25,1256.25]
            fstart = freq[p1.intFreq] + range_freq[0];   //656.25-600=56.25
            fend = freq[p1.intFreq] + range_freq[1];     //656.25+600=1256.25

            for (int i2 = tStart; i2 < tEnd; i2++) {     //t[10,34)
                Peak p2 = peakList.get(i2);
                if (p2 == null) {
                    continue;
                }

                if (band) {
                    int b1 = inBand(p1.intFreq);        //k=11 b1=1
                    int b2 = inBand(p2.intFreq);        //k=12 b2=3

                    //TODO
                    if(b1 == b2 && b1 != -1){           //要求目标区域的点和锚点在同一个子带内
                        Link l = new Link(p1, p2);
                        linkList.add(l);
                     }
                } else {       //？？？
                    if (freq[p2.intFreq] >= fstart && freq[p2.intFreq] <= fend) {
                        Link l = new Link(p1, p2);
                        linkList.add(l);
                    }
                }
            }
        }
    }

    public ArrayList<Link> getLinkList() {
        return linkList;
    }

    @SuppressWarnings("unused")
    public ArrayList<Peak> getPeakList() {
        return peakList;
    }

    public static class Peak {     //峰值点
        public int intFreq;        //坐标
        public float power;
        public int intTime;
    }

    public static class Link {           //点对
        public final Peak start;         //时间靠前的点start(t1,f1)
        public final Peak end;           //时间靠后的点end(t2,f2)

        final float[] tmp = new float[3];  //指纹

        public Link(Peak s, Peak e) {
            super();
            this.start = s;
            this.end = e;
            tmp[0] = s.intFreq;              //f1
            tmp[1] = e.intFreq;              //f2
            tmp[2] = e.intTime - s.intTime;  //delt t=t2-t1
        }
    }
}


