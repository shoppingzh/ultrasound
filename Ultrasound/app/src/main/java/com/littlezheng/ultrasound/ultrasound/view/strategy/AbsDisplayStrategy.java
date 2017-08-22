package com.littlezheng.ultrasound.ultrasound.view.strategy;

import android.content.Context;
import android.view.MotionEvent;

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
