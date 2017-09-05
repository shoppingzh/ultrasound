package com.littlezheng.drawingboard.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.widget.Toast;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.drawingboard.R;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public class BasicStrategy extends AbsDisplayStrategy {

    private Bitmap bmp;

    public BasicStrategy(Context context) {
        super(context);
        bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.drawBitmap(bmp,0,0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            Toast.makeText(mContext,"哇，美女！",Toast.LENGTH_SHORT).show();
        }
        return super.onTouchEvent(event);
    }
}
