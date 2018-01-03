package com.littlezheng.ultrasound4.ultrasound.display.draw;

import android.graphics.Bitmap;

import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/9/26/026.
 */

public class PointDrawCommand extends AbstractDrawCommand {

    private int x;
    private int y;

    public PointDrawCommand(Bitmap image, int x, int y) {
        super(image);
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute(ICanvasGL canvas) {
        canvas.drawBitmap(image, x, y);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
