package com.littlezheng.newultrasound.displayer;

import android.graphics.Bitmap;
import android.view.MotionEvent;

import com.littlezheng.newultrasound.core.FrameGenerator;

import static com.littlezheng.newultrasound.core.Bitmaps.bFrame;

/**
 * Created by Administrator on 2017/11/16/016.
 */

class BBModeDisplayer extends WorkModeDisplayer {

    BFrameImageBox pbl = new BFrameImageBox(bFrame);
    BFrameImageBox pbr = new BFrameImageBox(bFrame);

    protected BBModeDisplayer(FrameGenerator frameGenerator, boolean left) {
        super(frameGenerator, true, left);
        frameGenerator.setFrameType(FrameGenerator.FrameType.B_FRAME);
        if(left){
            pbr.setImage(EMPTY_ASIDE_FRAME);
        }else{
            pbl.setImage(EMPTY_ASIDE_FRAME);
        }
        add(pbl);
        add(pbr);
    }

    BBModeDisplayer(FrameGenerator frameGenerator){
        this(frameGenerator, true);
    }

    //确定一个图片盒子的图片保持静止
    private void ensureStatic() {
        if(isLeft()){
            pbl.setImage(bFrame);
            pbr.setImage(Bitmap.createBitmap(bFrame));
        }else{
            pbr.setImage(bFrame);
            pbl.setImage(Bitmap.createBitmap(bFrame));
        }
    }

    @Override
    public void toggle() {
        super.toggle();
        ensureStatic();
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        pbl.setDrawBounds(100, 150, width/2-10, height-150);
        pbr.setDrawBounds(width/2+10, 150, width-100, height-150);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX(), y = event.getY();
                if((pbl.getBounds().contains(x, y) && isLeft()) ||
                        (pbr.getBounds().contains(x, y) && !isLeft()) ){
                    toggle();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public String desc() {
        return "B/B(" + (isLeft() ? "L" : "R") + ")";
    }

}
