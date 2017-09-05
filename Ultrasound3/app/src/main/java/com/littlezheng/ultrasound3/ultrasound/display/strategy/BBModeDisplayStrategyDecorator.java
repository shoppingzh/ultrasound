package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.SampledData;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Param;
import com.littlezheng.ultrasound3.ultrasound.display.DrawInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class BBModeDisplayStrategyDecorator extends ModeDisplayStrategyDecorator {

    private static final String TAG = "BBModeDisplayStrategyDecorator";

    private final Param depthController;

    private Bitmap b1 = Bitmap.createBitmap(SampledData.THIRD_SAMPLE_MAX_WIDTH,
            SampledData.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);
    private Bitmap b2 = Bitmap.createBitmap(SampledData.THIRD_SAMPLE_MAX_WIDTH,
            SampledData.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);

    private int b1Depth = 13;
    private int b2Depth = 13;

    private Map<Integer,DrawInfo> b1DrawInfoMap = new HashMap<>();
    private Map<Integer,DrawInfo> b2DrawInfoMap = new HashMap<>();

    private Bitmap activeBmp = b1;
    private int activeDepth = b1Depth;

    public BBModeDisplayStrategyDecorator(UContext uContext, DisplayStrategy strategy) {
        super(uContext, strategy);
        depthController = uContext.getParam(UContext.PARAM_DEPTH);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);

        for(int i=0; i<20; i++){
            int thirdSampleWid = SampledData.getThirdSampleWidth(i);
            int thirdSampleHei = SampledData.getThirdSampleHeight(i);
            int displayWid = SampledData.getDisplayWidth(i);
            int displayHei = SampledData.getDisplayHeight(i);
            Rect src = new Rect(thirdSampleWid-displayWid,0,displayWid,displayHei);

            int hei = height / 2 - 40;
            int wid = (int) (width * 0.6f);
            int leftLeft = (width - wid) / 2,leftTop = 20;
            int rightLeft = leftLeft, rightTop = leftTop + hei + 20;

            Rect leftDst = new Rect(leftLeft,leftTop,wid+leftLeft,hei+leftTop);
            Rect rightDst = new Rect(rightLeft,rightTop,wid+rightLeft,hei+rightTop);
            b1DrawInfoMap.put(i,new DrawInfo(thirdSampleWid,thirdSampleHei,src,leftDst));
            b2DrawInfoMap.put(i,new DrawInfo(thirdSampleWid,thirdSampleHei,src,rightDst));
        }
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        DrawInfo b1Info = b1DrawInfoMap.get(b1Depth);
        canvas.invalidateTextureContent(b1);
        canvas.drawBitmap(b1,b1Info.src,b1Info.dst);

        DrawInfo b2Info = b2DrawInfoMap.get(b2Depth);
        canvas.invalidateTextureContent(b2);
        canvas.drawBitmap(b2,b2Info.src,b2Info.dst);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(b1DrawInfoMap.get(b1Depth).dst.contains((int)x, (int)y)){
                    activeBmp = b2;
                    activeDepth = b2Depth;
                    Log.d(TAG,"activeDepth: "+activeDepth);
                }
                if(b2DrawInfoMap.get(b2Depth).dst.contains((int)x, (int)y)){
                    activeBmp = b1;
                    activeDepth = b1Depth;
                    Log.d(TAG,"activeDepth: "+activeDepth);
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(activeBmp == b1){
            b1Depth = depthController.getCurrValue();
        }else{
            b2Depth = depthController.getCurrValue();
        }

        activeBmp.setPixels(uContext.getBImagePixels(), 0, SampledData.THIRD_SAMPLE_MAX_WIDTH,
                0, 0, SampledData.THIRD_SAMPLE_MAX_WIDTH, SampledData.THIRD_SAMPLE_MAX_HEIGHT);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        b1.eraseColor(Color.BLACK);
        b2.eraseColor(Color.BLACK);

        activeBmp = b1;
        activeDepth = b1Depth;
    }
}
