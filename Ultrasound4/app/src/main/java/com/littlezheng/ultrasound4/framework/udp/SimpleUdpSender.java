package com.littlezheng.ultrasound4.framework.udp;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/8/30/030.
 */

public class SimpleUdpSender implements UdpSender {

    public static final int MESSAGE_QUEUE_CAPACITY = 100;

    private static final String TAG = "SimpleUdpSender";

    private final DatagramSocket socket;
    private final BlockingQueue<DatagramPacket> messageQueue;   //消息队列

    private InetAddress dstAddress;     //发送目的地ip
    private int dstPort;     //发送目的地端口

    public SimpleUdpSender(InetAddress dstAddress, int dstPort) throws SocketException {
        this.dstAddress = dstAddress;
        this.dstPort = dstPort;
        socket = new DatagramSocket();
        messageQueue = new LinkedBlockingQueue<>(MESSAGE_QUEUE_CAPACITY);
        startWorkThread();
    }

    /**
     * 开启发送工作线程，该线程从消息队列中取出要发送的包并发送出去
     */
    private void startWorkThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        DatagramPacket packet = messageQueue.take();
                        socket.send(packet);
                        Log.d(TAG, "发送数据：" + Arrays.toString(packet.getData()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO
                }
            }
        }).start();
    }

    @Override
    public boolean send(byte[] data) {
        DatagramPacket p = new DatagramPacket(data, data.length, dstAddress, dstPort);
        //为什么用offer而不用put？
        //offer添加元素时，当队列满了的时候会直接返回false；而put则会阻塞当前线程直到队列不再满
        //为了不导致其它线程阻塞，需使用offer方法
        return messageQueue.offer(p);
    }

    /**
     * 重置发送目的地
     *
     * @param newAddress 新目的地地址
     * @param newPort    新目的地端口
     */
    public void resetDestination(InetAddress newAddress, int newPort) {
        dstAddress = newAddress;
        dstPort = newPort;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public InetAddress getDstAddress() {
        return dstAddress;
    }

    public int getDstPort() {
        return dstPort;
    }
}
