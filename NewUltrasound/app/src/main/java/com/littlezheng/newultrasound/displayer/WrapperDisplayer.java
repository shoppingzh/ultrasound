package com.littlezheng.newultrasound.displayer;

import com.littlezheng.newultrasound.graphics.Control;

/**
 * Created by Administrator on 2017/11/17/017.
 */

public abstract class WrapperDisplayer extends Displayer {

    private Displayer displayer;

    public WrapperDisplayer(Displayer displayer){
        this.displayer = displayer;
        for(Control c : displayer){
            add(c);
        }
    }

    @Override
    public void init(int width, int height) {
        displayer.init(width, height);
    }

}
