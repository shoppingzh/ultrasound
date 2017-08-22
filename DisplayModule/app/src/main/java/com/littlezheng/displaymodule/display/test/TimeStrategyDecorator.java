package com.littlezheng.displaymodule.display.test;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.displaymodule.DateUtils;
import com.littlezheng.displaymodule.display.image.TextImage;
import com.littlezheng.displaymodule.display.strategy.AbsDisplayStrategy;
import com.littlezheng.displaymodule.display.strategy.DisplayStrategy;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public class TimeStrategyDecorator extends AbsDisplayStrategy {

    //如传入HelloStrategy，将包装为同时具有时间和提示的绘制策略
    private DisplayStrategy strategy;

    //TimeStrategy
    private Paint p;
    private TextImage time;
    private int x,y;

    public TimeStrategyDecorator(Context context,DisplayStrategy strategy) {
        super(context);
        this.strategy = strategy;
        p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(60f);
        time = new TextImage(p, 16);
    }

    public void init(int width, int height){
        time.setDrawPos((width-time.getWidth())/2,50);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        strategy.onGLDraw(canvas);

        time.drawText("当前时间："+ DateUtils.getDateTimeStr());
        canvas.invalidateTextureContent(time.getImage());
        canvas.drawBitmap(time.getImage(),time.getX(),time.getY());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        strategy.onTouchEvent(event);
        return true;
    }
}
