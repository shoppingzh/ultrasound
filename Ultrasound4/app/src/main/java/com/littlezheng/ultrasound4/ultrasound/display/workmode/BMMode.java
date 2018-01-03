package com.littlezheng.ultrasound4.ultrasound.display.workmode;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.view.DisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.SampledData;
import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.UImage;

/**
 * Created by Administrator on 2017/9/23/023.
 */

public class BMMode extends WorkModeDisplayStrategy {

    private int bDepth = 13;
    private RectF bDst;

    private int mDepth = 13;
    private Rect mSrc;
    private RectF mDst;

    private Bitmap activeBmp = b1;
    private int activeDepth = bDepth;

    public BMMode(UContext uContext, DisplayStrategy displayStrategy) {
        super(uContext, displayStrategy);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);

        float hei = height * 0.8f;
        float wid = (width - 120) / 2f;
        float leftLeft = 50;
        float leftTop = height * 0.2f / 2;
        bDst = new RectF(leftLeft, leftTop, wid + leftLeft, hei + leftTop);

        float rightLeft = 60 + wid, rightTop = leftTop;
        mSrc = new Rect(0, 0, m.getWidth(), m.getHeight());
        mDst = new RectF(rightLeft, rightTop, wid + rightLeft, hei + rightTop);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.drawBitmap(b1, bSrcMap.get(bDepth), bDst);
        canvas.invalidateTextureContent(b1);
        canvas.drawBitmap(m, mSrc, mDst);
        canvas.invalidateTextureContent(m);

        super.onGLDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (bDst.contains((int) x, (int) y)) {
                    if(activeBmp == m) return super.onTouchEvent(event);
                    activeBmp = m;
                    activeDepth = mDepth;
                }
                if (mDst.contains((int) x, (int) y)) {
                    if(activeBmp == b1) return super.onTouchEvent(event);
                    activeBmp = b1;
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void refresh(UImage uImage) {
        if (activeBmp == b1) {
            bDepth = uImage.getDepth();
            b1.setPixels(uImage.getPixels(), 0, SampledData.THIRD_SAMPLE_MAX_WIDTH,
                    0, 0, SampledData.THIRD_SAMPLE_MAX_WIDTH, SampledData.THIRD_SAMPLE_MAX_HEIGHT);
        } else {
            m.setPixels(uImage.getPixels(), 0, 500, 0, 0,
                    500, SampledData.ORIGINAL_FRAME_HEIGHT);
        }
    }

}
