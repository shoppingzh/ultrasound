package com.littlezheng.ultrasound4.ultrasound.transfer;

import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.Mode;
import com.littlezheng.ultrasound4.ultrasound.component.Param;

/**
 * Created by Administrator on 2017/9/24/024.
 */

public class TransferFacade {

    private UContext uContext;
    private UdpTransceiver udpTransceiver;

    public TransferFacade(UContext uContext) {
        this.uContext = uContext;
        udpTransceiver = uContext.getUdpTransceiver();
        init();
    }

    /**
     * 初始化组件
     */
    private void init() {
        udpTransceiver.addProtocol(new ParamProtocol(0xFA,
                uContext.getParam(UContext.PARAM_CONTRAST)));
        udpTransceiver.addProtocol(new ParamProtocol(0xFB,
                uContext.getParam(UContext.PARAM_LIGHTNESS)));
        udpTransceiver.addProtocol(new ParamProtocol(0xFC,
                uContext.getParam(UContext.PARAM_GAIN)));
        udpTransceiver.addProtocol(new ParamProtocol(0xFD,
                uContext.getParam(UContext.PARAM_NEAR_GAIN)));
        udpTransceiver.addProtocol(new ParamProtocol(0xFE,
                uContext.getParam(UContext.PARAM_FAR_GAIN)));
        udpTransceiver.addProtocol(new ParamProtocol(0xF9,
                uContext.getParam(UContext.PARAM_DEPTH)));
        udpTransceiver.addProtocol(new ParamProtocol(0xF3,
                uContext.getParam(UContext.PARAM_SPEED)));
        udpTransceiver.addProtocol(new StateProtocol(0xF0,
                uContext.getStateSwitcher()));
        udpTransceiver.addProtocol(new ModeProtocol(0xF2,
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
        udpTransceiver.disableReceive(true);
    }

    /**
     * 解冻
     */
    public void unfreeze() {
        uContext.getStateSwitcher().unfreeze();
        udpTransceiver.enableReceive();
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
