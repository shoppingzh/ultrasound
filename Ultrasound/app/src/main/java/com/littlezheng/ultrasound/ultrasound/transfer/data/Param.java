package com.littlezheng.ultrasound.ultrasound.transfer.data;

/**
 * Created by zxp on 2017/8/9.
 */

public class Param extends DataReturner {

    private final String name;

    private final int defaultValue;
    private final int maxValue;
    private final int minValue;
    private final int increment;

    private int currValue;

    public Param(int controlCode, String name, int defaultValue,
                 int maxValue, int minValue, int increment) {
        super(controlCode);
        this.name = name;
        this.defaultValue = defaultValue;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.increment = increment;
        this.currValue = this.defaultValue;
    }

    public void increase(){
        if(currValue >= maxValue) return;
        currValue += increment;
        setData(currValue);
    }

    public void decrease(){
        if(currValue <= minValue) return;
        currValue -= increment;
        setData(currValue);
    }

    public void toDefault(){
        currValue = defaultValue;
        setData(currValue);
    }

    public String getName() {
        return name;
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

    public String getDesc(){
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append(" : ");
        sb.append(currValue);
        return sb.toString();
    }

}
