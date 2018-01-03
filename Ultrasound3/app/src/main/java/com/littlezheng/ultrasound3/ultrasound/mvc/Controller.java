package com.littlezheng.ultrasound3.ultrasound.mvc;

import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Mode;
import com.littlezheng.ultrasound3.ultrasound.base.Param;
import com.littlezheng.ultrasound3.ultrasound.transmission.UdpTransmitter;
import com.littlezheng.ultrasound3.ultrasound.transmission.protocol.ModeProtocol;
import com.littlezheng.ultrasound3.ultrasound.transmission.protocol.ParamProtocol;
import com.littlezheng.ultrasound3.ultrasound.transmission.protocol.StateProtocol;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class Controller {

    private final UContext uContext;

    private UdpTransmitter udpTransmitter;

    public Controller(UContext uContext) {
        this.uContext = uContext;
        udpTransmitter = uContext.getUdpTransmitter();
        init();
    }

    /**
     * 初始化组件
     */
    private void init() {
        udpTransmitter.addProtocol(new ParamProtocol(0xFA,
                uContext.getParam(UContext.PARAM_CONTRAST)));
        udpTransmitter.addProtocol(new ParamProtocol(0xFB,
                uContext.getParam(UContext.PARAM_LIGHTNESS)));
        udpTransmitter.addProtocol(new ParamProtocol(0xFC,
                uContext.getParam(UContext.PARAM_GAIN)));
        udpTransmitter.addProtocol(new ParamProtocol(0xFD,
                uContext.getParam(UContext.PARAM_NEAR_GAIN)));
        udpTransmitter.addProtocol(new ParamProtocol(0xFE,
                uContext.getParam(UContext.PARAM_FAR_GAIN)));
        udpTransmitter.addProtocol(new ParamProtocol(0xF9,
                uContext.getParam(UContext.PARAM_DEPTH)));
        udpTransmitter.addProtocol(new ParamProtocol(0xF3,
                uContext.getParam(UContext.PARAM_SPEED)));
        udpTransmitter.addProtocol(new StateProtocol(0xF0,
                uContext.getStateSwitcher()));
        udpTransmitter.addProtocol(new ModeProtocol(0xF2,
                uContext.getModeSwitcher()));
    }


    /**
     * 参数增加
     *
     * @param param
     */
    public void increase(int param) {
        Param p = uContext.getParam(param);
        if (p != null) {
            p.increase();
        }
    }

    /**
     * 参数减少
     *
     * @param param
     */
    public void decrease(int param) {
        Param p = uContext.getParam(param);
        if (p != null) {
            p.decrease();
        }
    }

    /**
     * 参数重置
     *
     * @param param
     */
    public void reset(int param) {
        Param p = uContext.getParam(param);
        if (p != null) {
            p.reset();
        }
    }

    /**
     * 冻结
     */
    public void freeze() {
        uContext.getStateSwitcher().freeze();
        udpTransmitter.disableReceive(true);
    }

    /**
     * 解冻
     */
    public void unfreeze() {
        uContext.getStateSwitcher().unfreeze();
        udpTransmitter.enableReceive();
    }

    /**
     * 是否处于冻结状态
     *
     * @return
     */
    public boolean isFrozen() {
        return uContext.getStateSwitcher().isFrozen();
    }

    /**
     * 冻结/解冻状态切换
     */
    public void toggleFreeze() {
        if (uContext.getStateSwitcher().isFrozen()) {
            unfreeze();
        } else {
            freeze();
        }
    }


    /**
     * 设置工作模式
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        uContext.getModeSwitcher().setMode(mode);
    }

}
