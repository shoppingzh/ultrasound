package com.littlezheng.ultrasound3.ultrasound.process;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/8/28/028.
 */

class StorableFrame implements Serializable {

    private static final long serialVersionUID = 409832798100291397L;

    byte[][] data;
    int depth;
    int[] colors;

    public StorableFrame(byte[][] data, int depth, int[] colors) {
        this.data = data;
        this.depth = depth;
        this.colors = colors;
    }

    @Override
    public String toString() {
        return "StorableFrame{" +
                "data=" + Arrays.toString(data) +
                ", depth=" + depth +
                ", colors=" + Arrays.toString(colors) +
                '}';
    }

}
