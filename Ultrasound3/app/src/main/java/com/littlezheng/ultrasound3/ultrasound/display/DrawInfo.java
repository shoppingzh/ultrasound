package com.littlezheng.ultrasound3.ultrasound.display;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by zxp on 2017/8/9.
 */

public class DrawInfo {

    public int thirdSamWid, thirdSamHei;
    public Rect src;
    public RectF dst;

    public DrawInfo(int thirdSamWid, int thirdSamHei, Rect src, Rect dst) {
        this.thirdSamWid = thirdSamWid;
        this.thirdSamHei = thirdSamHei;
        this.src = src;
        this.dst = new RectF(dst);
    }

    public DrawInfo(int thirdSamWid, int thirdSamHei, Rect src, RectF dst) {
        this.thirdSamWid = thirdSamWid;
        this.thirdSamHei = thirdSamHei;
        this.src = src;
        this.dst = dst;
    }

    @Override
    public String toString() {
        return "DrawInfo{" +
                "thirdSamWid=" + thirdSamWid +
                ", thirdSamHei=" + thirdSamHei +
                ", src=" + src +
                ", dst=" + dst +
                '}';
    }

}
