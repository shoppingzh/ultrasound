package com.littlezheng.ultrasound3.ultrasound.base;

import java.util.Observable;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class Param extends Observable{

    //参数名、默认值、最大值、最小值、增量
    private String name;
    private int defaultValue;
    private int maxValue;
    private int minValue;
    private int increment;

    private int currValue;

    public Param(String name, int defaultValue, int maxValue,
                           int minValue, int increment){
        this.name = name;
        this.defaultValue = defaultValue;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.increment = increment;
        this.currValue = defaultValue;
    }

    /**
     * 增加
     *
     * @return
     */
    public boolean increase(){
        if(currValue >= maxValue) return false;
        currValue += increment;
        paramChanged();
        return true;
    }

    /**
     * 减少
     *
     * @return
     */
    public boolean decrease(){
        if(currValue <= minValue) return false;
        currValue -= increment;
        paramChanged();
        return true;
    }

    /**
     * 重置为默认值
     */
    public void reset(){
        currValue = defaultValue;
        paramChanged();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getIncrement() {
        return increment;
    }

    public int getCurrValue() {
        return currValue;
    }

    /**
     * 参数改变，通知观察者
     */
    private void paramChanged(){
        setChanged();
        notifyObservers();
    }

    /**
     * 描述
     * @return 如 深度: 160mm
     */
    public String desc(){
        return name + ": " +currValue;
    }


}
