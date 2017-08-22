package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.Configuration;
import com.littlezheng.ultrasound.ultrasound.process.ImageCreator;
import com.littlezheng.ultrasound.ultrasound.view.strategy.AbsDisplayStrategy;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public class MModeStrategyDecorator extends AbsDisplayStrategy implements Observer {

    private DisplayStrategy strategy;

    private Bitmap bmp = Bitmap.createBitmap(500, Configuration.UDP_USEFUL_DATA_LEN,
            Bitmap.Config.ARGB_8888);
    private Rect src,dst;

    public MModeStrategyDecorator(Context context, DisplayStrategy strategy) {
        super(context);
        this.strategy = strategy;
    }

    public void init(int width, int height){
        src = new Rect(0,0,bmp.getWidth(),bmp.getHeight());
        int wid = (int) (width * 0.6f), hei = wid / 500 * 400;
        int left = (width-wid)/2, top = (height-hei)/2;
        dst = new Rect(left,top,wid+left,top+hei);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        strategy.onGLDraw(canvas);

        canvas.invalidateTextureContent(bmp);
        canvas.drawBitmap(bmp,src,dst);
    }

    @Override
    public void update(Observable o, Object arg) {
        bmp.setPixels(ImageCreator.mPixels,0,500,0,0,500,400);
    }

}
