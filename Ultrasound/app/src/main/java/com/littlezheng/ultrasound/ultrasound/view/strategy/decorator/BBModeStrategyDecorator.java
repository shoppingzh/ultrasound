package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.Configuration;
import com.littlezheng.ultrasound.ultrasound.Depth;
import com.littlezheng.ultrasound.ultrasound.process.ImageCreator;
import com.littlezheng.ultrasound.ultrasound.view.DrawInfo;
import com.littlezheng.ultrasound.ultrasound.view.image.Image;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/8/27/027.
 */

public class BBModeStrategyDecorator extends AbstractDisplayStrategyDecorator implements Observer{

    private static final String TAG = "BBModeStrategyDecorator";

    private Bitmap b1 = Bitmap.createBitmap(Configuration.THIRD_SAMPLE_MAX_WIDTH,
            Configuration.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);
    private Bitmap b2 = Bitmap.createBitmap(Configuration.THIRD_SAMPLE_MAX_WIDTH,
            Configuration.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);

    private Depth b1Depth = Depth.DEPTH_DEFAULT;
    private Depth b2Depth = Depth.DEPTH_DEFAULT;

    private Map<Depth,DrawInfo> b1DrawInfoMap = new HashMap<>();
    private Map<Depth,DrawInfo> b2DrawInfoMap = new HashMap<>();

    private Bitmap activeBmp = b1;
    private Depth activeDepth = b1Depth;

    public BBModeStrategyDecorator(Context context, DisplayStrategy displayStrategy) {
        super(context, displayStrategy);
    }

    public void init(int width, int height){
        for(Depth depth : Depth.values()){
            int thirdSampleWid = Configuration.getThirdSampleWidth(depth);
            int thirdSampleHei = Configuration.getThirdSampleHeight(depth);
            int displayWid = Configuration.getDisplayWidth(depth);
            int displayHei = Configuration.getDisplayHeight(depth);
            Rect src = new Rect(thirdSampleWid-displayWid,0,displayWid,displayHei);

            int hei = height / 2 - 40;
            int wid = (int) (width * 0.6f);
            int leftLeft = (width - wid) / 2,leftTop = 20;
            int rightLeft = leftLeft, rightTop = leftTop + hei + 20;

            Rect leftDst = new Rect(leftLeft,leftTop,wid+leftLeft,hei+leftTop);
            Rect rightDst = new Rect(rightLeft,rightTop,wid+rightLeft,hei+rightTop);
            b1DrawInfoMap.put(depth,new DrawInfo(thirdSampleWid,thirdSampleHei,src,leftDst));
            b2DrawInfoMap.put(depth,new DrawInfo(thirdSampleWid,thirdSampleHei,src,rightDst));
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
            b1Depth = ImageCreator.depth;
        }else{
            b2Depth = ImageCreator.depth;
        }

        activeBmp.setPixels(ImageCreator.bPixels,0,Configuration.THIRD_SAMPLE_MAX_WIDTH,0,0,
                Configuration.THIRD_SAMPLE_MAX_WIDTH,Configuration.THIRD_SAMPLE_MAX_HEIGHT);
    }

}
