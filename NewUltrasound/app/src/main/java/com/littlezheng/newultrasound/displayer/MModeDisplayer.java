package com.littlezheng.newultrasound.displayer;

import com.littlezheng.newultrasound.core.FrameGenerator;
import com.littlezheng.newultrasound.graphics.ClipImageBox;

import static com.littlezheng.newultrasound.core.Bitmaps.mFrame;

/**
 * Created by Administrator on 2017/11/17/017.
 */

class MModeDisplayer extends WorkModeDisplayer {

    ClipImageBox pb = new ClipImageBox(mFrame);

    protected MModeDisplayer(FrameGenerator frameGenerator) {
        super(frameGenerator, false, false);
        frameGenerator.setFrameType(FrameGenerator.FrameType.M_FRAME);
        add(pb);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        pb.setDrawBounds(200, 100, width-200, height-100);
    }

    @Override
    public String desc() {
        return "M";
    }

}
