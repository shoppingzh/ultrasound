package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;
import com.littlezheng.ultrasound3.ultrasound.base.SampledData;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.display.DrawInfo;
import com.littlezheng.ultrasound3.ultrasound.display.image.TextImage;
import com.littlezheng.ultrasound3.ultrasound.util.GraphUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class ReplayDisplayStrategy extends BaseDisplayStrategy implements Observer {

    private UContext uContext;

    private Bitmap bmp = Bitmap.createBitmap(SampledData.THIRD_SAMPLE_MAX_WIDTH,
            SampledData.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);
    private Map<Integer, DrawInfo> depthDrawInfoMap = new HashMap<>();
    private int depth;

    //回放提示文本
    private TextImage tipText;
    private GLPaint tipPaint;

    //回放帧数信息
    private TextImage framesText;

    public ReplayDisplayStrategy(UContext uContext) {
        super(uContext.getAndroidContext());
        this.uContext = uContext;
        depth = uContext.getParam(UContext.PARAM_DEPTH).getCurrValue();
        tipText = new TextImage(GraphUtils.getSimpleTextPaint(40, Color.WHITE), 2);
        tipText.setDrawPos(50, 20);
        tipText.drawText("回放");
        tipPaint = new GLPaint();
        tipPaint.setColor(Color.RED);
        tipPaint.setStyle(Paint.Style.FILL);

        Paint p = GraphUtils.getSimpleTextPaint(30f, Color.CYAN);
        String totalFrame = String.valueOf(uContext.getImageHolder().getCapacity());
        String maxText = totalFrame + "/" + totalFrame;
        framesText = new TextImage(p, (int) p.measureText(maxText), 30);
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

        framesText.setDrawPos(width - framesText.getWidth() - 10, height - framesText.getHeight() - 10);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {

        canvas.drawCircle(22, 40, 12, tipPaint);
        canvas.drawBitmap(tipText.getImage(), tipText.getX(), tipText.getY());

        canvas.invalidateTextureContent(framesText.getImage());
        canvas.drawBitmap(framesText.getImage(), framesText.getX(), framesText.getY());

        DrawInfo info = depthDrawInfoMap.get(depth);
        canvas.invalidateTextureContent(bmp);
        canvas.drawBitmap(bmp, info.src, info.dst);

    }

    @Override
    public void update(Observable o, Object arg) {
        this.depth = (int) arg;
        bmp.setPixels(uContext.getBImagePixels(), 0, SampledData.THIRD_SAMPLE_MAX_WIDTH, 0, 0,
                SampledData.THIRD_SAMPLE_MAX_WIDTH, SampledData.THIRD_SAMPLE_MAX_HEIGHT);
    }

}
