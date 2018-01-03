package com.littlezheng.newultrasound.core;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Administrator on 2017/11/11/011.
 */

public class SimpleFrameGenerator implements FrameGenerator{

    static final int[] grayColors = new int[256];
    static final int[] redColors = new int[256];
    static final int[] yellowColors = new int[256];
    static final int[] mixColors = new int[256];
    static final int[] reverseGray = new int[256];
    static final int[] reverseRed = new int[256];
    static final int[] reverseYellow = new int[256];
    static final int[] reverseMix = new int[256];
    static int[] colors = redColors;

    static {
        for (int i = 0; i < 256; i++) {
            int ri = 256 - i;
            grayColors[i] = Color.rgb(i, i, i);
            reverseGray[i] = Color.rgb(ri, ri, ri);
            redColors[i] = Color.rgb(i, 0, 0);
            reverseRed[i] = Color.rgb(ri, 0, 0);
            yellowColors[i] = Color.rgb(i, i, 0);
            reverseYellow[i] = Color.rgb(ri, ri, 0);
            if (i < 72) {
                mixColors[i] = Color.rgb(i, i, 0);
                reverseMix[i] = Color.rgb(ri, ri, 0);
            } else {
                mixColors[i] = Color.rgb(i, 0, 0);
                reverseMix[i] = Color.rgb(ri, 0, 0);
            }
        }
    }

    static final Bitmap colorBar = Bitmaps.colorBar;

    private final FrameSource bSource = new BFrameSource();
    private final FrameSource mSource = new MFrameSource();
    private FrameSource source;

    public SimpleFrameGenerator() {
        setFrameType(FrameType.B_FRAME);
        setPseudoColor(PseudoColor.NORMAL);
    }

    @Override
    public void putData(byte[] data) {
        source.put(data[403] - 1, data);
    }

    @Override
    public void setFrameType(FrameType type) {
        switch (type) {
            case B_FRAME:
                source = bSource;
                break;
            case M_FRAME:
                source = mSource;
                break;
        }
    }

    @Override
    public FrameType getFrameType(){
        return source == bSource ? FrameType.B_FRAME : FrameType.M_FRAME;
    }

    @Override
    public void setPseudoColor(PseudoColor pseudoColor) {
        switch (pseudoColor) {
            case NORMAL:
                colors = grayColors;
                colorBar.setPixels(reverseGray, 0, colorBar.getWidth(),
                        0, 0, colorBar.getWidth(), colorBar.getHeight());
                break;
            case RED:
                colors = redColors;
                colorBar.setPixels(reverseRed, 0, colorBar.getWidth(),
                        0, 0, colorBar.getWidth(), colorBar.getHeight());
                break;
            case YELLOW:
                colors = yellowColors;
                colorBar.setPixels(reverseYellow, 0, colorBar.getWidth(),
                        0, 0, colorBar.getWidth(), colorBar.getHeight());
                break;
            case MIX:
                colors = mixColors;
                colorBar.setPixels(reverseMix, 0, colorBar.getWidth(),
                        0, 0, colorBar.getWidth(), colorBar.getHeight());
                break;
        }
    }

    @Override
    public PseudoColor getPseudoColor() {
        if(colors == grayColors){
            return PseudoColor.NORMAL;
        }
        if(colors == redColors){
            return PseudoColor.RED;
        }
        if(colors == yellowColors){
            return PseudoColor.YELLOW;
        }
        if(colors == mixColors){
            return PseudoColor.MIX;
        }
        return PseudoColor.NORMAL;
    }


}

