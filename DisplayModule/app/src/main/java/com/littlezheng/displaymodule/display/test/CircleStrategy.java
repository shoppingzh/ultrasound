package com.littlezheng.displaymodule.display.test;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;
import com.littlezheng.displaymodule.display.strategy.DrawStrategy;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public class CircleStrategy implements DrawStrategy {

    private int radius = 300;
    GLPaint p = new GLPaint();
    private int x,y;

    public void init(GL10 gl, int width, int height) {
        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL);
        x = width/2;
        y = (height-radius)/2;

    }


    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.drawCircle(x, y, radius, p);
    }

}
