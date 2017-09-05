package com.littlezheng.drawingboard.api;

import android.graphics.Canvas;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public abstract class ShapeMaker implements Clickable {

    protected int clickCount = 0;
    protected int step;

    public ShapeMaker(int step){
        this.step = step;
    }

    @Override
    public void click(float x, float y) {
        if(clickCount < step){
            clickCount++;
        }
    }

    public boolean makeEnd(){
        return clickCount >= step;
    }

    /**
     * 创建图形
     * @param canvas
     */
    public abstract void make(Canvas canvas);

    /**
     * 重新指定位置
     * @param x
     * @param y
     */
    public abstract void reset(float x, float y);

}
