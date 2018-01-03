package com.littlezheng.newultrasound.transmission;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2017/11/9/009.
 */

public abstract class AbstractReceiver implements Receiver {

    protected List<ReceiveListener> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void addListener(ReceiveListener listener) {
        if (listener != null && !listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void removeListener(ReceiveListener listener) {
        listeners.remove(listener);
    }

    /**
     * 通知所有监听器
     *
     * @param data 接收到的数据
     */
    protected void notifyListeners(byte[] data) {
        for (ReceiveListener listener : listeners) {
            listener.onReceive(data);
        }
    }

}
