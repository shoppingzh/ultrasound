package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.Configuration;
import com.littlezheng.ultrasound.ultrasound.Depth;
import com.littlezheng.ultrasound.ultrasound.process.ImageCreator;
import com.littlezheng.ultrasound.ultrasound.transfer.ModeController;
import com.littlezheng.ultrasound.ultrasound.view.DrawInfo;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/8/27/027.
 */

public class BMModeStrategyDecorator extends AbstractDisplayStrategyDecorator implements Observer{

    private Bitmap b = Bitmap.createBitmap(Configuration.THIRD_SAMPLE_MAX_WIDTH,
            Configuration.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);
    private Map<Depth,DrawInfo> depthDrawInfoMap = new HashMap<>();
    private Depth depth = Depth.DEPTH_160;

    private Bitmap m = Bitmap.createBitmap(500, Configuration.UDP_USEFUL_DATA_LEN,
            Bitmap.Config.ARGB_8888);
    private Rect mSrcRct = new Rect(0,0,m.getWidth(),m.getHeight());
    private Rect mDstRct;

    //提供控制
    private ImageCreator imageCreator;
    private ModeController modeController;

    public BMModeStrategyDecorator(Context context, DisplayStrategy displayStrategy) {
        super(context, displayStrategy);
    }

    public void init(int width, int height){
        int hei = height / 2 - 40;
        int wid = (int) (width * 0.6f);
        int leftLeft = (width - wid) / 2,leftTop = 20;
        int rightLeft = leftLeft, rightTop = leftTop + hei + 20;

        Rect leftDst = new Rect(leftLeft,leftTop,wid+leftLeft,hei+leftTop);
        mDstRct = new Rect(rightLeft,rightTop,wid+rightLeft,hei+rightTop);
        for(Depth depth : Depth.values()){
            int thirdSampleWid = Configuration.getThirdSampleWidth(depth);
            int thirdSampleHei = Configuration.getThirdSampleHeight(depth);
            int displayWid = Configuration.getDisplayWidth(depth);
            int displayHei = Configuration.getDisplayHeight(depth);
            Rect src = new Rect(thirdSampleWid-displayWid,0,displayWid,displayHei);

            depthDrawInfoMap.put(depth,new DrawInfo(thirdSampleWid,thirdSampleHei,src,leftDst));
        }
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        DrawInfo info = depthDrawInfoMap.get(depth);
        canvas.invalidateTextureContent(b);
        canvas.drawBitmap(b, info.src, info.dst);

        canvas.invalidateTextureContent(m);
        canvas.drawBitmap(m, mSrcRct, mDstRct);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(depthDrawInfoMap.get(depth).dst.contains((int)x, (int)y)){
                    imageCreator.setbEnable(false);
                    imageCreator.setmEnable(true);
                    modeController.setMode(ModeController.Mode.MODE_M);
                }
                if(mDstRct.contains((int)x, (int)y)){
                    imageCreator.setbEnable(true);
                    imageCreator.setmEnable(false);
                    modeController.setMode(ModeController.Mode.MODE_B);
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void update(Observable o, Object arg) {
        depth = ImageCreator.depth;
        b.setPixels(ImageCreator.bPixels,0,Configuration.THIRD_SAMPLE_MAX_WIDTH,0,0,
                Configuration.THIRD_SAMPLE_MAX_WIDTH,Configuration.THIRD_SAMPLE_MAX_HEIGHT);
        m.setPixels(ImageCreator.mPixels,0,m.getWidth(),0,0,m.getWidth(),m.getHeight());
    }

    public void setImageCreator(ImageCreator imageCreator) {
        this.imageCreator = imageCreator;
    }

    public void setModeController(ModeController modeController) {
        this.modeController = modeController;
    }
}
