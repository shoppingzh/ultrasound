package com.littlezheng.ultrasound4.framework.udp;

/**
 * Created by Administrator on 2017/8/30/030.
 */

public interface UdpSender {

    /**
     * 发送数据
     *
     * @param data
     */
    boolean send(byte[] data);


}
