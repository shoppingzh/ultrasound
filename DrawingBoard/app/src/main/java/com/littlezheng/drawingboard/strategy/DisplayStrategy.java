package com.littlezheng.drawingboard.strategy;

import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public interface DisplayStrategy {

    void onGLDraw(ICanvasGL canvas);

    boolean onTouchEvent(MotionEvent event);

}
