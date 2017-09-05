package com.littlezheng.ultrasound.ultrasound.process;

import android.os.Environment;
import android.util.Log;

import com.littlezheng.ultrasound.ultrasound.Configuration;
import com.littlezheng.ultrasound.ultrasound.Depth;
import com.littlezheng.ultrasound.ultrasound.transfer.BufferedUdpReceiver;
import com.littlezheng.ultrasound.ultrasound.transfer.Param;
import com.littlezheng.ultrasound.ultrasound.transfer.validate.UdpPacketValidator;
import com.littlezheng.ultrasound.ultrasound.util.DateUtils;
import com.littlezheng.ultrasound.util.ObjectUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/8/14/014.
 */

public class ImageCreator extends Observable implements Observer{

    private static final String TAG = "ImageCreator";

    //全局B图像（以最大B图像为基准）
    public static final int[] bPixels =
            new int[Configuration.THIRD_SAMPLE_MAX_WIDTH*Configuration.THIRD_SAMPLE_MAX_HEIGHT];
    public static Depth depth = Depth.DEPTH_DEFAULT;

    //全局M图像
    public static final int[] mPixels =
            new int[500*Configuration.UDP_USEFUL_DATA_LEN];

    private Configuration conf;
    private BufferedUdpReceiver receiver;
    private UdpPacketValidator validator;

    private int idIdx = Configuration.UDP_RECEIVE_PACKET_SIZE - 3;
    private int usefulLen = Configuration.UDP_USEFUL_DATA_LEN;

    /**
     *  -----B图像相关-----
     *  zeroInsertions：插零数组
     *  positions：三次采样位置数组
     *  intervals：三次采样间隔数组
     *  frame：B模式的原始帧
     *  emptyBPixels：B模式的空图像
     *  secondSampleWid：二次采样帧宽度
     *  secondSampleWid：二次采样帧高度
     */
    private byte[] zeroInsertions;
    private int[][] positions;
    private int[][] intervals;
    private Frame bFrame;
    private int[] emptyBPixels = bPixels.clone();
    private int secondSampleWid;
    private int secondSampleHei;

    /**
     *  -----M图像相关-----
     *  mFrame：M模式的原始帧
     *  emptyMPixels：M模式的空图像
     */
    private MFrame mFrame = new MFrame(500,usefulLen,false);
    private int[] emptyMPixels = mPixels.clone();

    private boolean runState;
    private boolean bEnable;
    private boolean mEnable;

    //视频存储播放
    public static int currentFrame = 0;
    private ImageHolder imageHolder = new ImageHolder(100);
    private boolean videoState;

    public ImageCreator(Configuration conf,BufferedUdpReceiver receiver,
                        UdpPacketValidator validator){
        this.conf = conf;
        this.receiver = receiver;
        this.validator = validator;
    }

    public void start(){
        if(runState) return;
        runState = true;
        stopVideo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    loadDepthConfig();
                    while(runState){
                        byte[] data = receiver.receive();
                        if(!validator.validate(data)) continue;

                        int id = data[idIdx] & 0xff;
//                        Log.d(TAG,"id："+id);

                        //生成B图像
                        if(bEnable){
                            int offset = zeroInsertions[id-1];
                            if(bFrame.full()){
                                System.arraycopy(emptyBPixels,0,bPixels,0,bPixels.length);
                                byte[][] frameData = bFrame.getData();
                                Util.thirdSample(frameData,secondSampleWid,secondSampleHei,positions,intervals,bPixels);
                                bFrame.clear();
//                                bFrame = new BFrame(Configuration.SECOND_SAMPLE_WIDTH_BASE,
//                                        Configuration.SECOND_SAMPLE_MAX_HEIGHT,false);
                                byte[][] data2 = bFrame.getCloneData();
                                imageHolder.add(new StorableFrame(data2,depth));

                                setChanged();
                                notifyObservers();
                            }else{
                                bFrame.put(id-1,data,2,offset,usefulLen);
                            }
                        }

                        //生成M图像
                        if(mEnable){
//                            Log.d(TAG,"id："+id);
                            mFrame.put(data,2,0,usefulLen);
                            Util.generateMImage(mFrame,mPixels);

                            setChanged();
                            notifyObservers();
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop(){
        runState = false;
    }

    public void playVideo(){
        if(videoState) return;
        videoState = true;
        stop();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                for(StorableFrame frame : imageHolder){
                    if(frame != null){
                        currentFrame = ++count;
                        depth = frame.depth;
                        loadDepthConfig();

                        System.arraycopy(emptyBPixels,0,bPixels,0,bPixels.length);
                        Util.thirdSample(frame.data,secondSampleWid,secondSampleHei,positions,intervals,bPixels);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        setChanged();
                        notifyObservers();
                    }
                }
                stopVideo();
            }
        }).start();

    }

    public void playVideo(final File videoFile){
        if(videoState) return;
        videoState = true;
        stop();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                imageHolder = ObjectUtils.readObject(videoFile, ImageHolder.class);
                for(StorableFrame frame : imageHolder){
                    if(frame != null){
                        currentFrame = ++count;
                        depth = frame.depth;
                        loadDepthConfig();

                        System.arraycopy(emptyBPixels,0,bPixels,0,bPixels.length);
                        Util.thirdSample(frame.data,secondSampleWid,secondSampleHei,positions,intervals,bPixels);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        setChanged();
                        notifyObservers();
                    }
                }
                stopVideo();
            }
        }).start();

    }

    public void stopVideo(){
        videoState = false;
    }

    private void loadDepthConfig(){
        bFrame = new BFrame(Configuration.SECOND_SAMPLE_WIDTH_BASE,
                Configuration.SECOND_SAMPLE_MAX_HEIGHT,false);
        zeroInsertions = Configuration.getZeroInsertions(depth);
        positions = conf.getPositions(depth);
        intervals = conf.getIntervals(depth);
        secondSampleWid = Configuration.getSecondSampleWidth(depth);
        secondSampleHei = Configuration.getSecondSampleHeight(depth);
    }

    @Override
    public void update(Observable o, Object arg) {
        Param p = (Param) o;
        depth = Depth.getDepth(p.getCurrValue()*10+30);
        Log.d(TAG,"深度变化，当前深度："+depth.getValue());
        loadDepthConfig();
    }

    public boolean isbEnable() {
        return bEnable;
    }

    public void setbEnable(boolean bEnable) {
        this.bEnable = bEnable;
        receiver.clearBuffer();
    }

    public boolean ismEnable() {
        return mEnable;
    }

    public void setmEnable(boolean mEnable) {
        this.mEnable = mEnable;
        receiver.clearBuffer();
    }

    public void clearBImage(){
        System.arraycopy(emptyBPixels,0,bPixels,0,emptyBPixels.length);
    }

    public void clearMImage(){
        System.arraycopy(emptyMPixels,0,mPixels,0,emptyMPixels.length);
    }

    public void saveVideo() {
        if(runState) return;
        File file = new File(Environment.getExternalStorageDirectory(),
                Configuration.VIDEO_SAVE_FOLDER+"/"+ DateUtils.getDateTimeStr()+".ump4");
        boolean rst = ObjectUtils.saveObject(imageHolder, file);
        Log.d(TAG,"保存成功：" + rst);
    }

}
