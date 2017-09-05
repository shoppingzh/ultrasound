package com.littlezheng.ultrasound2.ultrasound.transmission.protocol;

import com.littlezheng.ultrasound2.ultrasound.component.StateController;

import java.util.Observable;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class StateProtocol extends Protocol {

    private final StateController stateController;

    public StateProtocol(int controlCode, StateController stateController) {
        super(controlCode);
        this.stateController = stateController;
        stateController.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        boolean frozen = (boolean) arg;
        if(frozen){
            setData(0x00);
        }else{
            setData(0xFF);
        }
        stateChanged();
    }

    /**
     * 状态改变
     */
    private void stateChanged() {
        setChanged();
        notifyObservers(getProtocol());
    }

}
