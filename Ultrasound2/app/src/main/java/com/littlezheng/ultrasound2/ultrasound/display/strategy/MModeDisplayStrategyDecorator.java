package com.littlezheng.ultrasound2.ultrasound.display.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound2.ultrasound.component.ParamController;
import com.littlezheng.ultrasound2.ultrasound.component.SampledData;
import com.littlezheng.ultrasound2.ultrasound.component.UContext;

import java.util.Observable;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class MModeDisplayStrategyDecorator extends ModeDisplayStrategyDecorator {

    private Bitmap bmp = Bitmap.createBitmap(500, SampledData.ORIGINAL_FRAME_HEIGHT,
            Bitmap.Config.ARGB_8888);
    private Rect src, dst;

    private ParamController speed;
    private Toast tipToast;
    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if(speed == null) return false;

            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            if(tipToast != null) tipToast.cancel();
            if (x > 0) {
                speed.increase();
                tipToast = Toast.makeText(mContext,"速度："+speed.getCurrValue(),Toast.LENGTH_SHORT);
                tipToast.show();
            } else if (x < 0) {
                speed.decrease();
                tipToast = Toast.makeText(mContext,"速度："+speed.getCurrValue(),Toast.LENGTH_SHORT);
                tipToast.show();
            }
            return true;
        }

    });

    public MModeDisplayStrategyDecorator(UContext uContext, DisplayStrategy strategy) {
        super(uContext, strategy);
        speed = uContext.getSpeed();
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);

        src = new Rect(0,0,bmp.getWidth(),bmp.getHeight());
        int wid = (int) (width * 0.6f), hei = wid / 500 * 400;
        int left = (width-wid)/2, top = (height-hei)/2;
        dst = new Rect(left,top,wid+left,top+hei);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        canvas.invalidateTextureContent(bmp);
        canvas.drawBitmap(bmp,src,dst);
    }

    @Override
    public void update(Observable o, Object arg) {
        bmp.setPixels(uContext.getMImagePixels(), 0, 500, 0, 0, 500, 400);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
}
