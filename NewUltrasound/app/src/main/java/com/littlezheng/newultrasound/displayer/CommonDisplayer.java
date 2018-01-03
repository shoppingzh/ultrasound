package com.littlezheng.newultrasound.displayer;

import android.graphics.Color;
import android.view.MotionEvent;

import com.littlezheng.newultrasound.core.Bitmaps;
import com.littlezheng.newultrasound.graphics.ClipImageBox;
import com.littlezheng.newultrasound.graphics.Label;
import com.littlezheng.newultrasound.graphics.ListBox;
import com.littlezheng.newultrasound.graphics.TimeLabel;

/**
 * Created by Administrator on 2017/11/13/013.
 */

public abstract class CommonDisplayer extends Displayer {

    static final TimeLabel timeLabel = new TimeLabel(); //时间
    static final ListBox paramListBox = new ListBox();  //参数列表
    static final Label runStateLabel = new Label(); //运行状态
    static final Label modeLabel = new Label(); //工作模式
    static final ClipImageBox colorBarPb = new ClipImageBox(Bitmaps.colorBar); //颜色条
    static final Label backFatLabel = new Label();

    static {
        initComponents();
    }

    private static void initComponents() {
        timeLabel.setTextSize(25);
        timeLabel.setTextColor(Color.CYAN);

        paramListBox.setTextSize(20);
        paramListBox.setTextColor(Color.WHITE);
        paramListBox.setSpacing(20);
        paramListBox.addElement("对比度：16");
        paramListBox.addElement("亮度：32");
        paramListBox.addElement("总增：16");
        paramListBox.addElement("近增：16");
        paramListBox.addElement("远增：16");
        paramListBox.addElement("深度：160mm");
        paramListBox.addElement("速度(M)：3");

        runStateLabel.setText("冻结");
        runStateLabel.setTextSize(35);
        runStateLabel.setTextColor(Color.WHITE);

        modeLabel.setTextSize(25);
        modeLabel.setTextColor(Color.WHITE);

        backFatLabel.setTextSize(25);
        backFatLabel.setTextColor(Color.YELLOW);
    }

    public static void updateRunState(boolean freeze){
        runStateLabel.setText(freeze ? "冻结" : "运行");
    }

    public static void updateContrast(int value){
        paramListBox.setText(0, "对比度：" + value);
    }

    public static void updateLightness(int value){
        paramListBox.setText(1, "亮度：" + value);
    }

    public static void updateGain(int value){
        paramListBox.setText(2, "总增：" + value);
    }

    public static void updateNearGain(int value){
        paramListBox.setText(3, "近增：" + value);
    }

    public static void updateFarGain(int value){
        paramListBox.setText(4, "远增：" + value);
    }

    public static void updateDepth(int value){
        paramListBox.setText(5, "深度：" + (value * 10 + 30) + "mm");
    }

    public static void updateBackFat(float fat){
        backFatLabel.setText("背膘：" + String.format("%.3f", fat));
    }

    public static void updateSpeed(int value){
        paramListBox.setText(6, "速度(M)：" + value);
    }

    public CommonDisplayer() {
        add(timeLabel);
        add(paramListBox);
        add(runStateLabel);
        add(modeLabel);
        add(colorBarPb);
        add(backFatLabel);
    }

    public void init(int width, int height) {
        timeLabel.setLocation(20, 50);
        paramListBox.setLocation(20, 120);
        runStateLabel.setLocation(50, height - 20);
        modeLabel.setLocation(width - modeLabel.getBounds().width() - 10, 50);
        colorBarPb.setDrawBounds(width-70, 100, width-30, height-100);
        backFatLabel.setLocation(width-300, 150);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(backFatLabel.getBounds().contains(event.getX(), event.getY())){
            backFatLabel.setText(null);
        }
        return super.onTouchEvent(event);
    }

}
