package com.littlezheng.ultrasound4.framework.view;

import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/9/20/020.
 */

public class NullDisplayStrategy implements DisplayStrategy {

    @Override
    public void onSurfaceChanged(int width, int height) {
        //no op
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        //no op
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
