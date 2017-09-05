package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.SampledData;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.display.DrawInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class ReplayDisplayStrategy extends BaseDisplayStrategy implements Observer{

    private UContext uContext;

    private Bitmap bmp = Bitmap.createBitmap(SampledData.THIRD_SAMPLE_MAX_WIDTH,
            SampledData.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);
    private Map<Integer, DrawInfo> depthDrawInfoMap = new HashMap<>();
    private int depth;

    public ReplayDisplayStrategy(UContext uContext) {
        super(uContext.getAndroidContext());
        this.uContext = uContext;
        depth = uContext.getParam(UContext.PARAM_DEPTH).getCurrValue();
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        for(int i=0;i<20;i++){
            int thirdSampleWid = SampledData.getThirdSampleWidth(i);
            int thirdSampleHei = SampledData.getThirdSampleHeight(i);
            int displayWid = SampledData.getDisplayWidth(i);
            int displayHei = SampledData.getDisplayHeight(i);
            Rect src = new Rect(thirdSampleWid-displayWid,0,displayWid,displayHei);
            int hei = Math.round(height * 0.9f),wid = Math.round((width * 0.9f)) ;
            int left = (width-wid)/2,top = (height-hei)/2;
            Rect dst = new Rect(left,top,wid+left,hei+top);
            depthDrawInfoMap.put(i, new DrawInfo(thirdSampleWid,thirdSampleHei,src,dst));
        }
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        DrawInfo info = depthDrawInfoMap.get(depth);
        canvas.invalidateTextureContent(bmp);
        canvas.drawBitmap(bmp,info.src,info.dst);
    }

    @Override
    public void update(Observable o, Object arg) {
        this.depth = (int) arg;
        bmp.setPixels(uContext.getBImagePixels(), 0, SampledData.THIRD_SAMPLE_MAX_WIDTH, 0, 0,
                SampledData.THIRD_SAMPLE_MAX_WIDTH,SampledData.THIRD_SAMPLE_MAX_HEIGHT);
    }

}
