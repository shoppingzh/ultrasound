package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/8/29/029.
 */

public abstract class BaseDisplayStrategy implements DisplayStrategy {

    protected Context mContext;

    public BaseDisplayStrategy(Context context) {
        mContext = context;
    }

    @Override
    public void init(int width, int height) {
        //no op
//        Log.d("BaseDisplayStrategy",getClass().getName()+"初始化");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void cleanup() {
        //no op
    }

}
