package com.littlezheng.ultrasound2.ultrasound.component;

import android.content.Context;

import com.littlezheng.ultrasound2.ultrasound.enums.Mode;
import com.littlezheng.ultrasound2.ultrasound.enums.Param;
import com.littlezheng.ultrasound2.ultrasound.transmission.UdpTransmitter;
import com.littlezheng.ultrasound2.ultrasound.transmission.protocol.StateProtocol;
import com.littlezheng.ultrasound2.ultrasound.transmission.protocol.ModeProtocol;
import com.littlezheng.ultrasound2.ultrasound.transmission.protocol.ParamProtocol;

import java.io.IOException;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class FrontController {

    private final UContext uContext;

    private UdpTransmitter udpTransmitter;

    public FrontController(UContext uContext){
        this.uContext = uContext;
        UdpTransmitter.Config conf = new UdpTransmitter.Config();
        try {
            conf.loadFromStream(uContext.getContext().getAssets().open("transmission.properties"));
            udpTransmitter = new UdpTransmitter(conf);
        } catch (IOException e) {
            throw new RuntimeException("初始化失败"+e.getMessage());
        }
        init();
        //默认冻结状态
        freeze();
    }

    /**
     * 初始化组件
     */
    private void init() {
        udpTransmitter.addProtocol(new ParamProtocol(0xFA, uContext.getContrast()));
        udpTransmitter.addProtocol(new ParamProtocol(0xFB, uContext.getLightness()));
        udpTransmitter.addProtocol(new ParamProtocol(0xFC, uContext.getGain()));
        udpTransmitter.addProtocol(new ParamProtocol(0xFD, uContext.getNearGain()));
        udpTransmitter.addProtocol(new ParamProtocol(0xFE, uContext.getFarGain()));
        udpTransmitter.addProtocol(new ParamProtocol(0xF9, uContext.getDepth()));
        udpTransmitter.addProtocol(new ParamProtocol(0xF3, uContext.getSpeed()));
        udpTransmitter.addProtocol(new StateProtocol(0xF0, uContext.getStateController()));
        udpTransmitter.addProtocol(new ModeProtocol(0xF2, uContext.getModeController()));
    }


    /**
     * 参数增加
     *
     * @param p
     */
    public void increase(Param p){
        switch (p){
            case CONTRAST:
                uContext.getContrast().increase();
                break;
            case LIGHTNESS:
                uContext.getLightness().increase();
                break;
            case GAIN:
                uContext.getGain().increase();
                break;
            case NEAR_GAIN:
                uContext.getNearGain().increase();
                break;
            case FAR_GAIN:
                uContext.getFarGain().increase();
                break;
            case DEPTH:
                uContext.getDepth().increase();
                break;
            case SPEED:
                uContext.getSpeed().increase();
                break;
        }
    }

    /**
     * 参数减少
     *
     * @param p
     */
    public void decrease(Param p){
        switch (p){
            case CONTRAST:
                uContext.getContrast().decrease();
                break;
            case LIGHTNESS:
                uContext.getLightness().decrease();
                break;
            case GAIN:
                uContext.getGain().decrease();
                break;
            case NEAR_GAIN:
                uContext.getNearGain().decrease();
                break;
            case FAR_GAIN:
                uContext.getFarGain().decrease();
                break;
            case DEPTH:
                uContext.getDepth().decrease();
                break;
            case SPEED:
                uContext.getSpeed().decrease();
                break;
        }
    }

    /**
     * 参数重置
     *
     * @param p
     */
    public void reset(Param p){
        switch (p){
            case CONTRAST:
                uContext.getContrast().reset();
                break;
            case LIGHTNESS:
                uContext.getLightness().reset();
                break;
            case GAIN:
                uContext.getGain().reset();
                break;
            case NEAR_GAIN:
                uContext.getNearGain().reset();
                break;
            case FAR_GAIN:
                uContext.getFarGain().reset();
                break;
            case DEPTH:
                uContext.getDepth().reset();
                break;
            case SPEED:
                uContext.getSpeed().reset();
                break;
        }
    }

    /**
     * 冻结
     */
    public void freeze(){
        uContext.getStateController().freeze();
        udpTransmitter.disableReceive(true);
    }

    /**
     * 解冻
     */
    public void unfreeze(){
        uContext.getStateController().unfreeze();
        udpTransmitter.enableReceive();
    }

    /**
     * 是否处于冻结状态
     * @return
     */
    public boolean isFrozen(){
        return uContext.getStateController().isFrozen();
    }

    /**
     * 冻结/解冻状态切换
     */
    public void toggleFreeze(){
        if(uContext.getStateController().isFrozen()){
            unfreeze();
        }else{
            freeze();
        }
    }


    /**
     * 设置工作模式
     *
     * @param mode
     */
    public void setMode(Mode mode){
        uContext.getModeController().setMode(mode);
    }

    public UdpTransmitter getUdpTransmitter() {
        return udpTransmitter;
    }
}
