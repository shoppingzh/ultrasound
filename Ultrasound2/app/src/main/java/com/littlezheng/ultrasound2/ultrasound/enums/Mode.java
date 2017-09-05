package com.littlezheng.ultrasound2.ultrasound.enums;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public enum Mode {

    MODE_B("B"),
    MODE_M("M"),
    MODE_BB("B/B"),
    MODE_BM("B/M");

    private String name;

    private Mode(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
