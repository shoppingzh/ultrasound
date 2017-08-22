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

public class TimeStrategy extends AbsDisplayStrategy {

    private Paint p;
    private TextImage time;
    private int x,y;

    public TimeStrategy(Context context){
        super(context);
        p = new Paint();
        p.setColor(Color.RED);
        p.setTextSize(60f);
        time = new TextImage(p, 16);
    }

    public void init(int width, int height){
        time.setDrawPos((width-time.getWidth())/2,(height-time.getHeight())/2);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        time.drawText("当前时间："+DateUtils.getDateTimeStr());
        canvas.invalidateTextureContent(time.getImage());
        canvas.drawBitmap(time.getImage(),time.getX(),time.getY());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                time.setDrawPos(x-time.getWidth(), y-time.getHeight());
        }
        return true;
    }
}
