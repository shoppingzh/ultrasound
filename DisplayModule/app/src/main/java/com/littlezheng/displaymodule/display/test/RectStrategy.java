package com.littlezheng.displaymodule.display.test;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;
import com.littlezheng.displaymodule.display.strategy.DrawStrategy;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public class RectStrategy implements DrawStrategy {

    private int wid = 300;
    private int hei = 300;
    GLPaint p = new GLPaint();
    private Rect rect;

    public void init(GL10 gl, int width, int height) {
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        int left = (width-wid)/2;
        int top = (height-hei)/2;
        rect = new Rect(left, top, left+wid, top+hei);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.drawRect(rect,p);
    }

}
