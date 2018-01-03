package com.littlezheng.ultrasound3.ultrasound.transmission.protocol;


import com.littlezheng.ultrasound3.ultrasound.base.Param;

import java.util.Observable;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class ParamProtocol extends Protocol {

    public ParamProtocol(int controlCode, Observable trigger) {
        super(controlCode, trigger);
    }

    @Override
    public void update(Observable o, Object arg) {
        //该方法被调用，说明参数发生了改变
        //参数发生改变时，可通知协议的观察者们，让UDP传输器将改变后的数据发出
        setData(((Param) o).getCurrValue());
        changed();
    }

}
