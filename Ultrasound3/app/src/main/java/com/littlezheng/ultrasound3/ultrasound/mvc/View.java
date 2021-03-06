package com.littlezheng.ultrasound3.ultrasound.mvc;

import android.content.Context;
import android.view.ViewGroup;

import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Mode;
import com.littlezheng.ultrasound3.ultrasound.display.DisplayView;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.ColorBarDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.FullScreenStrategy;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.ImageShowDisplayStrategy;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.ParamDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.ReplayDisplayStrategy;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.StateDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.TimeStrategy;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.old.BBModeDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.old.BMModeDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.old.BModeDisplayStrategyDecorator;
import com.littlezheng.ultrasound3.ultrasound.display.strategy.old.MModeDisplayStrategyDecorator;

import java.io.File;
import java.util.Observable;

/**
 * Created by Administrator on 2017/9/2/002.
 */

public class View {

    private UContext uContext;
    private final DisplayView displayView;

    private final Context mContext;

    private TimeStrategy timeStrategy;
    private ParamDisplayStrategyDecorator paramStrategy;
    private StateDisplayStrategyDecorator stateStrategy;
    private ColorBarDisplayStrategyDecorator colorBarStrategy;
    private FullScreenStrategy fullScreenStrategy;

    private BModeDisplayStrategyDecorator bModeStrategy;
    //    private BModeStrategy bModeStrategy;
    private MModeDisplayStrategyDecorator mModeStrategy;
    private BBModeDisplayStrategyDecorator bbModeStrategy;
    //    private BBModeStrategy bbModeStrategy;
//    private BMModeDisplayStrategyDecorator bmModeStrategy;
//    private BMModeStrategy bmModeStrategy;
    private BMModeDisplayStrategyDecorator bmModeStrategy;

    private ImageShowDisplayStrategy imageShowStrategy;
    private ReplayDisplayStrategy replayStrategy;

    public View(UContext uContext) {
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
        fullScreenStrategy = new FullScreenStrategy(mContext, colorBarStrategy);

//        bModeStrategy = new BModeStrategy(uContext, fullScreenStrategy);
        bModeStrategy = new BModeDisplayStrategyDecorator(uContext, fullScreenStrategy);
        mModeStrategy = new MModeDisplayStrategyDecorator(uContext, fullScreenStrategy);
        bbModeStrategy = new BBModeDisplayStrategyDecorator(uContext, fullScreenStrategy);
//        bbModeStrategy = new BBModeStrategy(uContext, fullScreenStrategy);
//        bmModeStrategy = new BMModeStrategy(uContext, fullScreenStrategy);
        bmModeStrategy = new BMModeDisplayStrategyDecorator(uContext, fullScreenStrategy);

        imageShowStrategy = new ImageShowDisplayStrategy(mContext);
        replayStrategy = new ReplayDisplayStrategy(uContext);
    }

    public void show(ViewGroup viewGroup) {
        viewGroup.addView(displayView);
    }

    public void hide(ViewGroup viewGroup) {
        viewGroup.removeView(displayView);
    }

    /**
     * 设定工作模式
     *
     * @param mode   模式
     * @param source 刷新源
     */
    public void setMode(Mode mode, Observable source) {
        switch (mode) {
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
     *
     * @param image
     */
    public void displayImage(File image) {
        imageShowStrategy.changeImage(image);
        displayView.changeStrategy(imageShowStrategy);
    }

    /**
     * 切换到回放模式
     *
     * @param source
     */
    public void replayMode(Observable source) {
        displayView.changeStrategy(replayStrategy);
        source.addObserver(replayStrategy);
    }

}
