package com.littlezheng.ultrasound4.ultrasound.component;


/**
 * Created by Administrator on 2017/8/31/031.
 */

public enum Mode {

    MODE_B("B", false, true),
    MODE_M("M", false, false),
    MODE_BB("B/B", true, true),
    MODE_BM("B/M", true, true),
    MODE_NONE("null", false, false);

    private String name;
    private boolean changeable;    //是否可转换模式
    private boolean bMode;   //是否使用左侧模式

    Mode(String name, boolean changeable, boolean bMode) {
        this.name = name;
        this.changeable = changeable;
        this.bMode = bMode;
    }

    public void change() {
        if (changeable) {
            bMode = !bMode;
        }
    }

    /**
     * 判断当前是否在左模式
     *
     * @return
     */
    public boolean isBMode() {
        return bMode;
    }

    @Override
    public String toString() {
        return name;
    }

}
