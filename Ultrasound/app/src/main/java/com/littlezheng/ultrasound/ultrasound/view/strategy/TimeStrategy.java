package com.littlezheng.ultrasound.ultrasound.view.strategy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.activity.MainActivity;
import com.littlezheng.ultrasound.ultrasound.util.DateUtils;
import com.littlezheng.ultrasound.ultrasound.view.image.TextImage;

import java.io.InputStream;
import java.util.AbstractList;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public class TimeStrategy extends AbstractDisplayStrategy {

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
        canvas.drawBitmap(time.getImage(),time.getX(),time.getY());
    }

    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            ((MainActivity)mContext).toggleFullScreen();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }
}
