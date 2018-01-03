package com.littlezheng.ultrasound3.ultrasound.display.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/**
 * Created by zxp on 2017/7/24.
 */

public abstract class Image implements Drawable {

    protected Bitmap bmp;
    protected Canvas canvas;
    protected int wid;
    protected int hei;

    //绘制
    protected int x;
    protected int y;

    private Paint paint = new Paint();

    public Image() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    /**
     * 获取要绘制的位图
     *
     * @return
     */
    public Bitmap getImage() {
        return bmp;
    }

    public int getWidth() {
        return wid;
    }

    public int getHeight() {
        return hei;
    }

    /**
     * 获取图片的绘制区域
     *
     * @return
     */
    public Rect getDrawRect() {
        return new Rect(x, y, x + wid, y + hei);
    }

    @Override
    public void setDrawPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public final void clear(Canvas c) {
//        Paint paint = new Paint();
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        c.drawPaint(paint);
    }
}
