package com.littlezheng.newultrasound.graphics;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/5/005.
 */

public class ListBox extends Control {

    private List<Label> labels = new ArrayList<>();
    private float spacing = 5f;

    @Override
    public void draw(Canvas c) {
        for (Control control : labels) {
            control.draw(c);
        }
//        drawBounds(c, 10, Color.YELLOW);
    }

    /**
     * 创建一个行标签，用于放置在ListBox中显示
     *
     * @param text 标签文本
     * @return
     */
    private Label createLineLabel(String text) {
        Label label = new Label();
        label.setText(text);
        label.setTextSize(paint.getTextSize());
        label.setTextColor(paint.getColor());
        return label;
    }

    /**
     * 在列表中添加一行数据
     *
     * @param text 该行显示的文本
     */
    public void addElement(String text) {
        Label label = createLineLabel(text);
        labels.add(label);

        int size = 0;
        if ((size = labels.size()) <= 1) {
            label.setLocation(x, y);
        } else {
            RectF lastParamBounds = labels.get(size - 2).getBounds();
            label.setLocation(x, lastParamBounds.bottom + lastParamBounds.height() + spacing);
        }
    }

    /**
     * 重新排列列表，通常在以下情况下：
     * 1. 设置新的画笔
     * 2. 设置新的显示位置
     * 3. 删除某行
     * 4. 重新设置行间距
     */
    protected void rearrange() {
        if (labels.isEmpty()) return;

        Label first = labels.get(0);
        first.setLocation(x, y);
        for (int i = 1; i < labels.size(); i++) {
            Label param = labels.get(i);
            Control last = labels.get(i - 1);
            RectF lastParamBounds = last.getBounds();
            param.setLocation(x, lastParamBounds.bottom + lastParamBounds.height() + spacing);
        }
    }


    /**
     * 在某个特点行插入一行数据
     *
     * @param index 行索引
     * @param text  插入的行的文本
     */
    public void insertElement(int index, String text) {
        if (index > labels.size()) return; //无法被添加

        Label label = createLineLabel(text);
        labels.add(index, label);

        rearrange();
    }

    /**
     * 删除列表中的某行，如果没有该行则不进行任何动作
     *
     * @param index 行的索引
     */
    public void removeElement(int index) {
        if (labels.remove(index) != null) {
            rearrange();
        }
    }

    /**
     * 为列表中的某行重新设置文本
     *
     * @param i       行索引
     * @param newText 新的显示文本
     */
    public void setText(int i, String newText) {
        Label label;
        if ((label = labels.get(i)) != null) {
            label.setText(newText);
        }
    }

    @Override
    public RectF getBounds() {
        if (labels.isEmpty()) return new RectF();

        float maxRight = 0;
        for (Control control : labels) {
            float right = control.getBounds().right;
            if (maxRight < right) {
                maxRight = right;
            }
        }

        Control first = labels.get(0);
        Control last = labels.get(labels.size() - 1);
        RectF bounds = new RectF(first.getBounds().left, first.getBounds().top,
                maxRight, last.getBounds().bottom);
        return bounds;
    }

    public void setSpacing(float spacing) {
        if (spacing < 0) return;
        this.spacing = spacing;
        rearrange();
    }

    @Override
    public void setLocation(float x, float y) {
        super.setLocation(x, y);
        rearrange();
    }

    public void setTextSize(float textSize) {
        paint.setTextSize(textSize);
        for (Label label : labels) {
            label.setTextSize(textSize);
        }
    }

    /**
     * 指定列表中行文本的颜色，该操作将会覆盖setTextColor(int index, int textColor)操作的结果
     *
     * @param textColor 文本颜色
     */
    public void setTextColor(int textColor) {
        paint.setColor(textColor);
        for (Label label : labels) {
            label.setTextColor(textColor);
        }
    }

    /**
     * 更改指定行的文本颜色
     *
     * @param index     行索引
     * @param textColor 新的颜色
     */
    public void setTextColor(int index, int textColor) {
        Label label = null;
        if ((label = labels.get(index)) != null) label.setTextColor(textColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                setLocation(event.getX() - getBounds().width() / 2,
                        event.getY() - getBounds().height() / 2);
                break;
        }
        return true;
    }

}
