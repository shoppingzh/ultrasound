package com.littlezheng.ultrasound2.ultrasound.component;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.littlezheng.ultrasound2.activity.MainActivity;
import com.littlezheng.ultrasound2.ultrasound.display.image.Image;
import com.littlezheng.ultrasound2.util.ObjectUtils;

import java.io.File;
import java.util.Observable;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class ReplayController extends Observable {

    private UContext uContext;

    private int[] colors;
    private int[] bImagePixels; //B图像像素
    private int depth; //深度
    private int[] clearBPixels; //用于清除B图像的空白帧
    private int secSamWid; //二次采样宽度
    private int secSamHei; //二次采样高度
    private int[][] positions; //三次采样位置参数
    private int[][] intervals; //三次采样间隔参数

    private boolean enabled;

    public ReplayController(UContext uContext){
        this.uContext = uContext;

        bImagePixels = uContext.getBImagePixels();
        clearBPixels = new int[bImagePixels.length];
    }

    /**
     * 回放
     */
    public void replay(){
        if(enabled) return;
        enabled = true;

        play(null);
    }

    /**
     * 播放指定视频
     * @param video
     */
    public void replay(File video) {
        if(enabled) return;
        enabled = true;

        play(video);
    }

    /**
     * 播放
     */
    private void play(final File video){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //加载视频源
                ImageHolder imageHolder = null;
                if(video == null){
                    imageHolder = uContext.getImageHolder();
                }else{
                    final Activity activity = (Activity) uContext.getContext();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(activity, "正在加载...", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    });
                    imageHolder = ObjectUtils.readObject(video, ImageHolder.class);
                }

                int count = 0;
                for(StorableFrame frame : imageHolder){
                    if(frame != null){
                        depth = frame.depth;
                        colors = frame.colors;
                        loadBImageConfig();

                        System.arraycopy(clearBPixels, 0, bImagePixels, 0, bImagePixels.length);
                        thirdSample(frame.data, secSamWid, secSamHei);
                        try {
                            Thread.sleep(70);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        setChanged();
                        notifyObservers(depth);
                    }
                }
                enabled = false;
                ((Activity)uContext.getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast t = Toast.makeText(uContext.getContext(), "播放完毕！", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        t.show();
                    }
                });
            }
        }).start();
    }

    /**
     * 停止播放
     */
    public void stop(){
        enabled = false;
    }

    private void loadBImageConfig() {
        secSamWid = SampledData.getSecondSampleWidth(depth);
        secSamHei = SampledData.getSecondSampleHeight(depth);
        positions = uContext.getSampledData().getPositions(depth);
        intervals = uContext.getSampledData().getIntervals(depth);
    }

    /**
     * 三次采样处理
     * @param origin 原始帧数据
     * @param width 原始帧宽度
     * @param height 原始帧高度
     */
    private void thirdSample(byte[][] origin, int width, int height) {
//        long start = System.currentTimeMillis();
        for(int i=0;i<height;i++){
            for(int j=0;j<width-1;j++){
                int curr = origin[j][i] & 0xff;
                int next = origin[j+1][i] & 0xff;

                int interval = intervals[i][j];
                int pos = positions[i][j];

                int cIdx = i * SampledData.THIRD_SAMPLE_MAX_WIDTH + pos;
//                //如果取样间隔小于等于1，则直接放置数据即可
                if(interval <= 1){
                    bImagePixels[cIdx] = colors[curr];
                    continue;
                }

                bImagePixels[cIdx++] = colors[curr];
//                //如果相邻两点值相同，则插值也相同，故不做运算
                if(curr == next){
                    for(int k=interval-1;k>0;k--){
                        bImagePixels[cIdx++] = colors[curr];
                    }
                }else{
                    for(int k=interval-1;k>0;k--){
                        float fInterval = (float)interval;
                        float pow1 = k / fInterval,pow2 = (interval-k) / fInterval;
                        int value = (int) (curr * pow1 + next * pow2);
                        bImagePixels[cIdx++] = colors[value];
                    }
                }

            }
        }

//        Log.d(TAG,"一帧数据处理时间："+(System.currentTimeMillis()-start)+"ms");
    }


}
