package com.littlezheng.ultrasound4.ultrasound.display;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.view.DisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.Colors;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/23/023.
 */

public class ColorBarDisplayStrategy extends UMultipleDisplayStrategy implements Observer {

    private Bitmap bmp = Bitmap.createBitmap(1, 256, Bitmap.Config.ARGB_4444);
    private Rect src, dst;

    private Colors colors;

    public ColorBarDisplayStrategy(UContext uContext, DisplayStrategy strategy) {
        super(uContext, strategy);
        colors = uContext.getColors();

        //颜色改变感知
        colors.addObserver(this);
        colors.notifyObservers();
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);

        src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        dst = new Rect(width - 50, 20, width - 5, height - 20);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        canvas.invalidateTextureContent(bmp);
        canvas.drawBitmap(bmp, src, dst);
    }


    @Override
    public void update(Observable o, Object arg) {
        bmp.setPixels(colors.getReverse(), 0, 1, 0, 0, 1, 256);
    }

}
