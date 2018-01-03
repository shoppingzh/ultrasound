package com.littlezheng.newultrasound.core;

import java.util.Observable;

/**
 * Created by Administrator on 2017/8/31/031.
 */

public class Param extends Observable {

    final int defaultValue;   //默认值
    final int maxValue;   //最大值
    final int minValue;   //最小值
    final int increment;  //增量
    String name;    //参数名
    int value;  //当前值

    public Param(int defaultValue, int maxValue,
                 int minValue, int increment) {
        this.defaultValue = defaultValue;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.increment = increment;
        this.value = defaultValue;
    }

    /**
     * 增加
     *
     * @return
     */
    public boolean increase() {
        if (value >= maxValue) return false;
        value += increment;
        paramChanged();
        return true;
    }

    /**
     * 减少
     *
     * @return
     */
    public boolean decrease() {
        if (value <= minValue) return false;
        value -= increment;
        paramChanged();
        return true;
    }

    /**
     * 重置为默认值
     */
    public void reset() {
        value = defaultValue;
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

    public int getValue() {
        return value;
    }

    /**
     * 参数改变，通知观察者
     */
    private void paramChanged() {
        setChanged();
        notifyObservers(value);
    }

}
