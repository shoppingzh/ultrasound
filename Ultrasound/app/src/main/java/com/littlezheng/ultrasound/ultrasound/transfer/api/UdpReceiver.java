package com.littlezheng.ultrasound.ultrasound.transfer.api;

/**
 * Created by zxp on 2017/8/9.
 */

public interface UdpReceiver {

    /**
     * 接收数据
     * @return
     */
    byte[] receive() throws InterruptedException;

}
