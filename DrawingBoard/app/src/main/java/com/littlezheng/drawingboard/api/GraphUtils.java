package com.littlezheng.drawingboard.api;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public class GraphUtils {

    public static void clearCanvas(Canvas c){
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        c.drawPaint(paint);
    }

}
