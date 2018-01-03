package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.display.image.TextImage;
import com.littlezheng.ultrasound3.ultrasound.util.DateUtils;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public class TimeStrategy extends BaseDisplayStrategy {

    private TextImage time;

    public TimeStrategy(Context context) {
        super(context);
        Paint p = new Paint();
        p.setColor(Color.CYAN);
        p.setTextSize(30f);
        time = new TextImage(p, 12);
        time.setDrawPos(20, 20);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        time.drawText(DateUtils.getDateTimeStr());
        canvas.invalidateTextureContent(time.getImage());
        canvas.drawBitmap(time.getImage(), time.getX(), time.getY());
    }

}
