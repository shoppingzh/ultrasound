package com.littlezheng.newultrasound.transmission;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/11/12/012.
 */

public final class UdpSender implements Sender {

    private static final UdpSender sender = new UdpSender();

    public static UdpSender getInstance(){
        return sender;
    }

    public static void init(String remoteIp, int remotePort){
        sender.setDestination(remoteIp, remotePort);
    }

    public static void sendData(byte[] data){
        sender.send(data);
    }

    public static void sendData(byte[] data, String remoteIp, int remotePort){
        init(remoteIp, remotePort);
        sendData(data);
    }

    private DatagramSocket socket;
    private final BlockingQueue<DatagramPacket> messages =
            new LinkedBlockingQueue<>(20);
    private InetAddress remoteAddress;
    private int remotePort;

    private UdpSender() {
        try {
            socket = new DatagramSocket(null);
        } catch (SocketException e) {
            e.printStackTrace();
            throw new RuntimeException("套接字创建错误！" + e);
        }
        new SenderThread().start();
    }

    public void setDestination(String remoteIp, int remotePort){
        try {
            sender.remoteAddress = InetAddress.getByName(remoteIp);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        sender.remotePort = remotePort;
    }

    public void send(byte[] data) {
        messages.offer(new DatagramPacket(data, data.length, remoteAddress, remotePort));
    }

    private class SenderThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    DatagramPacket packet = messages.take();
                    socket.send(packet);
                    Log.d(TAG, "发送数据：" + Arrays.toString(packet.getData()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                //TODO
            }
        }
    }


}
