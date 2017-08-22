package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.Configuration;
import com.littlezheng.ultrasound.ultrasound.Depth;
import com.littlezheng.ultrasound.ultrasound.process.ImageCreator;
import com.littlezheng.ultrasound.ultrasound.view.DrawInfo;
import com.littlezheng.ultrasound.ultrasound.view.strategy.AbsDisplayStrategy;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/8/22/022.
 * B模式下只有一个B图像，位于视图正中间
 */

public class BModeStrategyDecorator extends AbsDisplayStrategy implements Observer{

    private static final String TAG = "BModeStrategyDecorator";
    private DisplayStrategy strategy;

    private Bitmap bmp = Bitmap.createBitmap(Configuration.THIRD_SAMPLE_MAX_WIDTH,
            Configuration.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);
    private Map<Depth,DrawInfo> depthDrawInfoMap = new HashMap<>();
    private Depth depth = Depth.DEPTH_160;


    public BModeStrategyDecorator(Context context, DisplayStrategy strategy) {
        super(context);
        this.strategy = strategy;
    }

    public void init(int width, int height){
        for(Depth depth : Depth.values()){
            int thirdSampleWid = Configuration.getThirdSampleWidth(depth);
            int thirdSampleHei = Configuration.getThirdSampleHeight(depth);
            int displayWid = Configuration.getDisplayWidth(depth);
            int displayHei = Configuration.getDisplayHeight(depth);
            Rect src = new Rect(thirdSampleWid-displayWid,0,displayWid,displayHei);
            int hei = Math.round(height * 0.9f),wid = Math.round((width * 0.9f)) ;
            int left = (width-wid)/2,top = (height-hei)/2;
            Rect dst = new Rect(left,top,wid+left,hei+top);
            depthDrawInfoMap.put(depth,new DrawInfo(thirdSampleWid,thirdSampleHei,src,dst));
        }
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        strategy.onGLDraw(canvas);

        DrawInfo info = depthDrawInfoMap.get(depth);
        canvas.invalidateTextureContent(bmp);
        canvas.drawBitmap(bmp,info.src,info.dst);
    }

    @Override
    public void update(Observable o, Object arg) {
        this.depth = ImageCreator.depth;
        bmp.setPixels(ImageCreator.bPixels,0,Configuration.THIRD_SAMPLE_MAX_WIDTH,0,0,
                Configuration.THIRD_SAMPLE_MAX_WIDTH,Configuration.THIRD_SAMPLE_MAX_HEIGHT);
    }


}
