package com.littlezheng.drawingboard.api;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Random;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public class OvalMaker extends ShapeMaker {

    private Paint p = new Paint();
    private Point first = new Point();
    private Point second = new Point();
    private Point vertical = new Point();

    private RectF ovalRect = new RectF();

    public OvalMaker() {
        super(4);
        Random r = new Random();
        int c = Color.rgb(r.nextInt(255),r.nextInt(255),r.nextInt(255));
        p.setColor(c);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3f);
    }

    @Override
    public void click(float x, float y) {
        super.click(x, y);
        switch (clickCount){
            case 1:
                first.set((int)x, (int)y);
                break;
            case 2:
                second.set((int)x, (int)y);
                break;
            case 3:
                vertical.set((int)x, (int)y);
                break;
            case 4:
                Point leftPoint, rightPoint;
                if(first.x < second.x){
                    leftPoint = first;
                    rightPoint = second;
                }else{
                    leftPoint = second;
                    rightPoint = first;
                }
                ovalRect.set(leftPoint.x,vertical.y,rightPoint.x,vertical.y*2);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                canvas.drawCircle(first.x, first.y, 10, p);
                canvas.drawCircle(second.x, second.y, 10, p);
                canvas.drawCircle(vertical.x, vertical.y, 10, p);
                break;
            case 4:
                canvas.drawLine(ovalRect.left,ovalRect.top,ovalRect.left+500,ovalRect.top,p);
                canvas.drawLine(ovalRect.left,ovalRect.top,ovalRect.left,ovalRect.top+500,p);
                canvas.drawOval(ovalRect, p);
                break;
        }
    }

    @Override
    public void reset(float x, float y) {
        second.set((int)x, (int)y);
        Point leftPoint, rightPoint;
        if(first.x < second.x){
            leftPoint = first;
            rightPoint = second;
        }else{
            leftPoint = second;
            rightPoint = first;
        }
        ovalRect.set(leftPoint.x,vertical.y,rightPoint.x,vertical.y*2);
    }

}
