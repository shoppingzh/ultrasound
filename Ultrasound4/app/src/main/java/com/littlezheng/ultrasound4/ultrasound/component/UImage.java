package com.littlezheng.ultrasound4.ultrasound.component;

/**
 * Created by Administrator on 2017/9/23/023.
 */

public class UImage {

    private int[] pixels;
    private int depth;
    private int backFat;

    public UImage(int[] pixels) {
        this.pixels = pixels;
    }

    public UImage(int[] pixels, int depth) {
        this.pixels = pixels;
        this.depth = depth;
    }


    public int[] getPixels() {
        return pixels;
    }

    public void setPixels(int[] pixels) {
        this.pixels = pixels;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getBackFat() {
        return backFat;
    }

    public void setBackFat(int backFat) {
        this.backFat = backFat;
    }
}
