package com.littlezheng.newultrasound.core;

/**
 * Created by Administrator on 2017/11/11/011.
 */

public final class Depth extends Param {

    private static final Depth INSTANCE = new Depth();

    public Depth() {
        super(13, 19, 0, 1);
    }

    public static Depth getInstance() {
        return INSTANCE;
    }

    public static int value() {
        return INSTANCE.getValue();
    }

}
