package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.littlezheng.ultrasound3.activity.Resizeable;

/**
 * Created by Administrator on 2017/9/10/010.
 */

public class FullScreenStrategy extends BaseDisplayStrategyDecorator {

    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Resizeable resizeable = (Resizeable) mContext;
            resizeable.toggleFullScreen();
            return super.onDoubleTap(e);
        }
    });

    public FullScreenStrategy(Context context, DisplayStrategy strategy) {
        super(context, strategy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }


}
