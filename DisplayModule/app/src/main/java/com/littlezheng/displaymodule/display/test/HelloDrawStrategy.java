package com.littlezheng.displaymodule.display.test;

import android.graphics.Color;
import android.graphics.Paint;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.displaymodule.display.image.TextImage;
import com.littlezheng.displaymodule.display.strategy.DrawStrategy;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public class HelloDrawStrategy implements DrawStrategy {

    private TextImage hello;
    private Paint p = new Paint();
    int x, y;

    public void init(GL10 gl, int width, int height) {

        p.setColor(Color.RED);
        p.setTextSize(50f);
        hello = new TextImage(p,12);
        x = (int) ((width-p.measureText("Hello,world!"))/2);
        y = height / 2;
        hello.setDrawPos(x, y);
        hello.drawText("Hello,world!");
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.drawBitmap(hello.getImage(), hello.getX(), hello.getY());
    }

}
