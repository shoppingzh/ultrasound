package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Mode;
import com.littlezheng.ultrasound3.ultrasound.base.Param;
import com.littlezheng.ultrasound3.ultrasound.base.SampledData;
import com.littlezheng.ultrasound3.ultrasound.display.DrawInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/12/012.
 */

public class BMModeStrategy extends ModeStrategy {

    private final Param depth;

    private Bitmap b = Bitmap.createBitmap(SampledData.THIRD_SAMPLE_MAX_WIDTH,
            SampledData.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);
    private Map<Integer, DrawInfo> bDrawInfoMap = new HashMap<>();
    private int bDepth = 13;

    private Bitmap m = Bitmap.createBitmap(500, SampledData.ORIGINAL_FRAME_HEIGHT,
            Bitmap.Config.ARGB_8888);
    private Rect mSrc;
    private RectF mDst;
    private int mDepth = 13;

    private Bitmap activeBmp = b;
    private int activeDepth = bDepth;

    public BMModeStrategy(UContext uContext, DisplayStrategy strategy) {
        super(uContext, strategy);
        depth = uContext.getParam(UContext.PARAM_DEPTH);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);

        float hei = height * 0.8f;
        float wid = (width - 120) / 2f;
        float leftLeft = 50;
        float leftTop = height * 0.2f / 2;
        RectF leftDst = new RectF(leftLeft, leftTop, wid + leftLeft, hei + leftTop);

        for (int i = 0; i < 20; i++) {
            int thirdSampleWid = SampledData.getThirdSampleWidth(i);
            int thirdSampleHei = SampledData.getThirdSampleHeight(i);
            int displayWid = SampledData.getDisplayWidth(i);
            int displayHei = SampledData.getDisplayHeight(i);
            Rect src = new Rect(thirdSampleWid - displayWid, 0, displayWid, displayHei);

            bDrawInfoMap.put(i, new DrawInfo(thirdSampleWid, thirdSampleHei, src, leftDst));
        }

        float rightLeft = 60 + wid, rightTop = leftTop;
        mSrc = new Rect(0, 0, m.getWidth(), m.getHeight());
        mDst = new RectF(rightLeft, rightTop, wid + rightLeft, hei + rightTop);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        DrawInfo b1Info = bDrawInfoMap.get(bDepth);
        canvas.drawBitmap(b, b1Info.src, b1Info.dst);

        canvas.drawBitmap(m, mSrc, mDst);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (bDrawInfoMap.get(bDepth).dst.contains((int) x, (int) y)) {
                    activeBmp = m;
                    activeDepth = mDepth;
                    uContext.getModeSwitcher().setMode(Mode.MODE_M);
                }
                if (mDst.contains((int) x, (int) y)) {
                    activeBmp = b;
                    uContext.getModeSwitcher().setMode(Mode.MODE_B);
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void refresh(ICanvasGL canvas) {
        if (activeBmp == b) {
            bDepth = depth.getCurrValue();
            b.setPixels(uContext.getBImagePixels(), 0, SampledData.THIRD_SAMPLE_MAX_WIDTH,
                    0, 0, SampledData.THIRD_SAMPLE_MAX_WIDTH, SampledData.THIRD_SAMPLE_MAX_HEIGHT);
            canvas.invalidateTextureContent(b);
        } else {
            mDepth = depth.getCurrValue();
            m.setPixels(uContext.getMImagePixels(), 0, 500, 0, 0,
                    500, SampledData.ORIGINAL_FRAME_HEIGHT);
            canvas.invalidateTextureContent(m);
        }
    }


}
