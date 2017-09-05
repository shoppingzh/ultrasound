package com.littlezheng.transmissionmodule.component;

import java.util.Observable;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class StateController extends Observable{

    private boolean frozen;

    public void freeze(){
        if(frozen) return;
        frozen = true;
        stateChanged();
    }

    public void unfreeze(){
        if(!frozen) return;
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
