package com.littlezheng.newultrasound.core;

/**
 * Created by Administrator on 2017/11/17/017.
 */

public interface FrameGenerator {

    /**
     * 放入源数据，这是帧生成器的数据来源
     *
     * @param data
     */
    void putData(byte[] data);

    /**
     * 设置生成的帧的类型
     *
     * @param frameType
     */
    void setFrameType(FrameType frameType);

    /**
     * 获取当前生成帧的类型
     *
     * @return
     */
    FrameType getFrameType();

    /**
     * 设置帧处理伪彩色
     *
     * @param pseudoColor
     */
    void setPseudoColor(PseudoColor pseudoColor);

    /**
     * 获取帧处理伪彩色
     *
     * @return
     */
    PseudoColor getPseudoColor();

    enum FrameType {
        B_FRAME,
        M_FRAME
    }

    enum PseudoColor {
        NORMAL,
        RED,
        YELLOW,
        MIX
    }

}
