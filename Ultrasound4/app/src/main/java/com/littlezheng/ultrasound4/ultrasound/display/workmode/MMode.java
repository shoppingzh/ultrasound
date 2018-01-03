package com.littlezheng.ultrasound4.ultrasound.display.workmode;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.view.DisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.Param;
import com.littlezheng.ultrasound4.ultrasound.component.UImage;

/**
 * Created by Administrator on 2017/9/23/023.
 */

public class MMode extends WorkModeDisplayStrategy {

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

    public MMode(UContext uContext, DisplayStrategy displayStrategy) {
        super(uContext, displayStrategy);
        speed = uContext.getParam(UContext.PARAM_SPEED);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);

        src = new Rect(0, 0, m.getWidth(), m.getHeight());
        float wid = (int) (width * 0.75f), hei = wid / 500 * 400;
        float left = (width - wid) / 2, top = (height - hei) / 2;
        dst = new RectF(left, top, wid + left, top + hei);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.invalidateTextureContent(m);
        canvas.drawBitmap(m, src, dst);

        super.onGLDraw(canvas);
    }

    @Override
    protected void refresh(UImage uImage) {
        m.setPixels(uImage.getPixels(), 0, 500, 0, 0, 500, 400);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

}
