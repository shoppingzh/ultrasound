package com.littlezheng.newultrasound.displayer;

import android.view.MotionEvent;

import com.littlezheng.newultrasound.core.FrameGenerator;
import com.littlezheng.newultrasound.graphics.ClipImageBox;
import com.littlezheng.newultrasound.transmission.UdpSender;

import static com.littlezheng.newultrasound.core.Bitmaps.bFrame;
import static com.littlezheng.newultrasound.core.Bitmaps.mFrame;

/**
 * Created by Administrator on 2017/11/17/017.
 */

class BMModeDisplayer extends WorkModeDisplayer {

    BFrameImageBox pbb = new BFrameImageBox(bFrame);
    ClipImageBox pbm = new ClipImageBox(mFrame);

    protected BMModeDisplayer(FrameGenerator frameGenerator, boolean left) {
        super(frameGenerator, true, left);
        if(left){
            UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xf2, 0x00, (byte) 0xaa, 0x55});
            frameGenerator.setFrameType(FrameGenerator.FrameType.B_FRAME);
            pbm.setImage(EMPTY_ASIDE_FRAME);
        }else{
            UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xf2, (byte)0xe0, (byte) 0xaa, 0x55});
            frameGenerator.setFrameType(FrameGenerator.FrameType.M_FRAME);
            pbb.setImage(EMPTY_ASIDE_FRAME);
        }
        add(pbb);
        add(pbm);
    }

    BMModeDisplayer(FrameGenerator frameGenerator){
        this(frameGenerator, true);
    }

    private void ensureStatic() {
        if(isLeft()){
            UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xf2, 0x00, (byte) 0xaa, 0x55});
            frameGenerator.setFrameType(FrameGenerator.FrameType.B_FRAME);
            pbb.setImage(bFrame);
        }else{
            UdpSender.sendData(new byte[]{0x55, (byte) 0xaa, (byte) 0xf2, (byte)0xe0, (byte) 0xaa, 0x55});
            frameGenerator.setFrameType(FrameGenerator.FrameType.M_FRAME);
            pbm.setImage(mFrame);
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
        pbb.setDrawBounds(100, 150, width/2-10, height-150);
        pbm.setDrawBounds(width/2+10, 150, width-100, height-150);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX(), y = event.getY();
                if((pbb.getBounds().contains(x, y) && isLeft()) ||
                        (pbm.getBounds().contains(x, y) && !isLeft()) ){
                    toggle();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public String desc() {
        return "B/M(" + (isLeft() ? "L" : "R") + ")";
    }

}
