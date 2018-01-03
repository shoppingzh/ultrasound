package com.littlezheng.ultrasound4.ultrasound.display.draw;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/9/26/026.
 */

public class RectDrawCommand extends AbstractDrawCommand {

    private Rect src;
    private RectF dst;

    public RectDrawCommand(Bitmap image, Rect src, RectF dst) {
        super(image);
        this.src = src;
        this.dst = dst;
    }

    @Override
    public void execute(ICanvasGL canvas) {
        canvas.drawBitmap(image, src, dst);
    }

    public void resetDestination(RectF dst) {
        this.dst = dst;
    }

}
