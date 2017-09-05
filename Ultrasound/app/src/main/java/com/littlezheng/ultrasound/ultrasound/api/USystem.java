package com.littlezheng.ultrasound.ultrasound.api;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;

import com.littlezheng.ultrasound.adapter.Snapshot;
import com.littlezheng.ultrasound.task.ConfigTask;
import com.littlezheng.ultrasound.ultrasound.Configuration;
import com.littlezheng.ultrasound.ultrasound.process.ImageCreator;
import com.littlezheng.ultrasound.ultrasound.transfer.AsynUdpSender;
import com.littlezheng.ultrasound.ultrasound.transfer.BufferedUdpReceiver;
import com.littlezheng.ultrasound.ultrasound.transfer.ModeController;
import com.littlezheng.ultrasound.ultrasound.transfer.Param;
import com.littlezheng.ultrasound.ultrasound.transfer.RunController;
import com.littlezheng.ultrasound.ultrasound.transfer.validate.UdpPacket406Validator;
import com.littlezheng.ultrasound.ultrasound.transfer.validate.UdpPacketValidator;
import com.littlezheng.ultrasound.ultrasound.view.DisplayView;
import com.littlezheng.ultrasound.ultrasound.view.measure.ShapeMaker;
import com.littlezheng.ultrasound.ultrasound.view.strategy.ImageShowStrategy;
import com.littlezheng.ultrasound.ultrasound.view.strategy.NonDisplayStrategy;
import com.littlezheng.ultrasound.ultrasound.view.strategy.TimeStrategy;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.BBModeStrategyDecorator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.BMModeStrategyDecorator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.BModeStrategyDecorator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.ColorBarStrategyDecorator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.MModeStrategyDecorator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.MeasureStrategyDecorator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.ParamsStrategyDecorator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.PlayVideoStrategyDecorator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.decorator.RunStateStrategyDecorator;

import java.io.File;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2017/8/25/025.
 */

public class USystem {

    private Context mContext;

    //主要配置
    private Configuration conf;

    //工作线程：接收器、发送器、处理器
    private BufferedUdpReceiver receiver;
    private AsynUdpSender sender;
    private ImageCreator imageCreator;

    //运行控制器
    private RunController runController;
    //可调参数
    private Param contrast;
    private Param lightness;
    private Param gain;
    private Param nearGain;
    private Param farGain;
    private Param depth;
    private Param mModeSpeed;
    //模式控制器
    private ModeController modeController;

    //几个基本绘制策略：时间、参数、运行状态、颜色条
    private NonDisplayStrategy nonDisplayStrategy;
    private TimeStrategy timeStrategy;
    private ParamsStrategyDecorator paramsStrategyDecorator;
    private RunStateStrategyDecorator runStateStrategyDecorator;
    private ColorBarStrategyDecorator colorBarStrategyDecorator;
    //工作模式的绘制策略
    private BModeStrategyDecorator bModeStrategyDecorator;
    private MModeStrategyDecorator mModeStrategyDecorator;
    private BBModeStrategyDecorator bbModeStrategyDecorator;
    private BMModeStrategyDecorator bmModeStrategyDecorator;
    //其他策略
    private ImageShowStrategy imageShowStrategy;
    private MeasureStrategyDecorator measureStrategyDecorator;
    private PlayVideoStrategyDecorator playVideoStrategyDecorator;

    //显示视图
    private ViewGroup mViewContainer;
    private DisplayView displayView;


    ///////////当前系统运行状态信息
    private int workMode = UContext.MODE_B;

    public USystem(Context context, ViewGroup viewContainer){
        mContext = context;
        mViewContainer = viewContainer;
        initMainConfig();
        initDataReturner();
        initWorkThread();
        initDisplayStrategy();
        initDisplayView();
    }

    /**加载主要配置*/
    private void initMainConfig() {
        conf = Configuration.getInstance(mContext);
        new ConfigTask(mContext).execute(conf);
    }

    /**初始化回传数据*/
    private void initDataReturner() {
        //回传参数配置
        runController = new RunController(0xF0);
        contrast = new Param(0xFA,"对比度",32,48,16,1);
        lightness = new Param(0xFB,"亮度",32,46,20,1);
        gain = new Param(0xFC,"总增",16,32,1,1);
        nearGain = new Param(0xFD,"近增",16,32,1,1);
        farGain = new Param(0xFE,"远增",16,32,1,1);
        depth = new Param(0xF9,"深度",13,19,0,1);
        mModeSpeed = new Param(0xF3,"速度",3,3,0,1);
        modeController = new ModeController(0xF2);
    }

    /**初始化工作线程*/
    private void initWorkThread() {
        try {
            receiver = new BufferedUdpReceiver(Configuration.UDP_RECEIVE_PORT,
                    Configuration.UDP_RECEIVE_PACKET_QUEUE_CAPACITY,
                    Configuration.UDP_RECEIVE_PACKET_SIZE);
            sender = new AsynUdpSender(InetAddress.getByName(Configuration.AP_IP),
                    Configuration.UDP_SEND_PORT);
            sender.addTriggers(runController, contrast, lightness, gain,
                    nearGain, farGain, depth, mModeSpeed, modeController);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        UdpPacketValidator validator = new UdpPacket406Validator(Configuration.UDP_RECEIVE_PACKET_SIZE);
        imageCreator = new ImageCreator(conf,receiver,validator);
        depth.addObserver(imageCreator);
    }

    /**初始化显示策略*/
    private void initDisplayStrategy() {
        nonDisplayStrategy = new NonDisplayStrategy(mContext);
        timeStrategy = new TimeStrategy(mContext);
        runStateStrategyDecorator = new RunStateStrategyDecorator(mContext,timeStrategy,runController);
        paramsStrategyDecorator = new ParamsStrategyDecorator(mContext, runStateStrategyDecorator,
                contrast, lightness, gain, nearGain, farGain, depth, mModeSpeed);
        colorBarStrategyDecorator = new ColorBarStrategyDecorator(mContext, paramsStrategyDecorator);

        bModeStrategyDecorator = new BModeStrategyDecorator(mContext, colorBarStrategyDecorator);
        mModeStrategyDecorator = new MModeStrategyDecorator(mContext, colorBarStrategyDecorator);
        bbModeStrategyDecorator = new BBModeStrategyDecorator(mContext, colorBarStrategyDecorator);
        bmModeStrategyDecorator = new BMModeStrategyDecorator(mContext, colorBarStrategyDecorator);

        imageShowStrategy = new ImageShowStrategy(mContext);
        measureStrategyDecorator = new MeasureStrategyDecorator(mContext, bModeStrategyDecorator);
        playVideoStrategyDecorator = new PlayVideoStrategyDecorator(mContext, nonDisplayStrategy);

        //策略初始化工作
        mModeStrategyDecorator.setSpeed(mModeSpeed);
        bmModeStrategyDecorator.setImageCreator(imageCreator);
        bmModeStrategyDecorator.setModeController(modeController);

    }

    /**初始化显示视图*/
    private void initDisplayView() {
        displayView = new DisplayView(mContext);
        displayView.setOnWindowSizeChangedListener(new DisplayView.OnWindowSizeChangedListener() {
            @Override
            public void onWindowSizeChanged(int width, int height) {
                runStateStrategyDecorator.init(width, height);
                colorBarStrategyDecorator.init(width, height);
                bModeStrategyDecorator.init(width, height);
                mModeStrategyDecorator.init(width, height);
                bbModeStrategyDecorator.init(width, height);
                bmModeStrategyDecorator.init(width, height);
                imageShowStrategy.init(width, height);
                measureStrategyDecorator.initMask(width, height);
                playVideoStrategyDecorator.init(width, height);
            }
        });
        displayView.setDisplayStrategy(bModeStrategyDecorator);
        mViewContainer.addView(displayView);
    }

    /**是否冻结*/
    public boolean isFreeze(){
        return runController.isFreeze();
    }

    /**解冻*/
    public void unFreeze(){
        runController.unFreeze();
        receiver.start();
        imageCreator.start();
        changeWorkMode(workMode);
    }

    /**冻结*/
    public void freeze(){
        runController.freeze();
        receiver.stop();
        imageCreator.stop();
    }

    /**冻结/解冻转换*/
    public void freezeToggle(){
        if(isFreeze()){
            unFreeze();
        }else{
            freeze();
        }
    }

    /**
     * 更改工作模式
     * @param mode
     */
    public void changeWorkMode(int mode){
        switch (mode){
            case UContext.MODE_B:
                changeBMode();
                break;
            case UContext.MODE_M:
                changeMMode();
                break;
            case UContext.MODE_BB:
                changeBBMode();
                break;
            case UContext.MODE_BM:
                changeBMMode();
                break;
            default:
                break;
        }
    }

    /**
     * 添加一个新的测量项
     * @param shapeMaker
     */
    public void newMeasure(ShapeMaker shapeMaker){
        changeBMode();
        displayView.setDisplayStrategy(measureStrategyDecorator);
        measureStrategyDecorator.addShapeMaker(shapeMaker);
    }

    /**清除测量*/
    public void clearMeasure(){
        measureStrategyDecorator.removeAllShapeMaker();
        displayView.setDisplayStrategy(bModeStrategyDecorator);
    }

    /**
     * 捕获一个屏幕快照
     */
    public void takeSnapshot(){
        displayView.captureScreen();
    }

    /**
     * 加载一个快照到视图
     * @param snapshot
     */
    public void loadSnapshot(Snapshot snapshot){
        freeze();
        imageShowStrategy.loadImage(BitmapFactory.decodeFile(snapshot.getPathName()));
        displayView.setDisplayStrategy(imageShowStrategy);
    }

    /**
     * 参数增加
     * @param param
     */
    public void increaseParam(int param){
        switch (param){
            case UContext.PARAM_CONTRAST:
                contrast.increase();
                break;
            case UContext.PARAM_LIGHTNESS:
                lightness.increase();
                break;
            case UContext.PARAM_GAIN:
                gain.increase();
                break;
            case UContext.PARAM_NEAR_GAIN:
                nearGain.increase();
                break;
            case UContext.PARAM_FAR_GAIN:
                farGain.increase();
                break;
            case UContext.PARAM_DEPTH:
                depth.increase();
                break;
            case UContext.PARAM_SPEED:
                mModeSpeed.increase();
                break;
            default:
                //no op
                break;
        }
    }

    /**
     * 参数减
     * @param param
     */
    public void decreaseParam(int param){
        switch (param){
            case UContext.PARAM_CONTRAST:
                contrast.decrease();
                break;
            case UContext.PARAM_LIGHTNESS:
                lightness.decrease();
                break;
            case UContext.PARAM_GAIN:
                gain.decrease();
                break;
            case UContext.PARAM_NEAR_GAIN:
                nearGain.decrease();
                break;
            case UContext.PARAM_FAR_GAIN:
                farGain.decrease();
                break;
            case UContext.PARAM_DEPTH:
                depth.decrease();
                break;
            case UContext.PARAM_SPEED:
                mModeSpeed.decrease();
                break;
            default:
                //no op
                break;
        }
    }

    /**
     * 参数回默认值
     * @param param
     */
    public void toDefaultParam(int param){
        switch (param){
            case UContext.PARAM_CONTRAST:
                contrast.toDefault();
                break;
            case UContext.PARAM_LIGHTNESS:
                lightness.toDefault();
                break;
            case UContext.PARAM_GAIN:
                gain.toDefault();
                break;
            case UContext.PARAM_NEAR_GAIN:
                nearGain.toDefault();
                break;
            case UContext.PARAM_FAR_GAIN:
                farGain.toDefault();
                break;
            case UContext.PARAM_DEPTH:
                depth.toDefault();
                break;
            case UContext.PARAM_SPEED:
                mModeSpeed.toDefault();
                break;
            default:
                //no op
                break;
        }
    }

    private void changeBMode() {
        workMode = UContext.MODE_B;
        imageCreator.addObserver(bModeStrategyDecorator);
        imageCreator.deleteObserver(mModeStrategyDecorator);
        imageCreator.deleteObserver(bbModeStrategyDecorator);
        imageCreator.deleteObserver(bmModeStrategyDecorator);
        imageCreator.deleteObserver(playVideoStrategyDecorator);

        modeController.setMode(ModeController.Mode.MODE_B);
        imageCreator.setbEnable(true);
        imageCreator.setmEnable(false);
        displayView.setDisplayStrategy(bModeStrategyDecorator);
    }

    private void changeMMode() {
        workMode = UContext.MODE_M;
        imageCreator.addObserver(mModeStrategyDecorator);
        imageCreator.deleteObserver(bModeStrategyDecorator);
        imageCreator.deleteObserver(bbModeStrategyDecorator);
        imageCreator.deleteObserver(bmModeStrategyDecorator);
        imageCreator.deleteObserver(playVideoStrategyDecorator);

        modeController.setMode(ModeController.Mode.MODE_M);
        imageCreator.setbEnable(false);
        imageCreator.setmEnable(true);
        displayView.setDisplayStrategy(mModeStrategyDecorator);
    }

    private void changeBBMode() {
        workMode = UContext.MODE_BB;
        imageCreator.addObserver(bbModeStrategyDecorator);
        imageCreator.deleteObserver(bModeStrategyDecorator);
        imageCreator.deleteObserver(mModeStrategyDecorator);
        imageCreator.deleteObserver(bmModeStrategyDecorator);
        imageCreator.deleteObserver(playVideoStrategyDecorator);

        modeController.setMode(ModeController.Mode.MODE_BB);
        imageCreator.setbEnable(true);
        imageCreator.setmEnable(false);
        displayView.setDisplayStrategy(bbModeStrategyDecorator);
    }

    private void changeBMMode() {
        workMode = UContext.MODE_BM;
        imageCreator.addObserver(bmModeStrategyDecorator);
        imageCreator.deleteObserver(bModeStrategyDecorator);
        imageCreator.deleteObserver(mModeStrategyDecorator);
        imageCreator.deleteObserver(bbModeStrategyDecorator);
        imageCreator.deleteObserver(playVideoStrategyDecorator);

        modeController.setMode(ModeController.Mode.MODE_BM);
        imageCreator.setbEnable(true);
        imageCreator.setmEnable(true);
        displayView.setDisplayStrategy(bmModeStrategyDecorator);
    }


    /**播放视频*/
    public void playVideo() {
        freeze();
        imageCreator.addObserver(playVideoStrategyDecorator);
        displayView.setDisplayStrategy(playVideoStrategyDecorator);
        imageCreator.playVideo();
    }

    public void playVideo(File videoFile){
        freeze();
        imageCreator.addObserver(playVideoStrategyDecorator);
        displayView.setDisplayStrategy(playVideoStrategyDecorator);
        imageCreator.playVideo(videoFile);
    }

    /**保存视频到文件*/
    public void saveVideo() {
        imageCreator.saveVideo();
    }
}
