package com.littlezheng.newultrasound.transmission;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Administrator on 2017/11/12/012.
 */

public abstract class AbstractUdpSender implements Sender {

    protected DatagramSocket socket;

    public AbstractUdpSender() throws SocketException {
        socket = new DatagramSocket(null);
    }

    public void send(byte[] data, InetAddress addr, int port) throws IOException {
        socket.send(new DatagramPacket(data, data.length, addr, port));
    }

}
