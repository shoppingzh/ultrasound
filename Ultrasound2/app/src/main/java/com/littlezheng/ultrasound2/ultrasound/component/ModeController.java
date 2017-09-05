package com.littlezheng.ultrasound2.ultrasound.component;

import com.littlezheng.ultrasound2.ultrasound.enums.Mode;

import java.util.Observable;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class ModeController extends Observable{

    private Mode mode;

    public ModeController(Mode mode){
        this.mode = mode;
    }

    public void setMode(Mode mode) {
        if(mode == this.mode) return;
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
