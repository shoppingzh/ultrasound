package com.littlezheng.ultrasound.ultrasound.transfer.data;

/**
 * Created by zxp on 2017/8/9.
 */

public class RunController extends DataReturner {

    private boolean freeze = true;

    public RunController(int controlCode) {
        super(controlCode);
    }

    public void freeze(){
        freeze = true;
        setData((byte) 0);
    }

    public void unFreeze(){
        freeze = false;
        setData((byte) 0xFF);
    }

    public boolean isFreeze() {
        return freeze;
    }

}
