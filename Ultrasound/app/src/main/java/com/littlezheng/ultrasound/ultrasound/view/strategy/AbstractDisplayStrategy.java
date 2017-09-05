package com.littlezheng.ultrasound.ultrasound.view.strategy;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/8/21/021.
 */

public abstract class AbstractDisplayStrategy implements DisplayStrategy{

    private static final String TAG = "AbstractDisplayStrategy";

    protected Context mContext;

    public AbstractDisplayStrategy(Context context) {
        mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


}
