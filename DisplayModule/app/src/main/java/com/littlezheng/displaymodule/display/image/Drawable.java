package com.littlezheng.displaymodule.display.image;

import android.graphics.Canvas;

/**
 * Created by zxp on 2017/7/24.
 */

public interface Drawable {

    /**
     * 设定绘制位置
     * @param x 绘制x坐标
     * @param y 绘制y坐标
     */
    void setDrawPos(int x, int y);

    /**
     * 获取绘制的x坐标
     * @return
     */
    int getX();

    /**
     * 获取绘制的y坐标
     * @return
     */
    int getY();

    /**
     * 清除位图内容
     */
    void clear(Canvas c);

}
