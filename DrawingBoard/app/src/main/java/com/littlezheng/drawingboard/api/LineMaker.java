package com.littlezheng.drawingboard.api;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;

import java.util.Random;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public class LineMaker extends ShapeMaker {

    private final Random ran = new Random();
    Paint p = new Paint();
    Point first = new Point();
    Point second = new Point();
    Paint textPaint = new Paint();

    public LineMaker() {
        super(3);
        int color = Color.rgb(ran.nextInt(255),ran.nextInt(255),ran.nextInt(255));
        p.setColor(color);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);

        textPaint.setColor(color);
        textPaint.setTextSize(15f);
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
                canvas.drawLine(first.x, first.y, second.x, second.y, p);
                canvas.drawText("长度："+MeasureUtils.length(first,second),first.x,first.y-10,textPaint);
                break;
        }
    }

    @Override
    public void reset(float x, float y) {
        getCloser(x, y).set((int)x, (int)y);
    }

    private Point getCloser(float x, float y){
        Point p = new Point((int)x,(int)y);
        double firstLen = MeasureUtils.length(p,first);
        double secondLen = MeasureUtils.length(p, second);
        return firstLen < secondLen ? first : second;
    }

}
