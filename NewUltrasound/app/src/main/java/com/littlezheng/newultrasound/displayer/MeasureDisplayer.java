package com.littlezheng.newultrasound.displayer;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.littlezheng.newultrasound.displayer.measuremaker.NonShapeMaker;
import com.littlezheng.newultrasound.displayer.measuremaker.ShapeMaker;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2017/11/18/018.
 */

public class MeasureDisplayer extends WrapperDisplayer {

    private List<ShapeMaker> shapeMakers = new CopyOnWriteArrayList<>();
    private ShapeMaker nonShapeMaker = new NonShapeMaker();
    private ShapeMaker shapeMaker = nonShapeMaker;

    public MeasureDisplayer(Displayer displayer) {
        super(displayer);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (ShapeMaker shapeMaker : shapeMakers) {
            shapeMaker.make(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                shapeMaker.click(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                shapeMaker.move(x, y);
                break;
        }
        return true;
    }

    public void addShapeMaker(ShapeMaker shapeMaker) {
        if (shapeMaker == null) return;
        if (!this.shapeMaker.stepEnd()) {
            return;
        }
        this.shapeMaker = shapeMaker;
        shapeMakers.add(shapeMaker);
    }

    public void removeAllShapeMaker() {
        shapeMakers.clear();
        shapeMaker = nonShapeMaker;
    }

    public void removeCurrent() {
        if (shapeMaker.stepEnd()) return;
        if (shapeMakers.isEmpty()) return;
        shapeMakers.remove(shapeMakers.size() - 1);
        shapeMaker = nonShapeMaker;
    }

}
