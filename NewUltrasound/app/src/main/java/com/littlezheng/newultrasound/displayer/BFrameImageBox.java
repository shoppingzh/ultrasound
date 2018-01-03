package com.littlezheng.newultrasound.displayer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.littlezheng.newultrasound.core.Depth;
import com.littlezheng.newultrasound.graphics.ClipImageBox;

/**
 * Created by Administrator on 2017/11/13/013.
 */

class BFrameImageBox extends ClipImageBox {

    static final int[] B_FRAME_ORIGINAL_WIDTHS = {
            1452, 1244, 1120, 1036, 978,
            932, 898, 870, 848, 828,
            812, 798, 788, 776, 768,
            760, 752, 746, 740, 734,
    };
    static final int[] B_FRAME_DISPLAY_WIDTHS = {
            1299, 1113, 1002, 928, 875,
            835, 804, 779, 759, 742,
            727, 715, 705, 695, 688,
            680, 673, 668, 663, 657,
    };
    static final int[] B_FRAME_DISPLAY_HEIGHTS = {
            512, 484, 467, 457, 449,
            442, 438, 434, 430, 428,
            426, 424, 423, 421, 420,
            419, 418, 417, 416, 416,
    };
    static final Rect[] srcRects = new Rect[20];
    static{
        for (int i = 0; i < 20; i++) {
            int thirdSampleWid = B_FRAME_ORIGINAL_WIDTHS[i];
            int displayWid = B_FRAME_DISPLAY_WIDTHS[i];
            int displayHei = B_FRAME_DISPLAY_HEIGHTS[i];
            Rect src = new Rect(thirdSampleWid - displayWid, 0, displayWid, displayHei);
            srcRects[i] = src;
        }
    }

    private Depth depth = Depth.getInstance();

    public BFrameImageBox(Bitmap picture) {
        super(picture);
    }

    @Override
    public void draw(Canvas c) {
        c.drawBitmap(getImage(), srcRects[depth.getValue()], dstRect, paint);
    }

}
