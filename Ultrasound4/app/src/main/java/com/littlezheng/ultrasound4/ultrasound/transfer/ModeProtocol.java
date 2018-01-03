package com.littlezheng.ultrasound4.ultrasound.transfer;

import com.littlezheng.ultrasound4.ultrasound.component.Mode;

import java.util.Observable;

/**
 * Created by Administrator on 2017/8/31/031.
 */

class ModeProtocol extends Protocol {

    public ModeProtocol(int controlCode, Observable trigger) {
        super(controlCode, trigger);
    }

    @Override
    public void update(Observable o, Object arg) {
        Mode mode = (Mode) arg;
        switch (mode) {
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
                setData(0x00);
                break;
        }
        changed();
    }

}