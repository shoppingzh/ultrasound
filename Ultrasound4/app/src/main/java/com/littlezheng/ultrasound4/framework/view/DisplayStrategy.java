package com.littlezheng.ultrasound4.framework.view;

import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/9/20/020.
 */

public interface DisplayStrategy {

    /**
     * 窗口大小改变时的回调
     *
     * @param width
     * @param height
     */
    void onSurfaceChanged(int width, int height);

    /**
     * 一帧绘制的回调
     *
     * @param canvas
     */
    void onGLDraw(ICanvasGL canvas);

    /**
     * 触摸回调
     *
     * @param event
     * @return
     */
    boolean onTouchEvent(MotionEvent event);

}
