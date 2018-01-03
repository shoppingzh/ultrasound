package com.littlezheng.ultrasound4.ultrasound.display.workmode;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.view.DisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.SampledData;
import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.Param;
import com.littlezheng.ultrasound4.ultrasound.component.UImage;

/**
 * Created by Administrator on 2017/9/23/023.
 */

public class BBMode extends WorkModeDisplayStrategy {

    private int b1Depth = 13;
    private int b2Depth = 13;
    private RectF b1Dst;
    private RectF b2Dst;

    private Bitmap activeBmp = b1;
    private int activeDepth = b1Depth;

    private Param depth;

    public BBMode(UContext uContext, DisplayStrategy displayStrategy) {
        super(uContext, displayStrategy);
        depth = uContext.getParam(UContext.PARAM_DEPTH);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);

        float hei = height * 0.8f;
        float wid = (width - 120) / 2f;
        float leftLeft = 50, leftTop = height * 0.2f / 2;
        float rightLeft = 60 + wid, rightTop = leftTop;
        b1Dst = new RectF(leftLeft, leftTop, wid + leftLeft, hei + leftTop);
        b2Dst = new RectF(rightLeft, rightTop, wid + rightLeft, hei + rightTop);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.invalidateTextureContent(b1);
        canvas.drawBitmap(b1, bSrcMap.get(b1Depth), b1Dst);
        canvas.invalidateTextureContent(b2);
        canvas.drawBitmap(b2, bSrcMap.get(b2Depth), b2Dst);

        super.onGLDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (b1Dst.contains((int) x, (int) y)) {
                    activeBmp = b2;
                    activeDepth = b2Depth;
                }
                if (b2Dst.contains((int) x, (int) y)) {
                    activeBmp = b1;
                    activeDepth = b1Depth;
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void refresh(UImage uImage) {
        if (activeBmp == b1) {
            b1Depth = uImage.getDepth();
        } else {
            b2Depth = uImage.getDepth();
        }

        activeBmp.setPixels(uImage.getPixels(), 0, SampledData.THIRD_SAMPLE_MAX_WIDTH,
                0, 0, SampledData.THIRD_SAMPLE_MAX_WIDTH, SampledData.THIRD_SAMPLE_MAX_HEIGHT);
    }

}
