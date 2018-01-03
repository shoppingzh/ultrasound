package com.littlezheng.ultrasound4.ultrasound.display;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.littlezheng.ultrasound4.framework.view.DisplayStrategy;
import com.littlezheng.ultrasound4.framework.view.MultipleDisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.Resizeable;

/**
 * Created by Administrator on 2017/9/23/023.
 */

public class FullScreenDisplayStrategy extends MultipleDisplayStrategy {

    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Resizeable resizeable = (Resizeable) mContext;
            resizeable.toggleFullScreen();
            return super.onDoubleTap(e);
        }
    });

    public FullScreenDisplayStrategy(Context context, DisplayStrategy strategy) {
        super(context, strategy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

}
