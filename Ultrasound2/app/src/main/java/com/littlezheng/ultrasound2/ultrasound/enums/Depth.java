package com.littlezheng.ultrasound2.ultrasound.enums;

import java.net.PortUnreachableException;

/**
 * Created by zxp on 2017/8/3.
 */

public enum Depth {

    DEPTH_30(30),
    DEPTH_40(40),
    DEPTH_50(50),
    DEPTH_60(60),
    DEPTH_70(70),
    DEPTH_80(80),
    DEPTH_90(90),
    DEPTH_100(100),
    DEPTH_110(110),
    DEPTH_120(120),
    DEPTH_130(130),
    DEPTH_140(140),
    DEPTH_150(150),
    DEPTH_160(160),
    DEPTH_170(170),
    DEPTH_180(180),
    DEPTH_190(190),
    DEPTH_200(200),
    DEPTH_210(210),
    DEPTH_220(220);

    private int depth;

    private Depth(int depth){
        this.depth = depth;
    }

    public static int indexOf(Depth depth){
        return (depth.get() - 30)/10;
    }

    public int get(){
        return depth;
    }

}
