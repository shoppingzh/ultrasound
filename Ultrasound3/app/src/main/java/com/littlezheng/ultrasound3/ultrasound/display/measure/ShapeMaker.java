package com.littlezheng.ultrasound3.ultrasound.display.measure;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;

import java.util.Random;

/**
 * Created by Administrator on 2017/8/24/024.
 */

public abstract class ShapeMaker {

    protected final int steps;
    protected int step = 0;

    protected Paint pointPaint = new Paint();
    protected Paint linePaint = new Paint();
    protected Paint textPaint = new Paint();

    public ShapeMaker(int steps){
        this.steps = steps > 0 ? steps : 0;

        int randomColor = getRandomColor();
        pointPaint.setColor(randomColor);
        pointPaint.setStrokeWidth(3f);

        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[] { 1, 2, 4, 8}, 1);
        linePaint.setPathEffect(effects);

        textPaint.setColor(Color.WHITE);
        textPaint.setStrokeWidth(2f);
        textPaint.setTextSize(30f);
    }

    /**
     * 点击操作
     * @param x
     * @param y
     */
    public void click(float x, float y){
        if(step < steps) step++;
        //下一步具体要做什么由子类决定
    }

    /**
     * 移动操作
     * @param x
     * @param y
     */
    public void move(float x, float y){
        //no op
    }

    /**
     * 步骤是否结束
     * @return
     */
    public boolean stepEnd(){
        return step >= steps;
    }

    /**
     * 获取一个随机颜色
     * @return
     */
    int getRandomColor(){
        Random ran = new Random();
        return Color.rgb(ran.nextInt(255),ran.nextInt(255),ran.nextInt(255));
    }

    /**
     * 在指定画布上创建图形
     * @param canvas
     */
    public abstract void make(Canvas canvas);



}
