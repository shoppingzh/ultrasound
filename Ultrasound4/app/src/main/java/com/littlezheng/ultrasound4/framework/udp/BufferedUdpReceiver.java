package com.littlezheng.ultrasound4.framework.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/8/30/030.
 */

public class BufferedUdpReceiver implements UdpReceiver {

    private static final String TAG = "BufferedUdpReceiver";

    private final DatagramSocket socket;
    private final int packetSize;   //报文大小
    private final int port;

    private final BlockingQueue<byte[]> buffer;     //接收缓冲队列
    private final int bufferSize;   //缓冲队列容量

    private boolean enabled;
    private boolean clearBuffer;

    public BufferedUdpReceiver(int port, int packetSize, int bufferSize) throws SocketException {
        this.port = port;
        this.packetSize = packetSize;
        this.bufferSize = bufferSize;
        //设置端口可复用，可解决端口冲突问题导致应用闪退问题
        socket = new DatagramSocket(null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(port));
        socket.setReceiveBufferSize(212992);
        buffer = new LinkedBlockingQueue(bufferSize);

        enable();
    }

    /**
     * 默认缓冲100个数据包
     *
     * @param port
     * @param packetSize
     * @throws SocketException
     */
    public BufferedUdpReceiver(int port, int packetSize) throws SocketException {
        this(port, packetSize, 100);
    }

    /**
     * 该方法可能会引起线程阻塞，因为take方法是阻塞方法，请不要在主线程中直接调用该方法
     *
     * @return
     */
    @Override
    public byte[] receive() {
        try {
            return buffer.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 开启接收
     */
    public void enable() {
        if (enabled) return;
        enabled = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] buf = new byte[packetSize];
                byte[] empty = new byte[packetSize];
                DatagramPacket p = new DatagramPacket(buf, packetSize);
                try {
                    while (enabled) {
                        socket.receive(p);
                        byte[] oData = p.getData();
                        byte[] cData = new byte[packetSize];
                        System.arraycopy(oData, 0, cData, 0, packetSize);
                        buffer.put(cData);
//                        Log.d(TAG,"接收到数据："+ Arrays.toString(cData));
                        System.arraycopy(empty, 0, buf, 0, packetSize);
                    }

                    //代码运行到此处，说明接收已经关闭
                    if (clearBuffer) buffer.clear();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 关闭接收
     *
     * @param clearBuffer 是否同时清除缓冲区
     */
    public void disable(boolean clearBuffer) {
        this.clearBuffer = clearBuffer;
        enabled = false;
    }

    public int getPort() {
        return port;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
