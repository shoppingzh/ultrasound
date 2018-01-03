package com.littlezheng.ultrasound4.framework.view;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/9/20/020.
 */

public abstract class AbstractDisplayStrategy implements DisplayStrategy {

    protected Context mContext;
    protected int width;
    protected int height;

    public AbstractDisplayStrategy(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
