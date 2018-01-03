package com.littlezheng.newultrasound.core;

/**
 * Created by Administrator on 2017/11/11/011.
 */

public abstract class ArrayFrameSource extends AbstractFrameSource {

    protected byte[][] data;
    protected int cursor = 0;

    public ArrayFrameSource(int width, int height) {
        super(width, height);
        data = new byte[width][height];
    }

    private void checkFull() {
        if (cursor >= width) cursor = 0;
    }

    @Override
    public void put(byte[] lineData) {
        checkFull();
        System.arraycopy(lineData, 0, data[cursor++], 0, lineData.length);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= width) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void put(int num, byte[] lineData) {
        //checkIndex(num);
        cursor = num;
        put(lineData);
    }

    protected boolean full() {
        return cursor >= width;
    }

}
