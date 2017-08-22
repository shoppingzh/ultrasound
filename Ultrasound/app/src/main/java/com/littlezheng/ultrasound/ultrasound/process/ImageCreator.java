package com.littlezheng.ultrasound.ultrasound.process;

import android.util.Log;

import com.littlezheng.ultrasound.ultrasound.Configuration;
import com.littlezheng.ultrasound.ultrasound.Depth;
import com.littlezheng.ultrasound.ultrasound.transfer.api.UdpReceiver;
import com.littlezheng.ultrasound.ultrasound.transfer.data.Param;
import com.littlezheng.ultrasound.ultrasound.transfer.validate.UdpPacketValidator;

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
    private UdpReceiver receiver;
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

    public ImageCreator(Configuration conf,UdpReceiver receiver,
                        UdpPacketValidator validator){
        this.conf = conf;
        this.receiver = receiver;
        this.validator = validator;
    }

    public void start(){
        if(runState) return;
        runState = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    loadDepthConfig();
                    while(runState){
                        byte[] data = receiver.receive();
                        if(!validator.validate(data)) continue;

                        int id = data[idIdx] & 0xff;

                        //生成B图像
                        if(bEnable){
                            int offset = zeroInsertions[id-1];
                            if(bFrame.full()){
                                System.arraycopy(emptyBPixels,0,bPixels,0,bPixels.length);
                                byte[][] frameData = bFrame.getData();
                                Util.thirdSample(frameData,secondSampleWid,secondSampleHei,positions,intervals,bPixels);
                                bFrame.clear();

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
    }

    public boolean ismEnable() {
        return mEnable;
    }

    public void setmEnable(boolean mEnable) {
        this.mEnable = mEnable;
    }

    public void clearBImage(){
        System.arraycopy(emptyBPixels,0,bPixels,0,emptyBPixels.length);
    }

    public void clearMImage(){
        System.arraycopy(emptyMPixels,0,mPixels,0,emptyMPixels.length);
    }
}
