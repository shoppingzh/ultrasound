package com.littlezheng.drawingboard.api;

import android.graphics.Point;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public abstract class MeasureUtils {

    /**
     * 求两点之间的长度
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double length(float x1, float y1, float x2, float y2){
        float width = Math.abs(x1 - x2);
        float height = Math.abs(y1 - y2);
        return Math.sqrt(width*width + height*height);
    }

    /**
     * 求两点之间的长度
     * @param p1
     * @param p2
     * @return
     */
    public static double length(Point p1, Point p2){
        float width = Math.abs(p1.x - p2.x);
        float height = Math.abs(p1.y - p2.y);
        return Math.sqrt(width*width + height*height);
    }

}
