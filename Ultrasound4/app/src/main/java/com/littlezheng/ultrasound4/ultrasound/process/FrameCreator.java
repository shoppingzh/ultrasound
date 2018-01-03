package com.littlezheng.ultrasound4.ultrasound.process;

import com.littlezheng.ultrasound4.ultrasound.transfer.UdpTransceiver;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/24/024.
 */

public abstract class FrameCreator extends Observable implements Observer {

    private final UdpTransceiver udpTransceiver;

    private Thread workThread;
    private boolean enabled;

    public FrameCreator(UdpTransceiver udpTransceiver) {
        this.udpTransceiver = udpTransceiver;
    }

    /**
     * 开启处理
     */
    public final void enable() {
        if (enabled) return;
        enabled = true;

        workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                init();
                while (enabled) {
                    byte[] data = udpTransceiver.receive();
                    if (!validate(data)) continue;
                    onReceiveData(data);
                }
            }
        });
        workThread.start();
    }

    /**
     * 校验数据
     *
     * @param data 接收到的数据
     * @return
     */
    protected boolean validate(byte[] data) {
        return true;
    }

    /**
     * 关闭处理
     */
    public final void disable() {
        enabled = false;
    }

    /**
     * 是否正在处理中
     *
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 启动线程前做的初始化工作
     */
    protected abstract void init();

    /**
     * 接收到数据时的回调
     *
     * @param data 接收数据
     */
    protected abstract void onReceiveData(byte[] data);

}
