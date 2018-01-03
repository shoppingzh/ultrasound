package com.littlezheng.newultrasound.displayer;

import com.littlezheng.newultrasound.core.FrameGenerator;

import static com.littlezheng.newultrasound.core.Bitmaps.bFrame;

/**
 * Created by Administrator on 2017/11/16/016.
 */

class BModeDisplayer extends WorkModeDisplayer {

    BFrameImageBox pb = new BFrameImageBox(bFrame);

    protected BModeDisplayer(FrameGenerator frameGenerator) {
        super(frameGenerator, false, false);
        frameGenerator.setFrameType(FrameGenerator.FrameType.B_FRAME);
        add(pb);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        pb.setDrawBounds(200, 100, width-200, height-100);
    }

    @Override
    public String desc() {
        return "B";
    }

}
