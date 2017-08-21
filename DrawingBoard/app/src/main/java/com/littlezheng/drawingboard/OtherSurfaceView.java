package com.littlezheng.drawingboard;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2017/8/19/019.
 */

public class OtherSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;

    public OtherSurfaceView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSPARENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Canvas c = holder.lockCanvas();
                c.drawBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.aaa),0,0,null);
                holder.unlockCanvasAndPost(c);
            }
        }).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
