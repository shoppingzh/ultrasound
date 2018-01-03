package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Colors;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class ColorBarDisplayStrategyDecorator extends BaseDisplayStrategyDecorator {

    private Bitmap bmp = Bitmap.createBitmap(1, 256, Bitmap.Config.ARGB_8888);
    private Rect src, dst;

    private Colors colors;

    public ColorBarDisplayStrategyDecorator(UContext uContext, DisplayStrategy strategy) {
        super(uContext.getAndroidContext(), strategy);
        colors = uContext.getColors();
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        dst = new Rect(width - 50, 20, width - 5, height - 20);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        bmp.setPixels(colors.getReverse(), 0, 1, 0, 0, 1, 256);
        canvas.invalidateTextureContent(bmp);
        canvas.drawBitmap(bmp, src, dst);
    }

}
