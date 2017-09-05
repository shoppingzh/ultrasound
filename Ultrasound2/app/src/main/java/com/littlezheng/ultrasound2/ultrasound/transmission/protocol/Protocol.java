package com.littlezheng.ultrasound2.ultrasound.transmission.protocol;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by zxp on 2017/8/9.
 */

public abstract class Protocol extends Observable implements Observer {

    //返回数据的通讯协议，第三个字节表控制字，第四个字节表参数值
    private byte[] protocol = new byte[]{
            0x55, (byte)0xAA,
            0, 0,
            (byte)0xAA, 0x55
    };

    public Protocol(int controlCode){
        protocol[2] = (byte) controlCode;
    }

    public byte getControlCode() {
        return protocol[2];
    }

    public void setControlCode(int controlCode) {
        protocol[2] = (byte)controlCode;
    }

    public byte getData() {
        return protocol[3];
    }

    protected void setData(int data) {
        protocol[3] = (byte) data;
    }

    public byte[] getProtocol(){
        return protocol.clone();
    }

}
