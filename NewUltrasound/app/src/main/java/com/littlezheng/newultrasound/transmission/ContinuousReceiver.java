package com.littlezheng.newultrasound.transmission;

/**
 * Created by Administrator on 2017/11/9/009.
 * <p>
 * 具有连续接收功能的接收器，具备开启/关闭以及判断运行状态等功能
 */

public interface ContinuousReceiver extends Receiver {

    /**
     * 开始接收
     */
    void start();

    /**
     * 停止接收
     */
    void stop();

    /**
     * @return 是否正在接收
     */
    boolean isReceiving();

}
