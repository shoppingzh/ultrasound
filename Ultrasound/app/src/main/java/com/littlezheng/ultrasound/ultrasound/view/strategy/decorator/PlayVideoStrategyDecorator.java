package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.chillingvan.canvasgl.ICanvasGL;
import com.chillingvan.canvasgl.glcanvas.GLPaint;
import com.littlezheng.ultrasound.ultrasound.Configuration;
import com.littlezheng.ultrasound.ultrasound.Depth;
import com.littlezheng.ultrasound.ultrasound.process.ImageCreator;
import com.littlezheng.ultrasound.ultrasound.util.GraphUtils;
import com.littlezheng.ultrasound.ultrasound.view.DrawInfo;
import com.littlezheng.ultrasound.ultrasound.view.image.TextImage;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/8/28/028.
 */

public class PlayVideoStrategyDecorator extends AbstractDisplayStrategyDecorator
        implements Observer{

    private Bitmap bmp = Bitmap.createBitmap(Configuration.THIRD_SAMPLE_MAX_WIDTH,
            Configuration.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_8888);
    private Map<Depth,DrawInfo> depthDrawInfoMap = new HashMap<>();
    private Depth depth = Depth.DEPTH_160;

    private GLPaint tipPaint = new GLPaint();
    private TextImage tip = new TextImage(GraphUtils.getSimpleTextPaint(40f, Color.WHITE), 2);
    private TextImage frameCount;

    public PlayVideoStrategyDecorator(Context context, DisplayStrategy displayStrategy) {
        super(context, displayStrategy);

        tipPaint.setColor(Color.RED);
        tipPaint.setStyle(Paint.Style.FILL);
        Paint p = GraphUtils.getSimpleTextPaint(30f, Color.CYAN);
        frameCount = new TextImage(p, (int)p.measureText("100/100"), 30);
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

        tip.setDrawPos(50,20);
        tip.drawText("回放");

        frameCount.setDrawPos(width-frameCount.getWidth()-20, height-frameCount.getHeight());
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        canvas.drawCircle(20, 40, 15, tipPaint);
        canvas.invalidateTextureContent(tip.getImage());
        canvas.drawBitmap(tip.getImage(), tip.getX(), tip.getY());

        canvas.invalidateTextureContent(frameCount.getImage());
        frameCount.drawText(ImageCreator.currentFrame+"/100");
        canvas.drawBitmap(frameCount.getImage(), frameCount.getX(), frameCount.getY());

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
