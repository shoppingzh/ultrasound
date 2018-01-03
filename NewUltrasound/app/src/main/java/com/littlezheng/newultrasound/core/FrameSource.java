package com.littlezheng.newultrasound.core;

/**
 * 帧源：帧源是一个数据帧的原始数据，对该数据进行处理可以生成一个完整的帧，该帧对应了
 * 一个像素数组，可以填充到位图（Bitmap）进行显示。
 * 帧源的数据不是一次性给予的，而是通过网络或者其它形式，一条线一条线地放入。
 * put()方法可以将线数据放入到帧源中，线数据是一个byte数组。
 * put()方法还提供了一种可按照线id进行放入数据的手段，这适用于有id的帧源数据
 * <p>
 * Created by Administrator on 2017/11/11/011.
 */

public interface FrameSource {

    /**
     * 向帧源数据中放入一条线的数据
     *
     * @param lineData 线数据
     */
    void put(byte[] lineData);

    /**
     * 向帧源的指定位置放置一条线的数据，这个位置一般而言是线数据的id
     *
     * @param num      线id
     * @param lineData 线数据
     */
    void put(int num, byte[] lineData);

    /**
     * 对帧源数据进行处理
     */
    void process();

}
