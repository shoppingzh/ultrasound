package com.littlezheng.ultrasound4.ultrasound.display.draw;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/9/26/026.
 */

public abstract class AbstractDrawCommand implements DrawCommand {

    protected Bitmap image;

    public AbstractDrawCommand(Bitmap image) {
        this.image = image;
    }

}
