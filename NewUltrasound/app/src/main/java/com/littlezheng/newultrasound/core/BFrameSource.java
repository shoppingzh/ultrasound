package com.littlezheng.newultrasound.core;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/11/11/011.
 */

class BFrameSource extends ArrayFrameSource implements AutoProcessible {

    public static final int FRAME_SOURCE_WIDTH = 127;
    public static final int FRAME_SOURCE_HEIGHT = 512;
    public static final int FRAME_WIDTH = 1452;
    public static final int FRAME_HEIGHT = 512;

    static final Bitmap bFrame = Bitmaps.bFrame;
    static final int[] pixels = new int[FRAME_WIDTH * FRAME_HEIGHT];
    static final int[] emptyPixels = pixels.clone();

    private SampleData sampleData;
    private Depth depth;

    public BFrameSource() {
        super(FRAME_SOURCE_WIDTH, FRAME_SOURCE_HEIGHT);
        sampleData = SampleData.getInstance();
        depth = Depth.getInstance();
    }

    private void clearColumnData(byte[] data) {
        int len;
        byte[] blankData = new byte[(len = data.length)];
        System.arraycopy(blankData, 0, data, 0, len);
    }

    @Override
    public void put(byte[] lineData) {
        clearColumnData(data[cursor]);
        System.arraycopy(lineData, 2, data[cursor], SampleData.ZEROS[depth.value][cursor], 400);
        cursor++;
        if (full()) {
            process();
            cursor = 0;
        }
    }

    @Override
    public void put(int num, byte[] lineData) {
        cursor = num;
        put(lineData);
    }

    private void clearPixels() {
        System.arraycopy(emptyPixels, 0, pixels, 0, pixels.length);
    }

    @Override
    public void process() {
        clearPixels();
        int depthValue = depth.value;
        CalculateSupport.thirdSample(pixels, data, SampleData.SECOND_SAMPLE_WIDTH_BASE,
                SampleData.SECOND_SAMPLE_HEIGHT_DEPTHS[depthValue], sampleData.positions[depthValue],
                sampleData.intervals[depthValue], SimpleFrameGenerator.colors);
        bFrame.setPixels(pixels, 0, bFrame.getWidth(), 0, 0,
                bFrame.getWidth(), bFrame.getHeight());
    }

}
