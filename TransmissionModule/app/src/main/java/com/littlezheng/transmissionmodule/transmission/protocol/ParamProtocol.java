package com.littlezheng.transmissionmodule.transmission.protocol;

import com.littlezheng.transmissionmodule.component.ParamController;

import java.util.Observable;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class ParamProtocol extends Protocol{

    private final ParamController paramController;

    public ParamProtocol(int controlCode, ParamController paramController) {
        super(controlCode);
        this.paramController = paramController;
        paramController.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        //该方法被调用，说明参数发生了改变
        //参数发生改变时，可通知协议的观察者们，让UDP传输器将改变后的数据发出
        setData(((ParamController)o).getCurrValue());
        paramChanged();
    }

    /**
     * 参数变化
     */
    public void paramChanged(){
        setChanged();
        //通知UDP传输器，将协议内容推送给UDP传输器
        notifyObservers(getProtocol());
    }

}
