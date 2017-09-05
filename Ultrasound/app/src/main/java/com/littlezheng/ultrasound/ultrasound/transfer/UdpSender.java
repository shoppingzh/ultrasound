package com.littlezheng.ultrasound.ultrasound.transfer;

import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by zxp on 2017/8/8.
 */

public interface UdpSender extends Observer{

    /**
     * 发送数据
     * @param content 数据内容的字节数组形式
     * @param addr 发送到的地址
     * @param port 发送到的端口
     */
    void send(byte[] content, InetAddress addr, int port);

    /**
     * 发送数据
     * @param content 数据内容的字节数组形式
     * @param addr 发送到的地址
     * @param port 发送到的端口
     */
    void send(String content, InetAddress addr, int port);

    /**
     * 发送数据
     * @param content
     * @param addr
     * @param port
     */
    void send(byte[] content, String addr, int port);

    /**
     * 发送数据
     * @param content
     * @param addr
     * @param port
     */
    void send(String content, String addr, int port);

    /**
     * 发送数据
     * @param content
     */
    void send(byte[] content);

    /**
     * 发送数据
     * @param content
     */
    void send(String content);


}
