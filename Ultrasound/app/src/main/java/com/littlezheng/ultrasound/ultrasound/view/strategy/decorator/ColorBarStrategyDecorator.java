package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.process.Util;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

/**
 * Created by Administrator on 2017/8/25/025.
 */

public class ColorBarStrategyDecorator extends AbstractDisplayStrategyDecorator {

    private Bitmap bmp = Bitmap.createBitmap(1, 256, Bitmap.Config.ARGB_8888);
    private Rect src,dst;

    public ColorBarStrategyDecorator(Context context, DisplayStrategy displayStrategy) {
        super(context, displayStrategy);
    }

    public void init(int width, int height){
        src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        dst = new Rect(width-50, 20, width-5, height-20);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);
        bmp.setPixels(Util.currColors,0,1,0,0,1,256);
        canvas.invalidateTextureContent(bmp);
        canvas.drawBitmap(bmp,src,dst);
    }

}
