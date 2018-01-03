package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/8/29/029.
 */

public interface DisplayStrategy {

    /**
     * 显示初始化，提供了视图的宽高属性，可实现显示策略的延时初始化，因为
     * 该方法只在改变策略时调用
     * 但是也不要所有的初始化工作尤其是耗时的操作都放在该方法中执行，这样
     * 会影响到显示策略转换的效率
     *
     * @param width
     * @param height
     */
    void init(int width, int height);

    /**
     * OpenGl绘图，调用canvas画布的绘图api，所有绘制效果都将反映到显示视图上
     *
     * @param canvas
     */
    void onGLDraw(ICanvasGL canvas);

    /**
     * 视图的触摸事件
     *
     * @param event
     * @return
     */
    boolean onTouchEvent(MotionEvent event);

    /**
     * 显示策略的清理工作，如果视图不再使用到该显示策略，在切换到其它显示策略时，将对
     * 原来的显示策略进行清理工作
     */
    void cleanup();
}
