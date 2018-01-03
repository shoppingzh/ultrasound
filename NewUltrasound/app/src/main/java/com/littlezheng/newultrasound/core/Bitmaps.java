package com.littlezheng.newultrasound.core;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/11/17/017.
 */

public class Bitmaps {

    public static final Bitmap colorBar =
            Bitmap.createBitmap(1, 256, Bitmap.Config.ARGB_4444);

    public static final Bitmap bFrame =
            Bitmap.createBitmap(BFrameSource.FRAME_WIDTH, BFrameSource.FRAME_HEIGHT, Bitmap.Config.ARGB_4444);

    public static final Bitmap mFrame =
            Bitmap.createBitmap(MFrameSource.FRAME_WIDTH, MFrameSource.FRAME_HEIGHT, Bitmap.Config.ARGB_4444);

}
