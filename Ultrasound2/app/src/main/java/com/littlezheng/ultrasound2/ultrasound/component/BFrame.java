package com.littlezheng.ultrasound2.ultrasound.component;

/**
 * Created by Administrator on 2017/8/11/011.
 */

public class BFrame extends Frame {

    private int pos = 0;

    public BFrame(int width, int height, boolean horizontal) {
        super(width, height, horizontal);
    }

    @Override
    public void put(int num, byte[] entity, int srcPos, int dstPos, int len) {
        pos = num;
        System.arraycopy(entity, srcPos, data[pos++], dstPos, len);
    }

    @Override
    public void put(byte[] entity, int srcPos, int dstPos, int len) {
        System.arraycopy(entity, srcPos, data[pos++], dstPos, len);
    }

    @Override
    public void put(byte[] entity, int len) {
        System.arraycopy(entity, 0, data[pos++], 0, len);
    }

    @Override
    public void put(byte[] entity) {
        System.arraycopy(entity, 0, data[pos++], 0, entity.length);
    }

    @Override
    public void clear() {
        pos = 0;
    }

    @Override
    public boolean full() {
        return (pos >= width);
    }

}
