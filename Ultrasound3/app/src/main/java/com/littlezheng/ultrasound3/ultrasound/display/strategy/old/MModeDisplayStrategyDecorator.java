package com.littlezheng.ultrasound3.ultrasound.display.strategy.old;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Param;
import com.littlezheng.ultrasound3.ultrasound.base.SampledData;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.DisplayStrategy;

import java.util.Observable;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class MModeDisplayStrategyDecorator extends ModeDisplayStrategyDecorator {

    private Bitmap bmp = Bitmap.createBitmap(500, SampledData.ORIGINAL_FRAME_HEIGHT,
            Bitmap.Config.ARGB_4444);
    private Rect src;
    private RectF dst;

    private Param speed;
    private Toast tipToast;
    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (speed == null) return false;

            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            if (tipToast != null) tipToast.cancel();
            if (x > 0) {
                speed.increase();
                tipToast = Toast.makeText(mContext, "速度：" + speed.getCurrValue(), Toast.LENGTH_SHORT);
                tipToast.show();
            } else if (x < 0) {
                speed.decrease();
                tipToast = Toast.makeText(mContext, "速度：" + speed.getCurrValue(), Toast.LENGTH_SHORT);
                tipToast.show();
            }
            return true;
        }

    });

    public MModeDisplayStrategyDecorator(UContext uContext, DisplayStrategy strategy) {
        super(uContext, strategy);
        speed = uContext.getParam(UContext.PARAM_SPEED);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);

        src = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        float wid = (int) (width * 0.7f), hei = wid / 500 * 400;
        float left = (width - wid) / 2, top = (height - hei) / 2;
        dst = new RectF(left, top, wid + left, top + hei);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        canvas.invalidateTextureContent(bmp);
        canvas.drawBitmap(bmp, src, dst);
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

}
