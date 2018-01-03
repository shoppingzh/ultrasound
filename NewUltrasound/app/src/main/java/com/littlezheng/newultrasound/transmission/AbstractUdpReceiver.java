package com.littlezheng.newultrasound.transmission;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by Administrator on 2017/11/9/009.
 */

public abstract class AbstractUdpReceiver extends AbstractReceiver {

    protected DatagramSocket socket;
    protected byte[] buf;

    public AbstractUdpReceiver(int port) throws SocketException {
        socket = new DatagramSocket(null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(port));
        buf = new byte[0];
    }

    @Override
    public void receive() throws IOException {
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        socket.receive(p);
    }

    public int getPacketSize() {
        return buf.length;
    }

    public void setPacketSize(int packetSize) {
        buf = new byte[packetSize];
    }

}
