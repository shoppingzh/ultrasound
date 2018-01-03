package com.littlezheng.ultrasound3.ultrasound.base;

import java.util.Observable;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class StateSwitcher extends Observable {

    private boolean frozen = true;

    public void freeze() {
        frozen = true;
        stateChanged();
    }

    public void unfreeze() {
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
