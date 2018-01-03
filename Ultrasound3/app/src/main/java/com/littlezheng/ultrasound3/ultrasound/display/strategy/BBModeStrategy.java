package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Param;
import com.littlezheng.ultrasound3.ultrasound.base.SampledData;
import com.littlezheng.ultrasound3.ultrasound.display.DrawInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/9/009.
 */

public class BBModeStrategy extends ModeStrategy {

    private final Param depth;

    private Bitmap b1 = Bitmap.createBitmap(SampledData.THIRD_SAMPLE_MAX_WIDTH,
            SampledData.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);
    private Bitmap b2 = Bitmap.createBitmap(SampledData.THIRD_SAMPLE_MAX_WIDTH,
            SampledData.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);

    private int b1Depth = 13;
    private int b2Depth = 13;

    private Map<Integer, DrawInfo> b1DrawInfoMap = new HashMap<>();
    private Map<Integer, DrawInfo> b2DrawInfoMap = new HashMap<>();

    private Bitmap activeBmp = b1;
    private int activeDepth = b1Depth;

    public BBModeStrategy(UContext uContext, DisplayStrategy strategy) {
        super(uContext, strategy);
        depth = uContext.getParam(UContext.PARAM_DEPTH);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);

        float hei = height * 0.8f;
        float wid = (width - 120) / 2f;
        float leftLeft = 50, leftTop = height * 0.2f / 2;
        float rightLeft = 60 + wid, rightTop = leftTop;

        RectF leftDst = new RectF(leftLeft, leftTop, wid + leftLeft, hei + leftTop);
        RectF rightDst = new RectF(rightLeft, rightTop, wid + rightLeft, hei + rightTop);

        for (int i = 0; i < 20; i++) {
            int thirdSampleWid = SampledData.getThirdSampleWidth(i);
            int thirdSampleHei = SampledData.getThirdSampleHeight(i);
            int displayWid = SampledData.getDisplayWidth(i);
            int displayHei = SampledData.getDisplayHeight(i);
            Rect src = new Rect(thirdSampleWid - displayWid, 0, displayWid, displayHei);

            b1DrawInfoMap.put(i, new DrawInfo(thirdSampleWid, thirdSampleHei, src, leftDst));
            b2DrawInfoMap.put(i, new DrawInfo(thirdSampleWid, thirdSampleHei, src, rightDst));
        }
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        DrawInfo b1Info = b1DrawInfoMap.get(b1Depth);
        canvas.drawBitmap(b1, b1Info.src, b1Info.dst);

        DrawInfo b2Info = b2DrawInfoMap.get(b2Depth);
        canvas.drawBitmap(b2, b2Info.src, b2Info.dst);
    }

    @Override
    protected void refresh(ICanvasGL canvas) {
        if (activeBmp == b1) {
            b1Depth = depth.getCurrValue();
        } else {
            b2Depth = depth.getCurrValue();
        }

        activeBmp.setPixels(uContext.getBImagePixels(), 0, SampledData.THIRD_SAMPLE_MAX_WIDTH,
                0, 0, SampledData.THIRD_SAMPLE_MAX_WIDTH, SampledData.THIRD_SAMPLE_MAX_HEIGHT);
        canvas.invalidateTextureContent(b1);
        canvas.invalidateTextureContent(b2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (b1DrawInfoMap.get(b1Depth).dst.contains((int) x, (int) y)) {
                    activeBmp = b2;
                    activeDepth = b2Depth;
                }
                if (b2DrawInfoMap.get(b2Depth).dst.contains((int) x, (int) y)) {
                    activeBmp = b1;
                    activeDepth = b1Depth;
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        b1.eraseColor(Color.BLACK);
        b2.eraseColor(Color.BLACK);

        activeBmp = b1;
    }

}
