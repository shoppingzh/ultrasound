package com.littlezheng.ultrasound.ultrasound.transfer.api;

import android.util.Log;

import com.littlezheng.ultrasound.ultrasound.transfer.data.DataReturner;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zxp on 2017/8/8.
 */

public class AsynUdpSender implements UdpSender {

    private static final String TAG = "AsynUdpSender";

    private final DatagramSocket socket;

    //消息队列
    private final BlockingQueue<DatagramPacket> messageQueue = new LinkedBlockingQueue(10);

    private InetAddress defaultAddr;
    private int defaultPort;
    private boolean defaultInitialized = false;

    public AsynUdpSender() throws SocketException {
        socket = new DatagramSocket();
        startSendThread();
    }

    /**
     * 使用一个已有的socket套接字实例化发送器
     * @param socket
     */
    public AsynUdpSender(DatagramSocket socket){
        this.socket = socket;
        startSendThread();
    }

    public AsynUdpSender(InetAddress defaultAddr,int defaultPort) throws SocketException {
        socket = new DatagramSocket();
        this.defaultAddr = defaultAddr;
        this.defaultPort = defaultPort;
        defaultInitialized = true;
        startSendThread();
    }

    public AsynUdpSender(DatagramSocket socket,InetAddress defaultAddr,int defaultPort){
        this.socket = socket;
        this.defaultAddr = defaultAddr;
        this.defaultPort = defaultPort;
        defaultInitialized = true;
        startSendThread();
    }

    /**
     * 开启发送线程，消息队列一旦有数据，直接将其发送出去，否则线程阻塞
     */
    private void startSendThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        DatagramPacket packet = messageQueue.take();
                        socket.send(packet);
                        Log.d(TAG,"发送数据："+ Arrays.toString(packet.getData()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void send(byte[] content, InetAddress addr, int port) {
        if(content == null || content.length <= 0) return;
        DatagramPacket packet = new DatagramPacket(content,content.length,addr,port);
        messageQueue.offer(packet);
    }

    @Override
    public void send(String content, InetAddress addr, int port) {
        if(content == null) return;
        send(content.getBytes(),addr,port);
    }

    @Override
    public void send(byte[] content, String addr, int port) {
        try {
            send(content,InetAddress.getByName(addr),port);
        } catch (UnknownHostException e) {
            //将异常捕捉，ip转换错误的后果由用户自己承担
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void send(String content, String addr, int port) {
        try {
            send(content,InetAddress.getByName(addr),port);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void send(byte[] content) {
        if(!defaultInitialized) return;
        send(content,defaultAddr,defaultPort);
    }

    @Override
    public void send(String content) {
        if(!defaultInitialized) return;
        send(content,defaultAddr,defaultPort);
    }

    @Override
    public void addTrigger(Observable trigger) {
        trigger.addObserver(this);
    }

    @Override
    public void removeTrigger(Observable trigger) {
        trigger.deleteObserver(this);
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    @Override
    public void update(Observable o, Object arg) {
        DataReturner returner = (DataReturner)o;
        send(returner.getProtocol());
    }

}
