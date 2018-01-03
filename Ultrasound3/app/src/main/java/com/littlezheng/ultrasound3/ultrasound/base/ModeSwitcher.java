package com.littlezheng.ultrasound3.ultrasound.base;

import java.util.Observable;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class ModeSwitcher extends Observable {

    private Mode mode;

    public ModeSwitcher(Mode mode) {
        this.mode = mode;
    }

    public void setMode(Mode mode) {
        //if(mode == this.mode) return;
        this.mode = mode;
        modeChanged();
    }

    public Mode getMode() {
        return mode;
    }

    /**
     * 模式变化
     */
    private void modeChanged() {
        setChanged();
        notifyObservers(mode);
    }

}
