package com.littlezheng.newultrasound.displayer;

import com.littlezheng.newultrasound.graphics.Container;

/**
 * Created by Administrator on 2017/11/16/016.
 */

public abstract class Displayer extends Container {

    /**
     * 显示组件的初始化方法，该方法决定显示组件上的每一个显示控件应该显示的位置
     * 该方法的调用时机：
     * @see {@link com.littlezheng.newultrasound.displayer.DisplayView}
     * 的surfaceChanged()方法与show()方法都会触发该方法
     *
     * @param width
     * @param height
     */
    public abstract void init(int width, int height);

}
