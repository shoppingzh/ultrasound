package com.littlezheng.ultrasound2.ultrasound.component;

import android.graphics.Color;

import java.util.Observable;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class ColorController extends Observable{

    private PseudoColor color;

    //颜色信息
    private final int[] grayColors = new int[256];
    private final int[] redColors = new int[256];
    private final int[] yellowColors = new int[256];
    private final int[] mixColors = new int[256];
    private int[] colors = grayColors;

    public ColorController(){
        initColorArrays();
    }

    /**
     * 初始化颜色数组（灰色+伪彩）
     */
    private void initColorArrays() {
        //初始化灰阶颜色数组
        for(int i=0;i<256;i++){
            grayColors[i] = Color.rgb(i, i, i);
            redColors[i] = Color.rgb(i, 0, 0);
            yellowColors[i] = Color.rgb(i, i, 0);
            if(i < 72){
                mixColors[i] = Color.rgb(i, i, 0);
            }else{
                mixColors[i] = Color.rgb(i, 0, 0);
            }
        }
    }

    /**
     * 改变颜色
     * @param color
     */
    public void change(PseudoColor color){
        this.color = color;
        switch (color){
            case COLOR_NORMAL:
                colors = grayColors;
                break;
            case COLOR_RED:
                colors = redColors;
                break;
            case COLOR_YELLOW:
                colors = yellowColors;
                break;
            case COLOR_MIX:
                colors = mixColors;
                break;
        }
        setChanged();
        notifyObservers(0);
    }

    public int[] getColors() {
        return colors;
    }

    public enum PseudoColor{
        COLOR_NORMAL,
        COLOR_RED,
        COLOR_YELLOW,
        COLOR_MIX
    }

}
