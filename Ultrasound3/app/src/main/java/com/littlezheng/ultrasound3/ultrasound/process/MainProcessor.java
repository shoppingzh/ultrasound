package com.littlezheng.ultrasound3.ultrasound.process;

import android.os.Environment;
import android.util.Log;

import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Cal;
import com.littlezheng.ultrasound3.ultrasound.base.Colors;
import com.littlezheng.ultrasound3.ultrasound.base.ModeSwitcher;
import com.littlezheng.ultrasound3.ultrasound.base.SampledData;
import com.littlezheng.ultrasound3.ultrasound.transmission.UdpTransmitter;
import com.littlezheng.ultrasound3.ultrasound.transmission.validate.UdpPacket406Validator;
import com.littlezheng.ultrasound3.ultrasound.transmission.validate.UdpPacketValidator;
import com.littlezheng.ultrasound3.ultrasound.util.DateUtils;
import com.littlezheng.ultrasound3.util.ObjectUtils;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class MainProcessor
        extends Observable implements Observer {

    private final UContext uContext;
    private final UdpTransmitter udpTransmitter;
    private final UdpPacketValidator validator;

    //图像处理相关
    private int[] colors; //颜色数组

    private int[] bImagePixels; //B图像像素
    private int depth; //深度
    private Frame bFrame; //B图像帧
    private int[] clearBPixels; //用于清除B图像的空白帧
    private int secSamWid; //二次采样宽度
    private int secSamHei; //二次采样高度
    private byte[] zeros; //插零参数
    private int[][] positions; //三次采样位置参数
    private int[][] intervals; //三次采样间隔参数

    private int[] mImagePixels; //M图像像素
    private MFrame mFrame; //M图像帧
    private int[] clearMPixels; //用于清除M图像的空白帧

    //帧保持器
    private ImageHolder imageHolder;

    private boolean enabled;
    private boolean bEnabled;
    private boolean mEnabled;

    //是否计算背膘
    private byte[] lastBackFatData;

    public MainProcessor(UContext uContext) {
        this.uContext = uContext;
        udpTransmitter = uContext.getUdpTransmitter();
        validator = new UdpPacket406Validator();
        colors = uContext.getColors().get();
        bImagePixels = uContext.getBImagePixels();
        mImagePixels = uContext.getMImagePixels();
        clearBPixels = bImagePixels.clone();
        clearMPixels = mImagePixels.clone();

        loadMImageConfig();
        //深度感知、伪彩感知
        uContext.getParam(UContext.PARAM_DEPTH).addObserver(this);
        uContext.getColors().addObserver(this);
        //模式感知
        uContext.getModeSwitcher().addObserver(this);
    }

    /**
     * 开启处理
     */
    public void enable() {
        if (enabled) return;
        enabled = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadBImageConfig();
                imageHolder = uContext.getImageHolder();
                try {
                    while (enabled) {
                        byte[] data = udpTransmitter.receive();
                        if (!validator.validate(data)) continue;

                        int id = data[403] & 0xff;

                        if (id == 64) {
                            lastBackFatData = data;
                        }

                        //B图像生成
                        if (bEnabled) {
                            int offset = zeros[id - 1];
                            if (bFrame.full()) {
                                System.arraycopy(clearBPixels, 0, bImagePixels, 0, bImagePixels.length);
                                byte[][] frameData = bFrame.getData();
                                thirdSample(bImagePixels, frameData, secSamWid,
                                        secSamHei, positions, intervals, colors);
                                bFrame.clear();

                                byte[][] data2 = bFrame.getCloneData();
                                imageHolder.add(new StorableFrame(data2, depth, colors));

//                                int[] pixels = new int[bImagePixels.length];
//                                System.arraycopy(bImagePixels, 0, pixels, 0, bImagePixels.length);
                                setChanged();
                                notifyObservers();
//                                notifyObservers(pixels);
                            } else {
                                bFrame.put(id - 1, data, 2, offset, SampledData.ORIGINAL_FRAME_HEIGHT);
                            }
                        }

                        //M图像生成
                        if (mEnabled) {
                            mFrame.put(data, 2, 0, SampledData.ORIGINAL_FRAME_HEIGHT);
                            generateMImage(mImagePixels, mFrame, colors);

                            setChanged();
                            notifyObservers();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO
                }
            }
        }).start();
    }

    /**
     * 关闭处理
     */
    public void disable() {
        enabled = false;
    }

    /**
     * 开启/关闭B或M图像处理
     *
     * @param b
     * @param m
     */
    public void enableProcess(boolean b, boolean m) {
        bEnabled = b;
        mEnabled = m;
    }

    /**
     * 是否正在处理中
     *
     * @return
     */
    public boolean inProcess() {
        return enabled;
    }

    /**
     * 设置伪彩
     *
     * @param color
     */
    public void setPseudoColor(Colors.PseudoColor color) {
        uContext.getColors().change(color);
    }

    /**
     * 保存当前视频
     */
    public boolean saveVideo() {
        if (enabled) return false;
        File file = new File(Environment.getExternalStorageDirectory(),
                UContext.VIDEO_STORAGE_PATH + "/" + DateUtils.getDateTimeStr() + ".ump4");
        ObjectUtils.saveObject(imageHolder, file);
        return true;
    }

    /**
     * 计算背膘
     */
    public void calculateBackFat() {
        if (lastBackFatData != null) {
            float fat = Cal.calculateBackFat(lastBackFatData);
            setChanged();
            notifyObservers(fat);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        ModeSwitcher modeSwitcher = uContext.getModeSwitcher();
        if (o == modeSwitcher) {
            Log.d(TAG, "当前模式：" + modeSwitcher.getMode());
            switch (modeSwitcher.getMode()) {
                case MODE_B:
                    enableProcess(true, false);
                    break;
                case MODE_M:
                    enableProcess(false, true);
                    break;
                case MODE_BB:
                    enableProcess(true, false);
                    break;
            }
        }
        if (arg != null) {
            colors = uContext.getColors().get();
            return;
        }
        loadBImageConfig();
    }


    /**
     * 三次采样处理
     *
     * @param bImagePixels 存放处理结果的像素数组
     * @param origin       源数据
     * @param width        源数据帧宽度
     * @param height       源数据帧高度
     * @param positions    插值采样位置信息
     * @param intervals    插值间隔信息
     * @param colors       处理像素的颜色
     */
    public static void thirdSample(int[] bImagePixels,
                                   byte[][] origin,
                                   int width,
                                   int height,
                                   int[][] positions,
                                   int[][] intervals,
                                   int[] colors) {
//        long start = System.currentTimeMillis();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - 1; j++) {
                int curr = origin[j][i] & 0xff;
                int next = origin[j + 1][i] & 0xff;

                int interval = intervals[i][j];
                int pos = positions[i][j];

                int cIdx = i * SampledData.THIRD_SAMPLE_MAX_WIDTH + pos;
//                //如果取样间隔小于等于1，则直接放置数据即可
                if (interval <= 1) {
                    bImagePixels[cIdx] = colors[curr];
                    continue;
                }

                bImagePixels[cIdx++] = colors[curr];
//                //如果相邻两点值相同，则插值也相同，故不做运算
                if (curr == next) {
                    for (int k = interval - 1; k > 0; k--) {
                        bImagePixels[cIdx++] = colors[curr];
                    }
                } else {
                    for (int k = interval - 1; k > 0; k--) {
                        float fInterval = (float) interval;
                        float pow1 = k / fInterval, pow2 = (interval - k) / fInterval;
                        int value = (int) (curr * pow1 + next * pow2);
                        bImagePixels[cIdx++] = colors[value];
                    }
                }

            }
        }
//        Log.d(TAG,"一帧数据处理时间："+(System.currentTimeMillis()-start)+"ms");
    }


    /**
     * M图像生成算法
     *
     * @param mImagePixels 存放处理后的M图像像素数组
     * @param mFrame       M图像源数据帧
     * @param colors       处理像素的颜色
     */
    public static void generateMImage(int[] mImagePixels,
                                      MFrame mFrame,
                                      int[] colors) {
//        long s = System.currentTimeMillis();
        byte[][] data = mFrame.getData();
        int wid = mFrame.getWidth(), hei = mFrame.getHeight();
        int start = mFrame.getPos();
        int idx = 0;
        for (int i = 0; i < hei; i++) {
            for (int j = start; j < wid; j++) {
                mImagePixels[idx++] = colors[data[j][i] & 0xff];
            }
            for (int j = 0; j < start; j++) {
                mImagePixels[idx++] = colors[data[j][i] & 0xff];
            }
        }
//        Log.d(TAG,"M模式一帧数据处理时间："+(System.currentTimeMillis()-s)+"ms");
    }

    private void loadBImageConfig() {
        depth = uContext.getParam(UContext.PARAM_DEPTH).getCurrValue();
        bFrame = new BFrame(SampledData.SECOND_SAMPLE_WIDTH_BASE,
                SampledData.SECOND_SAMPLE_MAX_HEIGHT, false);
        secSamWid = SampledData.getSecondSampleWidth(depth);
        secSamHei = SampledData.getSecondSampleHeight(depth);
        zeros = SampledData.getZeroInsertions(depth);
        positions = uContext.getSampledData().getPositions(depth);
        intervals = uContext.getSampledData().getIntervals(depth);
    }

    private void loadMImageConfig() {
        mFrame = new MFrame(500, SampledData.ORIGINAL_FRAME_HEIGHT, false);
    }

}
