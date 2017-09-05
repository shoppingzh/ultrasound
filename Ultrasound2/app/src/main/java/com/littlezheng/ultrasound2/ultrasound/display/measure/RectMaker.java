package com.littlezheng.ultrasound2.ultrasound.display.measure;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.littlezheng.ultrasound2.ultrasound.util.MeasureUtils;

/**
 * Created by Administrator on 2017/8/24/024.
 */

public class RectMaker extends ShapeMaker {

    private PointF leftTop = new PointF();
    private PointF rightBottom = new PointF();

    public RectMaker() {
        super(3);
    }

    @Override
    public void click(float x, float y) {
        super.click(x, y);
        switch (step){
            case 1:
                leftTop.set(x, y);
                break;
            case 2:
                rightBottom.set(x, y);
                break;
            case 3:
                break;
        }
    }

    @Override
    public void move(float x, float y) {
        super.move(x, y);
        getCloser(x, y).set(x, y);
    }

    /**
     * 获取更近的点
     * @param x
     * @param y
     * @return
     */
    private PointF getCloser(float x, float y){
        PointF p = new PointF(x, y);
        double firstLen = MeasureUtils.length(p, leftTop);
        double secondLen = MeasureUtils.length(p, rightBottom);
        return firstLen < secondLen ? leftTop : rightBottom;
    }

    @Override
    public void make(Canvas canvas) {
        switch (step){
            case 1:
                canvas.drawCircle(leftTop.x, leftTop.y, 10, pointPaint);
                break;
            case 2:
                canvas.drawCircle(leftTop.x, leftTop.y, 10, pointPaint);
                canvas.drawCircle(rightBottom.x, rightBottom.y, 10, pointPaint);
                break;
            case 3:
                canvas.drawCircle(leftTop.x, leftTop.y, 10, pointPaint);
                canvas.drawCircle(rightBottom.x, rightBottom.y, 10, pointPaint);
                canvas.drawRect(leftTop.x,leftTop.y,rightBottom.x,rightBottom.y,linePaint);
                canvas.drawText("周长："+MeasureUtils.rectPerimeter(leftTop,rightBottom)+", 面积："+MeasureUtils.rectArea(leftTop,rightBottom),
                        rightBottom.x, rightBottom.y, textPaint);
                break;
        }
    }
}
