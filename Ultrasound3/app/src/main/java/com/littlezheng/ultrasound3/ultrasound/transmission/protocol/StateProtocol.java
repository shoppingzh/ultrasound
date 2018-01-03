package com.littlezheng.ultrasound3.ultrasound.transmission.protocol;

import java.util.Observable;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class StateProtocol extends Protocol {

    public StateProtocol(int controlCode, Observable trigger) {
        super(controlCode, trigger);
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean frozen = (boolean) arg;
        if (frozen) {
            setData(0x00);
        } else {
            setData(0xFF);
        }
        changed();
    }

}
