package com.littlezheng.ultrasound4.framework.graphics.measuremaker;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by Administrator on 2017/8/24/024.
 * <p>
 * 两步即可画出一条直线，第一次点击画出第一个点，第二次点击直接画出直线
 */

public class LineMaker extends ShapeMaker {

    //确定直线的两个点
    private PointF first = new PointF();
    private PointF second = new PointF();

    public LineMaker() {
        super(3);
    }

    @Override
    public void click(float x, float y) {
        super.click(x, y);
        switch (step) {
            case 1:
                first.set(x, y);
                break;
            case 2:
                second.set(x, y);
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
     *
     * @param x
     * @param y
     * @return
     */
    private PointF getCloser(float x, float y) {
        PointF p = new PointF(x, y);
        double firstLen = MeasureUtils.length(p, first);
        double secondLen = MeasureUtils.length(p, second);
        return firstLen < secondLen ? first : second;
    }

    @Override
    public void make(Canvas canvas) {
        switch (step) {
            case 1:
                canvas.drawCircle(first.x, first.y, 10, pointPaint);
                break;
            case 2:
                canvas.drawCircle(first.x, first.y, 10, pointPaint);
                canvas.drawCircle(second.x, second.y, 10, pointPaint);
                break;
            case 3:
                canvas.drawCircle(first.x, first.y, 10, pointPaint);
                canvas.drawCircle(second.x, second.y, 10, pointPaint);
                canvas.drawLine(first.x, first.y, second.x, second.y, linePaint);
                canvas.drawText("长度：" + String.format("%.2f", MeasureUtils.length(first, second)), second.x, second.y, textPaint);
                break;
        }
    }

}
