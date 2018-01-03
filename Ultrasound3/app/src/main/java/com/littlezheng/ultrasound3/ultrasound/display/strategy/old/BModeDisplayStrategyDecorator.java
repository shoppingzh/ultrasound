package com.littlezheng.ultrasound3.ultrasound.display.strategy.old;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.SampledData;
import com.littlezheng.ultrasound3.ultrasound.display.DrawInfo;
import com.littlezheng.ultrasound3.ultrasound.display.image.TextImage;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.DisplayStrategy;
import com.littlezheng.ultrasound3.ultrasound.util.GraphUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class BModeDisplayStrategyDecorator extends ModeDisplayStrategyDecorator {

    private static final String TAG = "BModeDisplayStrategyDecorator";

    private Bitmap bmp = Bitmap.createBitmap(SampledData.THIRD_SAMPLE_MAX_WIDTH,
            SampledData.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_4444);
    private Map<Integer, DrawInfo> depthDrawInfoMap = new HashMap<>();
    private int depth;

    private TextImage backFatTi;

    public BModeDisplayStrategyDecorator(UContext uContext, DisplayStrategy strategy) {
        super(uContext, strategy);
        depth = uContext.getParam(UContext.PARAM_DEPTH).getCurrValue();

        Paint p = GraphUtils.getSimpleTextPaint(30, Color.GREEN);
        backFatTi = new TextImage(p, (int) p.measureText("背膘：40.00"), 30);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        for (int i = 0; i < 20; i++) {
            int thirdSampleWid = SampledData.getThirdSampleWidth(i);
            int thirdSampleHei = SampledData.getThirdSampleHeight(i);
            int displayWid = SampledData.getDisplayWidth(i);
            int displayHei = SampledData.getDisplayHeight(i);
            Rect src = new Rect(thirdSampleWid - displayWid, 0, displayWid, displayHei);
            int hei = Math.round(height * 0.9f), wid = Math.round((width * 0.9f));
            int left = (width - wid) / 2, top = (height - hei) / 2;
            Rect dst = new Rect(left, top, wid + left, hei + top);
            depthDrawInfoMap.put(i, new DrawInfo(thirdSampleWid, thirdSampleHei, src, dst));
        }
        backFatTi.setDrawPos(width - backFatTi.getWidth() - 60, 100);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        DrawInfo info = depthDrawInfoMap.get(depth);
        canvas.invalidateTextureContent(bmp);
        canvas.drawBitmap(bmp, info.src, info.dst);

        canvas.invalidateTextureContent(backFatTi.getImage());
        canvas.drawBitmap(backFatTi.getImage(), backFatTi.getX(), backFatTi.getY());
    }

    @Override
    public void update(Observable o, Object arg) {
        this.depth = uContext.getParam(UContext.PARAM_DEPTH).getCurrValue();
        bmp.setPixels(uContext.getBImagePixels(), 0, SampledData.THIRD_SAMPLE_MAX_WIDTH, 0, 0,
                SampledData.THIRD_SAMPLE_MAX_WIDTH, SampledData.THIRD_SAMPLE_MAX_HEIGHT);
        if (arg != null) {
            backFatTi.drawText("背膘：" + arg);
        }
    }

}
