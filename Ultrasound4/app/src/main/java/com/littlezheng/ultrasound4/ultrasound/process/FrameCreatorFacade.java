package com.littlezheng.ultrasound4.ultrasound.process;

import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.Mode;
import com.littlezheng.ultrasound4.ultrasound.component.ModeSwitcher;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/24/024.
 */

public class FrameCreatorFacade implements Observer {

    private final BFrameCreator bFrameCreator;
    private final MFrameCreator mFrameCreator;

    public FrameCreatorFacade(UContext uContext) {
        bFrameCreator = new BFrameCreator(uContext);
        mFrameCreator = new MFrameCreator(uContext);

        //模式感知
        uContext.getModeSwitcher().addObserver(this);
    }

    private void enableBCreator() {
        mFrameCreator.disable();
        bFrameCreator.enable();
    }

    private void enableMCreator() {
        bFrameCreator.disable();
        mFrameCreator.enable();
    }

    public void disable() {
        bFrameCreator.disable();
        mFrameCreator.disable();
    }

    public void enable(Mode mode) {
        if (mode.isBMode()) {
            enableBCreator();
        } else {
            enableMCreator();
        }
    }

    public FrameCreator getCreator(Mode mode) {
        FrameCreator creator = null;
        if (mode.isBMode()) {
            mFrameCreator.disable();
            creator = bFrameCreator;
        } else {
            bFrameCreator.disable();
            creator = mFrameCreator;
        }
        return creator;
    }

    public float getCurrentBackFat() {
        return bFrameCreator.getCurrentBackFat();
    }

    @Override
    public void update(Observable o, Object arg) {
        ModeSwitcher modeSwitcher = (ModeSwitcher)o;
        if(modeSwitcher.getMode().isBMode()){
            enableBCreator();
        }else{
            enableMCreator();
        }
    }

    public BFrameCreator getbFrameCreator() {
        return bFrameCreator;
    }

    public MFrameCreator getmFrameCreator() {
        return mFrameCreator;
    }
}
