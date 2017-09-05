package com.littlezheng.ultrasound.ultrasound.process;

import com.littlezheng.ultrasound.ultrasound.Depth;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/28/028.
 */

class StorableFrame implements Serializable{

    byte[][] data;
    Depth depth;

    public StorableFrame(byte[][] data, Depth depth){
        this.data = data;
        this.depth = depth;
    }

//    @Override
//    public String toString() {
//        return "StorableFrame{" +
//                "data=" + data +
//                ", depth=" + depth +
//                '}';
//    }
}
