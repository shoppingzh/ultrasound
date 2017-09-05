package com.littlezheng.ultrasound3.ultrasound.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;

import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Mode;
import com.littlezheng.ultrasound3.ultrasound.display.DisplayView;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.BBModeDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.BMModeDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.BModeDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.ColorBarDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.ImageShowDisplayStrategy;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.MModeDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.ParamDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.ReplayDisplayStrategy;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.StateDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.TimeStrategy;

import java.util.Observable;

/**
 * Created by Administrator on 2017/9/2/002.
 */

public class ViewController {

    private UContext uContext;
    private final DisplayView displayView;

    private final Context mContext;

    private TimeStrategy timeStrategy;
    private ParamDisplayStrategyDecorator paramStrategy;
    private StateDisplayStrategyDecorator stateStrategy;
    private ColorBarDisplayStrategyDecorator colorBarStrategy;

    private BModeDisplayStrategyDecorator bModeStrategy;
    private MModeDisplayStrategyDecorator mModeStrategy;
    private BBModeDisplayStrategyDecorator bbModeStrategy;
    private BMModeDisplayStrategyDecorator bmModeStrategy;

    private ImageShowDisplayStrategy imageShowStrategy;
    private ReplayDisplayStrategy replayStrategy;

    public ViewController(UContext uContext){
        this.uContext = uContext;
        mContext = uContext.getAndroidContext();
        displayView = new DisplayView(mContext);

        initStrategy();
    }

    private void initStrategy() {
        timeStrategy = new TimeStrategy(mContext);
        paramStrategy = new ParamDisplayStrategyDecorator(uContext, timeStrategy);
        stateStrategy = new StateDisplayStrategyDecorator(uContext, paramStrategy);
        colorBarStrategy = new ColorBarDisplayStrategyDecorator(uContext, stateStrategy);

        bModeStrategy = new BModeDisplayStrategyDecorator(uContext, colorBarStrategy);
        mModeStrategy = new MModeDisplayStrategyDecorator(uContext, colorBarStrategy);
        bbModeStrategy = new BBModeDisplayStrategyDecorator(uContext, colorBarStrategy);
        bmModeStrategy = new BMModeDisplayStrategyDecorator(uContext, colorBarStrategy);

        imageShowStrategy = new ImageShowDisplayStrategy(mContext);
        replayStrategy = new ReplayDisplayStrategy(uContext);
    }

    public void show(ViewGroup viewGroup){
        viewGroup.addView(displayView);
    }

    public void hide(ViewGroup viewGroup){
        viewGroup.removeView(displayView);
    }

    /**
     * 设定工作模式
     * @param mode 模式
     * @param source 刷新源
     */
    public void setMode(Mode mode, Observable source){
        switch (mode){
            case MODE_B:
                displayView.changeStrategy(bModeStrategy);
                bModeStrategy.addPixelsRefreshSource(source);
                mModeStrategy.removePixelsRefreshSource(source);
                bbModeStrategy.removePixelsRefreshSource(source);
                bmModeStrategy.removePixelsRefreshSource(source);
                break;
            case MODE_M:
                displayView.changeStrategy(mModeStrategy);
                mModeStrategy.addPixelsRefreshSource(source);
                bModeStrategy.removePixelsRefreshSource(source);
                bbModeStrategy.removePixelsRefreshSource(source);
                bmModeStrategy.removePixelsRefreshSource(source);
                break;
            case MODE_BB:
                displayView.changeStrategy(bbModeStrategy);
                bbModeStrategy.addPixelsRefreshSource(source);
                bModeStrategy.removePixelsRefreshSource(source);
                mModeStrategy.removePixelsRefreshSource(source);
                bmModeStrategy.removePixelsRefreshSource(source);
                break;
            case MODE_BM:
                displayView.changeStrategy(bmModeStrategy);
                bmModeStrategy.addPixelsRefreshSource(source);
                bModeStrategy.removePixelsRefreshSource(source);
                mModeStrategy.removePixelsRefreshSource(source);
                bbModeStrategy.removePixelsRefreshSource(source);
                break;
        }
    }

    /**
     * 保存当前视图的位图
     */
    public void saveViewBitmap() {
        displayView.saveCurrentBitmap();
    }

    /**
     * 显示图片
     * @param image
     */
    public void displayImage(Bitmap image) {
        imageShowStrategy.changeImage(image);
        displayView.changeStrategy(imageShowStrategy);
    }

    /**
     * 切换到回放模式
     * @param source
     */
    public void replayMode(Observable source) {
        displayView.changeStrategy(replayStrategy);
        source.addObserver(replayStrategy);
    }

}
