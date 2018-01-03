package com.littlezheng.newultrasound.transmission;

/**
 * Created by Administrator on 2017/11/18/018.
 */

public interface PacketValidator {

    /**
     * 校验
     *
     * @param data 数据包
     * @return 校验结果，true为校验成功，否则校验失败
     */
    boolean validate(byte[] data);

}
