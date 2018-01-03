package com.littlezheng.newultrasound.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/11/3/003.
 */

public abstract class Control implements Drawable {

    protected final Paint paint;
    protected float x;
    protected float y;
    private Control parent;

    public Control() {
        paint = new Paint();
    }

    /**
     * 在该控件下添加子控件
     *
     * @param c
     */
    public void add(Control c) {
        throw new UnsupportedOperationException("该控件不支持添加组件！");
    }

    /**
     * 移除位于该控件下的子控件
     *
     * @param c
     */
    public void remove(Control c) {
        throw new UnsupportedOperationException("该控件不支持删除组件！");
    }

    /**
     * 获取该控件的父控件
     *
     * @return
     */
    public Control getParent() {
        return parent;
    }

    /**
     * 设置该控件的父控件
     *
     * @param parent
     */
    protected void setParent(Control parent) {
        this.parent = parent;
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointF getLocation() {
        return new PointF(x, y);
    }

    public RectF getBounds() {
        return new RectF(0, 0, 0, 0);
    }


    /**
     * 绘制边界
     *
     * @param c 画布
     */
    protected void drawBounds(Canvas c, int width, int color) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(width);
        p.setColor(color);
        c.drawRect(getBounds(), p);
    }

    /**
     * 触摸事件处理
     *
     * @param event 事件对象
     * @return 是否冒泡
     */
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
