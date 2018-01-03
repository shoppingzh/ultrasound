package com.littlezheng.ultrasound4.ultrasound.process;

/**
 * Created by Administrator on 2017/8/11/011.
 */

public class MFrame extends Frame {

    private static final String TAG = "MFrame";

    private int pos = 0;

    public MFrame(int width, int height, boolean horizontal) {
        super(width, height, horizontal);
    }

    @Override
    public void put(int num, byte[] entity, int srcPos, int dstPos, int len) {
        //no op
    }

    @Override
    public void put(byte[] entity, int srcPos, int dstPos, int len) {
        returnToZeroIfFull();
        System.arraycopy(entity, srcPos, data[pos++], dstPos, len);
//        Log.d(TAG,"线数据："+ Arrays.toString(data[pos-1]));
    }

    @Override
    public void put(byte[] entity, int len) {
        //no op
    }

    @Override
    public void put(byte[] entity) {
        returnToZeroIfFull();
        System.arraycopy(entity, 0, data[pos++], 0, entity.length);
    }

    private void returnToZeroIfFull() {
        if (pos >= width) {
            pos = 0;
        }
    }

    @Override
    public void clear() {
        // no op
    }

    @Override
    public boolean full() {
        return false;
    }

    public int getPos() {
        return pos;
    }
}
