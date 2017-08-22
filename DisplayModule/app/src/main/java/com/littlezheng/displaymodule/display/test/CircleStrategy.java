package com.littlezheng.displaymodule.display.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;
import com.littlezheng.displaymodule.display.strategy.AbsDisplayStrategy;
import com.littlezheng.displaymodule.display.strategy.DisplayStrategy;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public class CircleStrategy extends AbsDisplayStrategy {

    private int radius = 300;
    GLPaint p = new GLPaint();
    private int x,y;

    public CircleStrategy(Context context) {
        super(context);
    }

    public void init(int width, int height) {
        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL);
        x = width/2;
        y = (height-radius)/2;
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.drawCircle(x, y, radius, p);
    }

    Random r = new Random();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            p.setColor(Color.rgb(r.nextInt(255),r.nextInt(255),r.nextInt(255)));
        }
        return super.onTouchEvent(event);
    }

}
