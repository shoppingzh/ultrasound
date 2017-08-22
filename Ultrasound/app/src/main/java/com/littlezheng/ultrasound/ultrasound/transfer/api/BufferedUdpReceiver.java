package com.littlezheng.ultrasound.ultrasound.transfer.api;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by zxp on 2017/8/9.
 */

public class BufferedUdpReceiver implements UdpReceiver {

    private static final String TAG = "BufferedUdpReceiver";

    private final int port;
    private DatagramSocket socket;
    private final UdpPacketQueue queue;

    private DatagramPacket packet;
    private byte[] buf;
    //用于清空画出缓冲区的空数组
    private byte[] clearBuf;

    private boolean state = false;

    public BufferedUdpReceiver(int port,int bufferCapacity,int packetSize) throws SocketException {
        this.port = port;
        socket = new DatagramSocket(port);
        queue = new UdpPacketQueue(bufferCapacity);
        buf = new byte[packetSize];
        clearBuf = buf.clone();
        packet = new DatagramPacket(buf,packetSize);
    }

    public void start(){
        state = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(state){
                        socket.receive(packet);
                        //Log.d(TAG,"接收到数据："+ Arrays.toString(packet.getData()));
                        queue.put(packet.getData());
                        System.arraycopy(clearBuf,0,buf,0,buf.length);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop(){
        state = false;
    }

    public void stop(boolean clearBuffer){
        stop();
        if(clearBuffer) queue.clear();
    }

    /**
     * 接收数据，不要在主线程调用！
     * 该方法使用阻塞式缓冲队列，可能引起线程阻塞
     * @return
     * @throws InterruptedException
     */
    @Override
    public byte[] receive() throws InterruptedException {
        return queue.take();
    }

}
