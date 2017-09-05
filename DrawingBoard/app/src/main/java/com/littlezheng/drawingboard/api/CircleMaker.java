package com.littlezheng.drawingboard.api;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;

import java.util.Random;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public class CircleMaker extends ShapeMaker {

    Paint p = new Paint();
    Point first = new Point();
    Point second = new Point();
    Point center = new Point();
    float radius;
    Random ran = new Random();

    public CircleMaker() {
        super(3);
        p.setColor(Color.rgb(ran.nextInt(255),ran.nextInt(255),ran.nextInt(255)));
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);
    }

    @Override
    public void click(float x, float y) {
        super.click(x, y);
        if(clickCount == 1){
            first.set((int)x, (int)y);
        }
        if(clickCount == 2){
            second.set((int)x, (int)y);
        }
        if(clickCount == 3){
            int width = Math.abs(second.x - first.x);
            int height = Math.abs(second.y - first.y);
            radius = (float) Math.sqrt(width*width + height*height) / 2;
            int leftX = first.x > second.x ? second.x : first.x;
            int topY = first.y > second.y ? second.y : first.y;
            center.set(leftX+width/2, topY+height/2);
        }
    }

    @Override
    public void make(Canvas canvas) {
        switch (clickCount){
            case 1:
                canvas.drawCircle(first.x, first.y, 10, p);
                break;
            case 2:
                canvas.drawCircle(first.x, first.y, 10, p);
                canvas.drawCircle(second.x, second.y, 10, p);
                break;
            case 3:
                canvas.drawCircle(center.x,center.y,radius,p);
                break;
        }

    }

    @Override
    public void reset(float x, float y) {
        center.set((int)x,(int) y);
    }

}
