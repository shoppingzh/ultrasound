package com.littlezheng.displaymodule.display.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.displaymodule.display.image.TextImage;
import com.littlezheng.displaymodule.display.strategy.AbsDisplayStrategy;
import com.littlezheng.displaymodule.display.strategy.DisplayStrategy;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public class HelloDisplayStrategy extends AbsDisplayStrategy {

    private TextImage hello;
    private Paint p = new Paint();
    int x, y;

    public HelloDisplayStrategy(Context context) {
        super(context);
    }

    public void init(int width, int height) {
        p.setColor(Color.RED);
        p.setTextSize(50f);
        hello = new TextImage(p,6);
        x = (int) ((width-p.measureText("Hello,world!"))/2);
        y = height / 2;
        hello.setDrawPos(x, y);
        hello.drawText("Hello,world!");
    }


    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.drawBitmap(hello.getImage(), hello.getX(), hello.getY());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                hello.setDrawPos(x-hello.getWidth(), y-hello.getHeight());
        }
        return true;
    }
}
