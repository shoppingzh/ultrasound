package com.littlezheng.ultrasound3.ultrasound.process;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;

import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.SampledData;
import com.littlezheng.ultrasound3.util.ObjectUtils;

import java.io.File;
import java.util.Observable;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class PlayProcessor extends Observable {

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
    private Thread currentWorker;

    public PlayProcessor(UContext uContext) {
        this.uContext = uContext;

        bImagePixels = uContext.getBImagePixels();
        clearBPixels = new int[bImagePixels.length];
    }

    /**
     * 回放
     */
    public void replay() {
        if (enabled) return;
        enabled = true;

        playVideo(null);
    }

    /**
     * 播放指定视频
     *
     * @param video
     */
    public void play(File video) {
        if (enabled) return;
        enabled = true;

        playVideo(video);
    }


    /**
     * 播放
     */
    private void playVideo(final File video) {
        currentWorker = new Thread(new Worker(video));
        currentWorker.start();
    }

    /**
     * 停止播放
     */
    public void stop() {
        enabled = false;
    }

    /**
     * 是否正在回放
     *
     * @return
     */
    public boolean inReplay() {
        return enabled;
    }

    private void loadBImageConfig() {
        secSamWid = SampledData.getSecondSampleWidth(depth);
        secSamHei = SampledData.getSecondSampleHeight(depth);
        positions = uContext.getSampledData().getPositions(depth);
        intervals = uContext.getSampledData().getIntervals(depth);
    }

    private class Worker implements Runnable {

        File video;

        public Worker(File video) {
            this.video = video;
        }

        @Override
        public void run() {
            //加载视频源
            ImageHolder imageHolder = null;
            if (video == null) {
                imageHolder = uContext.getImageHolder();
            } else {
                final Activity activity = (Activity) uContext.getAndroidContext();
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
            for (StorableFrame frame : imageHolder) {
                if (!enabled) break;
                if (frame != null) {
                    depth = frame.depth;
                    colors = frame.colors;
                    loadBImageConfig();

                    System.arraycopy(clearBPixels, 0, bImagePixels, 0, bImagePixels.length);
                    MainProcessor.thirdSample(bImagePixels, frame.data, secSamWid, secSamHei,
                            positions, intervals, colors);
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
        }
    }

}
