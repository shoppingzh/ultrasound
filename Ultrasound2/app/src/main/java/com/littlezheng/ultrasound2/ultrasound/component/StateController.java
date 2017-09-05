package com.littlezheng.ultrasound2.ultrasound.component;

import java.util.Observable;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class StateController extends Observable{

    private boolean frozen = true;

    public void freeze(){
        frozen = true;
        stateChanged();
    }

    public void unfreeze(){
        frozen = false;
        stateChanged();
    }

    public boolean isFrozen() {
        return frozen;
    }

    /**
     * 状态改变
     */
    private void stateChanged() {
        setChanged();
        notifyObservers(frozen);
    }

}
