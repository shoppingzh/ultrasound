package com.littlezheng.newultrasound.transmission;

import java.io.IOException;

/**
 * Created by Administrator on 2017/11/9/009.
 */

public interface Receiver {

    /**
     * 接收数据
     *
     * @throws IOException 网络错误等将导致该异常抛出
     */
    void receive() throws IOException;

    /**
     * 添加接收监听器，当接收到数据时将通知该监听器
     *
     * @param listener 监听器
     */
    void addListener(ReceiveListener listener);

    /**
     * 移除接收监听器，接收到数据时将不再通知该监听器
     *
     * @param listener
     */
    void removeListener(ReceiveListener listener);

    /**
     * 接收监听器
     */
    interface ReceiveListener {

        /**
         * 接收到数据时的回调方法，一般而言要先调用receive()方法直到接收到数据时
         * 该方法被回调
         *
         * @param data
         */
        void onReceive(byte[] data);

    }

}
