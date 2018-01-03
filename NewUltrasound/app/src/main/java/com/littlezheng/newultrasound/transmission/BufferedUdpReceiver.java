package com.littlezheng.newultrasound.transmission;

import java.io.IOException;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/11/9/009.
 */

public class BufferedUdpReceiver extends AbstractUdpReceiver implements ContinuousReceiver {

    private BlockingQueue<byte[]> packetBuffer;
    private boolean flag;

    public BufferedUdpReceiver(int port, int packetSize, int bufferSize) throws SocketException {
        super(port);
        buf = new byte[packetSize];
        packetBuffer = new LinkedBlockingQueue<>(bufferSize);
        new TakeThread().start();
    }

    @Override
    public void receive() throws IOException {
        super.receive();
        notifyListeners(buf);
    }

    @Override
    public void start() {
        if (!flag) {
            flag = true;
            new PutThread().start();
        }
    }

    @Override
    public void stop() {
        flag = false;
        //停止接收器的同时清空缓冲区的内容
        packetBuffer.clear();
    }

    @Override
    public boolean isReceiving() {
        return flag;
    }

    /**
     * 从缓冲队列中取出数据的线程
     */
    private class TakeThread extends Thread {

        public TakeThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    notifyListeners(packetBuffer.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //no op
                }
            }
        }
    }

    /**
     * 将接收到的数据放入缓冲区的线程
     */
    private class PutThread extends Thread {
        @Override
        public void run() {
            while (flag) {
                try {
                    BufferedUdpReceiver.super.receive();
                    packetBuffer.put(Arrays.copyOf(buf, buf.length));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
