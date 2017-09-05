package com.littlezheng.ultrasound2.ultrasound.transmission.protocol;


import com.littlezheng.ultrasound2.ultrasound.enums.Mode;
import com.littlezheng.ultrasound2.ultrasound.component.ModeController;

import java.util.Observable;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class ModeProtocol extends Protocol {

    private final ModeController modeController;

    public ModeProtocol(int controlCode, ModeController modeController) {
        super(controlCode);
        this.modeController = modeController;
        modeController.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        Mode mode = (Mode) arg;
        switch (mode){
            case MODE_B:
                setData(0x00);
                break;
            case MODE_M:
                setData(0xE0);
                break;
            case MODE_BB:
                setData(0x50);
                break;
            case MODE_BM:
                setData(0xD0);
                break;
        }
        modeChanged();
    }

    /**
     * 模式改变
     */
    private void modeChanged() {
        setChanged();
        notifyObservers(getProtocol());
    }

}
