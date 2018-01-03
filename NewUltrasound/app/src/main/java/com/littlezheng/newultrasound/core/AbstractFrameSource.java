package com.littlezheng.newultrasound.core;

/**
 * Created by Administrator on 2017/11/11/011.
 */

public abstract class AbstractFrameSource implements FrameSource {

    protected int width;
    protected int height;

    public AbstractFrameSource(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void process() {
        throw new UnsupportedOperationException();
    }

}
