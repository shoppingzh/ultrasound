package com.littlezheng.displaymodule.display.strategy;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glview.GLContinuousView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public interface DisplayStrategy {

    /**
     * 绘制策略，当GLSurfaceView每次绘制时，调用该方法
     * @param canvas GL画布
     */
    void onGLDraw(ICanvasGL canvas);

    /**
     * 触摸事件，实现类界面与用户有交互时实现
     * @param event
     * @return
     */
    boolean onTouchEvent(MotionEvent event);

}
