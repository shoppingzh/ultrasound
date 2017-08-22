package com.littlezheng.displaymodule.display.strategy;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.chillingvan.canvasgl.glview.GLContinuousView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public abstract class AbsDisplayStrategy implements DisplayStrategy{

    private static final String TAG = "AbsDisplayStrategy";
    protected Context mContext;

    public AbsDisplayStrategy(Context context) {
        mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


}
