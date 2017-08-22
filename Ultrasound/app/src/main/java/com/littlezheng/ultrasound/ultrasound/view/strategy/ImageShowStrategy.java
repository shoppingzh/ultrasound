package com.littlezheng.ultrasound.ultrasound.view.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.CanvasGL;
import com.chillingvan.canvasgl.ICanvasGL;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public class ImageShowStrategy extends AbsDisplayStrategy {

    private static final float MAX_SCALE_RATIO = 3f;

    private int windowWidth;
    private int windowHeight;

    private Bitmap image;
    private CanvasGL.BitmapMatrix m = new CanvasGL.BitmapMatrix();
    private float scaleRatio = 1f;
    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            final float x = e.getX();
            final float y = e.getY();
            if(scaleRatio < MAX_SCALE_RATIO){
                scaleRatio++;
                m.postScale(scaleRatio, scaleRatio, x, y);
            }else{
                m.reset();
                m.postScale((float)windowWidth/image.getWidth(),(float)windowHeight/image.getHeight());
                scaleRatio = 1f;
            }

            return super.onDoubleTap(e);
        }


        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

    });

    public ImageShowStrategy(Context context) {
        super(context);
    }

    public void init(int width, int height){
        m.reset();
        windowWidth = width;
        windowHeight = height;
    }

    public void loadImage(Bitmap newImage){
        if(image != null && !image.isRecycled()){
            image.recycle();
            image = null;
        }
        image = newImage;
        m.postScale((float)windowWidth/image.getWidth(),(float)windowHeight/image.getHeight());
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.drawBitmap(image,m);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }
}
