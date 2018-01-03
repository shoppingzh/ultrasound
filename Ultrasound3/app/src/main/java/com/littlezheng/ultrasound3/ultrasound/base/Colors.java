package com.littlezheng.ultrasound3.ultrasound.base;

import android.graphics.Color;

import java.util.Observable;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class Colors extends Observable {

    private PseudoColor color;

    //颜色信息
    private final int[] grayColors = new int[256];
    private final int[] redColors = new int[256];
    private final int[] yellowColors = new int[256];
    private final int[] mixColors = new int[256];
    private int[] colors = grayColors;

    private final int[] reverseGray = new int[256];
    private final int[] reverseRed = new int[256];
    private final int[] reverseYellow = new int[256];
    private final int[] reverseMix = new int[256];

    public Colors() {
        initColorArrays();
    }

    /**
     * 初始化颜色数组（灰色+伪彩）
     */
    private void initColorArrays() {
        //初始化灰阶颜色数组
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

    /**
     * 改变颜色
     *
     * @param color
     */
    public void change(PseudoColor color) {
        this.color = color;
        switch (color) {
            case COLOR_NORMAL:
                colors = grayColors;
                break;
            case COLOR_RED:
                colors = redColors;
                break;
            case COLOR_YELLOW:
                colors = yellowColors;
                break;
            case COLOR_MIX:
                colors = mixColors;
                break;
        }
        setChanged();
        notifyObservers(0);
    }

    public int[] get() {
        return colors;
    }

    public int[] getReverse() {
        if (color == null) return reverseGray;
        int[] colors = null;
        switch (color) {
            case COLOR_NORMAL:
                colors = reverseGray;
                break;
            case COLOR_RED:
                colors = reverseRed;
                break;
            case COLOR_YELLOW:
                colors = reverseYellow;
                break;
            case COLOR_MIX:
                colors = reverseMix;
                break;
            default:
                colors = reverseGray;
                break;
        }

        return colors;
    }

    public PseudoColor getColor() {
        return color;
    }

    public enum PseudoColor {
        COLOR_NORMAL,
        COLOR_RED,
        COLOR_YELLOW,
        COLOR_MIX
    }

}
