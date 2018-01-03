package com.littlezheng.ultrasound4.framework.graphics.image;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/9/22/022.
 */

public class BitmapImage extends Image {

    public BitmapImage(int wid, int hei, Bitmap.Config config) {
        this.wid = wid;
        this.hei = hei;
        this.bmp = Bitmap.createBitmap(wid, hei, config);
    }

    public void refresh(int[] pixels) {
        if (pixels.length != wid * hei) return;
        bmp.setPixels(pixels, 0, wid, 0, 0, wid, hei);
    }

}
