package com.littlezheng.ultrasound.ultrasound.transfer.api;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zxp on 2017/8/8.
 */

public class UdpPacketQueue {

    private static final String TAG = "UdpPacketQueue";

    private final LinkedBlockingQueue<byte[]> queue;

    public UdpPacketQueue(){
        this.queue = new LinkedBlockingQueue<>();
    }

    public UdpPacketQueue(int capacity){
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    /**
     * 向缓冲队列中放入一个数据包
     * @param data
     * @throws InterruptedException
     */
    public void put(byte[] data) throws InterruptedException {
        put(data,data.length);
    }

    /**
     * 向缓冲队列中放入一个数据包的len位
     * @param data
     * @param len
     * @throws InterruptedException
     */
    public void put(byte[] data,int len) throws InterruptedException {
        byte[] copyPack = new byte[len];
        System.arraycopy(data,0,copyPack,0,len);
        queue.put(copyPack);
//        Log.d(TAG,"UDP缓冲队列放入数据："+ Arrays.toString(data));
    }

    /**
     * 从缓冲队列中获取一个数据包
     * @return
     * @throws InterruptedException
     */
    public byte[] take() throws InterruptedException {
        return queue.take();
    }

    /**
     * 获取当前队列的大小
     * @return
     */
    public int size(){
        return queue.size();
    }

    /**
     * 清空缓冲队列
     */
    public void clear(){
        queue.clear();
        Log.d(TAG,"清空缓冲队列数据！");
    }

}
