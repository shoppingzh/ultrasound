package com.littlezheng.displaymodule.display.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;
import com.littlezheng.displaymodule.display.strategy.AbsDisplayStrategy;
import com.littlezheng.displaymodule.display.strategy.DisplayStrategy;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public class RectStrategy extends AbsDisplayStrategy {

    private int wid = 300;
    private int hei = 300;
    GLPaint p = new GLPaint();
    private Rect rect;

    public RectStrategy(Context context) {
        super(context);
    }

    public void init(int width, int height) {
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
