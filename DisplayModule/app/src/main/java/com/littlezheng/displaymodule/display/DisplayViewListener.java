package com.littlezheng.displaymodule.display;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public interface DisplayViewListener {

    /**
     * 显示GL视图大小发生改变时回调该方法
     * @param gl
     * @param width 改变后的视图宽度
     * @param height 改变后的视图高度
     */
    void onSurfaceChanged(GL10 gl, int width, int height);

}
