package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.content.Context;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/8/29/029.
 */

public class NonDisplayStrategy extends BaseDisplayStrategy {

    public NonDisplayStrategy(Context context) {
        super(context);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
