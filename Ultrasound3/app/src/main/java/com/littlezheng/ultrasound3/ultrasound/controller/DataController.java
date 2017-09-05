package com.littlezheng.ultrasound3.ultrasound.controller;

import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Colors;
import com.littlezheng.ultrasound3.ultrasound.process.MainProcessor;
import com.littlezheng.ultrasound3.ultrasound.process.PlayProcessor;

import java.io.File;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class DataController {

    private UContext uContext;

    private final MainProcessor mainProcessor;
    private final PlayProcessor playProcessor;

    public DataController(UContext uContext){
        this.uContext = uContext;
        mainProcessor = new MainProcessor(uContext);
        playProcessor = new PlayProcessor(uContext);
    }

    /**
     * 开启图像处理
     * @return 开启结果
     */
    public boolean enableProcess(){
        //正在回放中则不进行处理
        if(playProcessor.inReplay()) return false;
        mainProcessor.enableProcess(true, false);
        mainProcessor.enable();
        return true;
    }

    /**
     * 开启对应的图像处理
     * @param b 是否处理B图像
     * @param m 是否处理M图像
     * @return
     */
    public boolean enableProcess(boolean b, boolean m){
        if(! mainProcessor.inProcess()) return false;
        mainProcessor.enableProcess(b, m);
        return true;
    }

    /**
     * 关闭处理
     */
    public void disableProcess(){
        mainProcessor.disable();
    }

    /**
     * 开启回放处理
     * @return 开启结果
     */
    public boolean replay(){
        if(mainProcessor.inProcess()) return false;
        playProcessor.replay();
        return true;
    }

    /**
     * 开启视频处理
     * @param video 视频文件
     * @return 开启结果
     */
    public boolean play(File video){
        if(mainProcessor.inProcess()) return false;
        playProcessor.play(video);
        return true;
    }

    /**
     * 停止播放
     */
    public void stopPlay(){
        playProcessor.stop();
    }

    /**
     * 设置伪彩
     * @param color
     */
    public void setPseudoColor(Colors.PseudoColor color){
        if(mainProcessor.inProcess()){
            mainProcessor.setPseudoColor(color);
        }
    }

    /**
     * 保存视频
     */
    public boolean saveVideo() {
        return mainProcessor.saveVideo();
    }

    public MainProcessor getMainProcessor() {
        return mainProcessor;
    }

    public PlayProcessor getPlayProcessor() {
        return playProcessor;
    }


}
