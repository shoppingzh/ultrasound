package com.littlezheng.transmissionmodule.transmission;

import com.littlezheng.transmissionmodule.component.Param;
import com.littlezheng.transmissionmodule.component.StateController;
import com.littlezheng.transmissionmodule.component.Mode;
import com.littlezheng.transmissionmodule.component.ModeController;
import com.littlezheng.transmissionmodule.component.ParamController;
import com.littlezheng.transmissionmodule.transmission.protocol.FreezeProtocol;
import com.littlezheng.transmissionmodule.transmission.protocol.ModeProtocol;
import com.littlezheng.transmissionmodule.transmission.protocol.ParamProtocol;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class Fronter {

    private ParamController contrast;
    private ParamController lightness;
    private ParamController gain;
    private ParamController nearGain;
    private ParamController farGain;
    private ParamController depth;
    private ParamController speed;

    private StateController stateController;
    private ModeController modeController;

    private final UdpTransmitter udpTransmitter;

    public Fronter(UdpTransmitter udpTransmitter){
        this.udpTransmitter = udpTransmitter;

        initComponents();
    }

    /**
     * 初始化组件
     */
    private void initComponents() {
        contrast = new ParamController("对比度", 32, 48, 16, 1);
        lightness = new ParamController("亮度", 32, 46, 20, 1);
        gain = new ParamController("总增", 16, 32, 1, 1);
        nearGain = new ParamController("近增", 16, 32, 1, 1);
        farGain = new ParamController("远增", 16, 32, 1, 1);
        depth = new ParamController("深度", 13, 19, 0, 1);
        speed = new ParamController("速度", 3, 3, 0, 1);
        stateController = new StateController();
        modeController = new ModeController(Mode.MODE_B);

        udpTransmitter.addProtocol(new ParamProtocol(0xFA, contrast));
        udpTransmitter.addProtocol(new ParamProtocol(0xFB, lightness));
        udpTransmitter.addProtocol(new ParamProtocol(0xFC, gain));
        udpTransmitter.addProtocol(new ParamProtocol(0xFD, nearGain));
        udpTransmitter.addProtocol(new ParamProtocol(0xFE, farGain));
        udpTransmitter.addProtocol(new ParamProtocol(0xF9, depth));
        udpTransmitter.addProtocol(new ParamProtocol(0xF3, speed));
        udpTransmitter.addProtocol(new FreezeProtocol(0xF0, stateController));
        udpTransmitter.addProtocol(new ModeProtocol(0xF2, modeController));
    }


    /**
     * 参数增加
     *
     * @param p
     */
    public void increase(Param p){
        switch (p){
            case CONTRAST:
                contrast.increase();
                break;
            case LIGHTNESS:
                lightness.increase();
                break;
            case GAIN:
                gain.increase();
                break;
            case NEAR_GAIN:
                nearGain.increase();
                break;
            case FAR_GAIN:
                farGain.increase();
                break;
            case DEPTH:
                depth.increase();
                break;
            case SPEED:
                speed.increase();
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
                contrast.decrease();
                break;
            case LIGHTNESS:
                lightness.decrease();
                break;
            case GAIN:
                gain.decrease();
                break;
            case NEAR_GAIN:
                nearGain.decrease();
                break;
            case FAR_GAIN:
                farGain.decrease();
                break;
            case DEPTH:
                depth.decrease();
                break;
            case SPEED:
                speed.decrease();
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
                contrast.reset();
                break;
            case LIGHTNESS:
                lightness.reset();
                break;
            case GAIN:
                gain.reset();
                break;
            case NEAR_GAIN:
                nearGain.reset();
                break;
            case FAR_GAIN:
                farGain.reset();
                break;
            case DEPTH:
                depth.reset();
                break;
            case SPEED:
                speed.reset();
                break;
        }
    }

    /**
     * 冻结
     */
    public void freeze(){
        stateController.freeze();
    }

    /**
     * 解冻
     */
    public void unfreeze(){
        stateController.unfreeze();
    }

    /**
     * 设置工作模式
     *
     * @param mode
     */
    public void setMode(Mode mode){
        modeController.setMode(mode);
    }



    /***********************************GETTERS***************************************/

    public ParamController getContrast() {
        return contrast;
    }

    public ParamController getLightness() {
        return lightness;
    }

    public ParamController getGain() {
        return gain;
    }

    public ParamController getNearGain() {
        return nearGain;
    }

    public ParamController getFarGain() {
        return farGain;
    }

    public ParamController getDepth() {
        return depth;
    }

    public ParamController getSpeed() {
        return speed;
    }

    public StateController getStateController() {
        return stateController;
    }

    public ModeController getModeController() {
        return modeController;
    }

    public UdpTransmitter getUdpTransmitter() {
        return udpTransmitter;
    }
}
