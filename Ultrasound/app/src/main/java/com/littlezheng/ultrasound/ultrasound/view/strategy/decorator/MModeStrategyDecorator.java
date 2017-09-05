package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.Configuration;
import com.littlezheng.ultrasound.ultrasound.process.ImageCreator;
import com.littlezheng.ultrasound.ultrasound.transfer.Param;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public class MModeStrategyDecorator extends AbstractDisplayStrategyDecorator implements Observer {

    private Bitmap bmp = Bitmap.createBitmap(500, Configuration.UDP_USEFUL_DATA_LEN,
            Bitmap.Config.ARGB_8888);
    private Rect src,dst;

    private Toast tipToast;
    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if(speed == null) return false;

            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            if(tipToast != null) tipToast.cancel();
            if (x > 0) {
                speed.increase();
                tipToast = Toast.makeText(mContext,"速度："+speed.getCurrValue(),Toast.LENGTH_SHORT);
                tipToast.show();
            } else if (x < 0) {
                speed.decrease();
                tipToast = Toast.makeText(mContext,"速度："+speed.getCurrValue(),Toast.LENGTH_SHORT);
                tipToast.show();
            }
            return true;
        }

    });

    //速度控制参数
    private Param speed;

    public MModeStrategyDecorator(Context context, DisplayStrategy strategy) {
        super(context,strategy);
    }

    public void init(int width, int height){
        src = new Rect(0,0,bmp.getWidth(),bmp.getHeight());
        int wid = (int) (width * 0.6f), hei = wid / 500 * 400;
        int left = (width-wid)/2, top = (height-hei)/2;
        dst = new Rect(left,top,wid+left,top+hei);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        canvas.invalidateTextureContent(bmp);
        canvas.drawBitmap(bmp,src,dst);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        bmp.setPixels(ImageCreator.mPixels,0,500,0,0,500,400);
    }

    public void setSpeed(Param speed) {
        this.speed = speed;
    }
}
