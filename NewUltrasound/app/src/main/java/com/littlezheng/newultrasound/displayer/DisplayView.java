package com.littlezheng.newultrasound.displayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.littlezheng.newultrasound.graphics.Container;
import com.littlezheng.newultrasound.graphics.Label;
import com.littlezheng.newultrasound.view.ContinuousSurfaceView;

/**
 * Created by Administrator on 2017/11/2/002.
 */

public class DisplayView extends ContinuousSurfaceView {

    private static final String TAG = "DisplayView";

    private Displayer displayer = new BlankDisplayer();

    public DisplayView(final Context context) {
        super(context);
        setKeepScreenOn(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        super.surfaceChanged(holder, format, width, height);
        displayer.init(width, height);
    }

    @Override
    protected void onSurfaceDraw(Canvas canvas) {
        displayer.draw(canvas);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void show(Displayer displayer){
        if(displayer == null) displayer = new BlankDisplayer();
        this.displayer = displayer;
        displayer.init(getWidth(), getHeight());
    }


    public Displayer getDisplayer() {
        return displayer;
    }

    /**
     * 捕捉当前显示的信息
     *
     * @return
     */
    public Bitmap getScreenImage(){
        Bitmap image = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(image);

        Container container = new Container();
        container.add(displayer);
        container.draw(c);

        return image;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return displayer.onTouchEvent(event);
    }

    private static class BlankDisplayer extends CommonDisplayer{
        Label tipsLabel = new Label();
        {
            add(tipsLabel);
        }
        @Override
        public void init(int width, int height) {
            super.init(width, height);
            tipsLabel.setTextSize(30);
            tipsLabel.setTextColor(Color.LTGRAY);
            tipsLabel.setText("按下运行键，然后选择一个工作模式");
            RectF bounds = tipsLabel.getBounds();
            tipsLabel.setLocation((width-bounds.width())/2, (height-bounds.height())/2);
        }
    }

}
