package com.littlezheng.newultrasound.core;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/11/11/011.
 */

class MFrameSource extends ArrayFrameSource implements AutoProcessible {

    public static final int FRAME_SOURCE_WIDTH = 500;
    public static final int FRAME_SOURCE_HEIGHT = 400;
    public static final int FRAME_WIDTH = 500;
    public static final int FRAME_HEIGHT = 400;

    static final Bitmap mFrame = Bitmaps.mFrame;
    static final int[] pixels = new int[FRAME_WIDTH * FRAME_HEIGHT];

    public MFrameSource() {
        super(FRAME_SOURCE_WIDTH, FRAME_SOURCE_HEIGHT);
    }

    @Override
    public void put(byte[] lineData) {
        if (full()) cursor = 0;
        System.arraycopy(lineData, 2, data[cursor++], 0, 400);
        process();
    }

    @Override
    public void put(int num, byte[] lineData) {
        put(lineData);
    }

    @Override
    public void process() {
        CalculateSupport.generateMImage(pixels, data, width, height, cursor, SimpleFrameGenerator.colors);
        mFrame.setPixels(pixels, 0, mFrame.getWidth(), 0, 0,
                mFrame.getWidth(), mFrame.getHeight());
    }

}
