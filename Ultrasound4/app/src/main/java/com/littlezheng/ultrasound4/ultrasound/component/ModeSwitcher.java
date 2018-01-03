package com.littlezheng.ultrasound4.ultrasound.component;

import java.util.Observable;

/**
 * Created by Administrator on 2017/9/5/005.
 */

public class ModeSwitcher extends Observable {

    private Mode mode;

    public ModeSwitcher(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        //if(mode == this.mode) return;
        this.mode = mode;
        modeChanged();
    }

    /**
     * 模式变化
     */
    private void modeChanged() {
        setChanged();
        notifyObservers(mode);
    }

}
