package com.littlezheng.ultrasound4.ultrasound.display;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.graphics.image.TextImage;
import com.littlezheng.ultrasound4.framework.view.AbstractDisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.util.DateUtils;

/**
 * Created by Administrator on 2017/9/20/020.
 */

public class TimeDisplayStrategy extends AbstractDisplayStrategy {

    private TextImage timeTi;

    public TimeDisplayStrategy(Context context) {
        super(context);
        Paint p = new Paint();
        p.setColor(Color.CYAN);
        p.setTextSize(30f);
        timeTi = new TextImage(p, 12);
        timeTi.setDrawPos(20, 20);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        timeTi.drawText(DateUtils.getDateTimeStr());
        canvas.invalidateTextureContent(timeTi.getImage());
        canvas.drawBitmap(timeTi.getImage(), timeTi.getX(), timeTi.getY());
    }

}
