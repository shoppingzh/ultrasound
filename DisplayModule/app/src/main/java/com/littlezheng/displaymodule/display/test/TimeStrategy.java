package com.littlezheng.displaymodule.display.test;

import android.graphics.Color;
import android.graphics.Paint;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.displaymodule.DateUtils;
import com.littlezheng.displaymodule.display.image.TextImage;
import com.littlezheng.displaymodule.display.strategy.DrawStrategy;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public class TimeStrategy implements DrawStrategy {

    private Paint p;
    private TextImage time;
    private int x,y;

    public TimeStrategy(){
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

}
