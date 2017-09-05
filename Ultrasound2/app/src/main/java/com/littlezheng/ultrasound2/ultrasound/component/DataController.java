package com.littlezheng.ultrasound2.ultrasound.component;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.littlezheng.ultrasound2.ultrasound.enums.Depth;
import com.littlezheng.ultrasound2.ultrasound.enums.Mode;
import com.littlezheng.ultrasound2.ultrasound.transmission.UdpTransmitter;
import com.littlezheng.ultrasound2.ultrasound.transmission.validate.UdpPacket406Validator;
import com.littlezheng.ultrasound2.ultrasound.transmission.validate.UdpPacketValidator;
import com.littlezheng.ultrasound2.ultrasound.util.DateUtils;
import com.littlezheng.ultrasound2.util.ObjectUtils;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class DataController extends Observable implements Observer{

    private static final String TAG = "DataController";

    private UContext uContext;
    private UdpTransmitter udpTransmitter;
    private UdpPacketValidator validator;

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

    private boolean enabled;
    private boolean bEnabled;
    private boolean mEnabled;

    //帧保持器
    private ImageHolder imageHolder;

    public DataController(UContext uContext, UdpTransmitter udpTransmitter){
        this.uContext = uContext;
        this.udpTransmitter = udpTransmitter;
        validator = new UdpPacket406Validator();
        colors = uContext.getColorController().getColors();
        bImagePixels = uContext.getBImagePixels();
        mImagePixels = uContext.getMImagePixels();
        clearBPixels = bImagePixels.clone();
        clearMPixels = mImagePixels.clone();

        loadMImageConfig();
        //深度感知、伪彩感知
        uContext.getDepth().addObserver(this);
        uContext.getColorController().addObserver(this);
    }

    private void loadBImageConfig() {
        depth = uContext.getDepth().getCurrValue();
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

    /**
     * 开启图像数据处理
     */
    public void enable() {
        if(enabled) return;
        enabled = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadBImageConfig();
                imageHolder = uContext.getImageHolder();
                try{
                    while(enabled){
                        byte[] data = udpTransmitter.receive();
                        if(!validator.validate(data)) continue;

                        int id = data[403] & 0xff;

                        //B图像生成
                        if(bEnabled){
                            int offset = zeros[id-1];
                            if(bFrame.full()){
                                System.arraycopy(clearBPixels, 0, bImagePixels, 0, bImagePixels.length);
                                byte[][] frameData = bFrame.getData();
                                thirdSample(frameData, secSamWid, secSamHei);
                                bFrame.clear();

                                byte[][] data2 = bFrame.getCloneData();
                                imageHolder.add(new StorableFrame(data2, depth, colors));

                                setChanged();
                                notifyObservers();
                            }else{
                                bFrame.put(id-1, data, 2, offset, SampledData.ORIGINAL_FRAME_HEIGHT);
                            }
                        }

                        //M图像生成
                        if(mEnabled){
                            mFrame.put(data, 2, 0, SampledData.ORIGINAL_FRAME_HEIGHT);
                            generateMImage();

                            setChanged();
                            notifyObservers();
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                    //TODO
                }
            }
        }).start();
    }

    /**
     * 关闭图像数据处理
     */
    public void disable(){
        enabled = false;
    }

    /**
     * 是否正在处理中
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isBEnabled() {
        return bEnabled;
    }

    public boolean isMEnabled() {
        return mEnabled;
    }

    /**
     * 切换模式，需要做的事情是对应不同的模式，开启不同的图像处理
     * @param mode
     */
    public void setMode(Mode mode){
        switch (mode){
            case MODE_B:
                bEnabled = true;
                mEnabled = false;
                break;
            case MODE_M:
                bEnabled = false;
                mEnabled = true;
                break;
            case MODE_BB:
                bEnabled = true;
                mEnabled = false;
                break;
            case MODE_BM:
                bEnabled = true;
                mEnabled = true;
                break;
        }
    }

    /**
     * 设置伪彩
     * @param color
     */
    public void setPseudoColor(ColorController.PseudoColor color){
        uContext.getColorController().change(color);
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

    /**
     * M图像生成
     */
    private void generateMImage() {
//        long s = System.currentTimeMillis();
        byte[][] data = mFrame.getData();
        int wid = mFrame.getWidth(),hei = mFrame.getHeight();
        int start = mFrame.getPos();
        int idx = 0;
        for(int i=0;i<hei;i++){
            for(int j=start;j<wid;j++){
                mImagePixels[idx++] = colors[data[j][i] & 0xff];
            }
            for(int j=0;j<start;j++){
                mImagePixels[idx++] = colors[data[j][i] & 0xff];
            }
        }
//        Log.d(TAG,"M模式一帧数据处理时间："+(System.currentTimeMillis()-s)+"ms");
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg != null){
            colors = uContext.getColorController().getColors();
            return;
        }
        loadBImageConfig();
    }

    /**
     * 保存当前视频
     */
    public void saveVideo() {
        if(enabled) return;
        File file = new File(Environment.getExternalStorageDirectory(),
                UContext.VIDEO_STORAGE_PATH + "/" + DateUtils.getDateTimeStr()+".ump4");
        ObjectUtils.saveObject(imageHolder, file);
    }

}
