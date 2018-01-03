package com.littlezheng.ultrasound4.ultrasound.transfer;

import com.littlezheng.ultrasound4.framework.udp.BufferedUdpReceiver;
import com.littlezheng.ultrasound4.framework.udp.SimpleUdpSender;
import com.littlezheng.ultrasound4.framework.udp.UdpSender;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

/**
 * Created by Administrator on 2017/9/22/022.
 */

public class UdpTransceiver implements Observer {

    private final UdpSender udpSender;
    private final BufferedUdpReceiver udpReceiver;

    /**
     * UDP传输器
     *
     * @param config 需对Config中所有属性进行合理初始化
     */
    public UdpTransceiver(Config config) throws SocketException {
        if (!(config.receivePort >= 1 && config.receivePort <= 65536))
            throw new IllegalArgumentException("接收端口错误！");
        if (config.receivePackSize <= 0)
            throw new IllegalArgumentException("数据包大小必须大于0！");
        if (config.receiveBufSize <= 0)
            throw new IllegalArgumentException("缓冲区大小错误！");
        if (!(config.sendPort >= 1 && config.sendPort <= 65536))
            throw new IllegalArgumentException("发送端口错误！");
        udpSender = new SimpleUdpSender(config.sendAddress, config.sendPort);
        udpReceiver = new BufferedUdpReceiver(config.receivePort, config.receivePackSize, config.receiveBufSize);
    }

    /**
     * 发送数据
     *
     * @param data
     * @return
     */
    public boolean send(byte[] data) {
        return udpSender.send(data);
    }

    /**
     * 接收数据
     *
     * @return
     */
    public byte[] receive() {
        return udpReceiver.receive();
    }

    /**
     * 启用接收
     */
    public void enableReceive() {
        udpReceiver.enable();
    }

    /**
     * 禁用接收
     *
     * @param clearBuffer
     */
    public void disableReceive(boolean clearBuffer) {
        udpReceiver.disable(clearBuffer);
    }

    /**
     * 增加协议，协议内容一旦改变，将通知传输器将更新后的协议内容发送出去
     *
     * @param protocol
     */
    public void addProtocol(Protocol protocol) {
        protocol.addObserver(this);
    }

    /**
     * 删除协议，协议内容更新将不作出反应
     *
     * @param protocol
     */
    public void deleteProtocol(Protocol protocol) {
        protocol.deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof Protocol)) return;
        send(((Protocol) o).getProtocol());
    }

    public UdpSender getUdpSender() {
        return udpSender;
    }

    public BufferedUdpReceiver getUdpReceiver() {
        return udpReceiver;
    }

    /**
     * 初始化传输器需要的配置信息
     */
    public static class Config {
        public int receivePort;
        public int receivePackSize;
        public int receiveBufSize;

        public InetAddress sendAddress;
        public int sendPort;

        public void loadFromStream(InputStream in) {
            Properties prop = new Properties();
            try {
                prop.load(in);

                receivePort = Integer.valueOf(prop.getProperty("receivePort"));
                receivePackSize = Integer.valueOf(prop.getProperty("receivePackSize"));
                receiveBufSize = Integer.valueOf(prop.getProperty("receiveBufSize"));

                sendAddress = InetAddress.getByName(prop.getProperty("sendAddress"));
                sendPort = Integer.valueOf(prop.getProperty("sendPort"));
            } catch (IOException e) {
                throw new RuntimeException("配置信息加载失败！");
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        //do nothing
                    }
                }
            }
        }

    }


}
