package com.littlezheng.ultrasound.ultrasound.util;

import android.graphics.Paint;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public abstract class GraphUtils {

    /**
     * 获取简单文本画笔，只指定文字大小和颜色
     * @param textSize 文字大小
     * @param textColor 文字颜色
     * @return
     */
    public static Paint getSimpleTextPaint(float textSize, int textColor){
        Paint p = new Paint();
        p.setTextSize(textSize);
        p.setColor(textColor);
        return p;
    }

}
