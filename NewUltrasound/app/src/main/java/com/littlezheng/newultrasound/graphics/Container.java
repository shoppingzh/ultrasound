package com.littlezheng.newultrasound.graphics;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2017/11/3/003.
 */

public class Container extends Control implements Iterable<Control> {

    private static final String TAG = "Container";

    private List<Control> controls = new CopyOnWriteArrayList<>();

    @Override
    public void draw(Canvas canvas) {
        for (Control c : controls) {
            c.draw(canvas);
        }
        //drawBounds(canvas, 2, Color.YELLOW);
    }

    public void add(Control c) {
        if(c == null || c == this) return;
        controls.add(c);
        c.setParent(this);
    }

    public void add(Control c, int index) {
        if(c == null || c == this) return;
        controls.add(index, c);
        c.setParent(this);
    }

    private boolean removeChild(Control c) {
        boolean result = controls.remove(c);
        if(result) c.setParent(null);
        return result;
    }

    /**
     * 递归删除指定控件
     *
     * @param c
     */
    public void remove(Control c) {
        if(removeChild(c)) return;
        for(Control control : controls){
            if(control instanceof Container){
                control.remove(c);
            }
        }
    }

    @Override
    public Iterator iterator() {
        return controls.iterator();
    }

    @Override
    public RectF getBounds() {
        float minLeft = 0, minTop = 0, maxRight = 0, maxBottom = 0;
        for (Control control : controls) {
            RectF bounds = control.getBounds();
            float left = bounds.left,
                    top = bounds.top,
                    right = bounds.right,
                    bottom = bounds.bottom;
            if (minLeft > left) minLeft = left;
            if (minTop > top) minTop = top;
            if (maxRight < right) maxRight = right;
            if (maxBottom < bottom) maxBottom = bottom;
        }
        return new RectF(minLeft, minTop, maxRight, maxBottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (Control c : controls) {
            if (c.getBounds().contains(event.getX(), event.getY())) {
                Log.d(TAG, "当前触摸组件：" + c);
                return c.onTouchEvent(event);
            }
        }
        return true;
    }


}
