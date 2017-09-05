package com.littlezheng.drawingboard.strategy;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public abstract class AbsDisplayStrategy implements DisplayStrategy {

    protected Context mContext;

    public AbsDisplayStrategy(Context context){
        mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
