package com.littlezheng.ultrasound.ultrasound.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

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

    /**
     * 清除画布内容
     * @param c
     */
    public static void clearCanvas(Canvas c){
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        c.drawPaint(paint);
    }

}
