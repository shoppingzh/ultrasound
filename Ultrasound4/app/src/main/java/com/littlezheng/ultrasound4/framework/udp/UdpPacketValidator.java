package com.littlezheng.ultrasound4.framework.udp;

/**
 * Created by zxp on 2017/5/25.
 */

public abstract class UdpPacketValidator {

    /**
     * UDP数据包校验
     *
     * @param packetData
     * @return
     */
    public abstract boolean validate(byte[] packetData);

}