package com.littlezheng.ultrasound.ultrasound.process;

import android.graphics.Color;

import com.littlezheng.ultrasound.ultrasound.Configuration;

/**
 * Created by Administrator on 2017/8/10/010.
 */

public class Util {

    private static final String TAG = "Util";

    public static final int PSEUDO_COLOR_NORMAL = 0;
    public static final int PSEUDO_COLOR_RED = 1;
    public static final int PSEUDO_COLOR_YELLOW = 2;
    public static final int PSEUDO_COLOR_MIX = 3;

    private static final int[] grayColors = new int[256];
    private static final int[] redColors = new int[256];
    private static final int[] yellowColors = new int[256];
    private static final int[] mixColors = new int[256];

    public static int[] currColors = grayColors;

    static {
        //初始化灰阶颜色数组
        for(int i=0;i<256;i++){
            grayColors[i] = Color.rgb(i, i, i);
            redColors[i] = Color.rgb(i, 0, 0);
            yellowColors[i] = Color.rgb(i, i, 0);
            if(i < 72){
                mixColors[i] = Color.rgb(i, i, 0);
            }else{
                mixColors[i] = Color.rgb(i, 0, 0);
            }
        }
    }

    public static void thirdSample(byte[][] origin, int width, int height,
                                   int[][] positions, int[][] intervals, int[] pixels){
//        long start = System.currentTimeMillis();
        for(int i=0;i<height;i++){
            for(int j=0;j<width-1;j++){
                int curr = origin[j][i] & 0xff;
                int next = origin[j+1][i] & 0xff;

                int interval = intervals[i][j];
                int pos = positions[i][j];

                int cIdx = i * Configuration.THIRD_SAMPLE_MAX_WIDTH + pos;
//                //如果取样间隔小于等于1，则直接放置数据即可
                if(interval <= 1){
                    pixels[cIdx] = currColors[curr];
                    continue;
                }

                pixels[cIdx++] = currColors[curr];
//                //如果相邻两点值相同，则插值也相同，故不做运算
                if(curr == next){
                    for(int k=interval-1;k>0;k--){
                        pixels[cIdx++] = currColors[curr];
                    }
                }else{
                    for(int k=interval-1;k>0;k--){
                        float fInterval = (float)interval;
                        float pow1 = k / fInterval,pow2 = (interval-k) / fInterval;
                        int value = (int) (curr * pow1 + next * pow2);
                        pixels[cIdx++] = currColors[value];
                    }
                }

            }
        }

//        Log.d(TAG,"一帧数据处理时间："+(System.currentTimeMillis()-start)+"ms");
    }

    public static void thirdSampleWithSmooth(byte[][] origin, int width, int height,
                                   int[][] positions, int[][] intervals, int[] pixels){
//        long start = System.currentTimeMillis();
        for(int i=0;i<height;i++){
            for(int j=0;j<width-1;j++){
                int curr = origin[j][i] & 0xff;
                int next = origin[j+1][i] & 0xff;
                int smoothCurr = (curr + next) / 2;

                int interval = intervals[i][j];
                int pos = positions[i][j];

                int cIdx = i * Configuration.THIRD_SAMPLE_MAX_WIDTH + pos;
//                //如果取样间隔小于等于1，则直接放置数据即可
                if(interval <= 1){
                    pixels[cIdx] = currColors[smoothCurr];
                    continue;
                }

                pixels[cIdx++] = currColors[smoothCurr];
//                //如果相邻两点值相同，则插值也相同，故不做运算
                if(curr == next){
                    for(int k=interval-1;k>0;k--){
                        pixels[cIdx++] = currColors[smoothCurr];
                    }
                }else{
                    for(int k=interval-1;k>0;k--){
                        float fInterval = (float)interval;
                        float pow1 = k / fInterval,pow2 = (interval-k) / fInterval;
                        int value = (int) (smoothCurr * pow1 + next * pow2);
                        pixels[cIdx++] = currColors[value];
                    }
                }

            }
        }

//        Log.d(TAG,"一帧数据处理时间："+(System.currentTimeMillis()-start)+"ms");
    }

    public static void generateMImage(MFrame frame, int[] pixels) {
//        long s = System.currentTimeMillis();
        byte[][] data = frame.getData();
        int wid = frame.getWidth(),hei = frame.getHeight();
        int start = frame.getPos();
        int idx = 0;
        for(int i=0;i<hei;i++){
            for(int j=start;j<wid;j++){
                pixels[idx++] = currColors[data[j][i] & 0xff];
            }
            for(int j=0;j<start;j++){
                pixels[idx++] = currColors[data[j][i] & 0xff];
            }
        }
//        Log.d(TAG,"M模式一帧数据处理时间："+(System.currentTimeMillis()-s)+"ms");
    }

    public static void setPseudoColor(int pseudoColor){
        switch (pseudoColor){
            case PSEUDO_COLOR_NORMAL:
                currColors = grayColors;
                break;
            case PSEUDO_COLOR_RED:
                currColors = redColors;
                break;
            case PSEUDO_COLOR_YELLOW:
                currColors = yellowColors;
                break;
            case PSEUDO_COLOR_MIX:
                currColors = mixColors;
                break;
            default:
                currColors = grayColors;
                break;
        }
    }


}
