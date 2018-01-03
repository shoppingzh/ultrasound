package com.littlezheng.ultrasound4.ultrasound.display;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.ViewGroup;

import com.littlezheng.ultrasound4.framework.graphics.measuremaker.ShapeMaker;
import com.littlezheng.ultrasound4.framework.view.DisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.Mode;
import com.littlezheng.ultrasound4.ultrasound.display.draw.TextDrawCommand;
import com.littlezheng.ultrasound4.ultrasound.display.workmode.BBMode;
import com.littlezheng.ultrasound4.ultrasound.display.workmode.BMMode;
import com.littlezheng.ultrasound4.ultrasound.display.workmode.BMode;
import com.littlezheng.ultrasound4.ultrasound.display.workmode.MMode;
import com.littlezheng.ultrasound4.ultrasound.display.workmode.WorkModeDisplayStrategy;

import java.io.File;
import java.util.Observable;

/**
 * Created by Administrator on 2017/9/23/023.
 */

public class WindowController {

    private UContext uContext;
    private Context mContext;

    private DisplayView displayView;

    private TimeDisplayStrategy timeDisplay;
    private ParamsDisplayStrategy paramsDisplay;
    private RunStateDisplayStrategy runStateDisplay;
    private ColorBarDisplayStrategy colorBarDisplay;
    private FullScreenDisplayStrategy fullScreenDisplay;
    private MeasureDisplayStrategy measureDisplay;
    private ImageShowDisplayStrategy imageShowDisplay;

    //4种工作模式
    private WorkModeDisplayStrategy bmode;
    private WorkModeDisplayStrategy mmode;
    private WorkModeDisplayStrategy bbmode;
    private WorkModeDisplayStrategy bmmode;

    private TextDrawCommand backFatCommand;

    public WindowController(UContext uContext, ViewGroup wrapper) {
        this.uContext = uContext;
        mContext = uContext.getAndroidContext();

        timeDisplay = new TimeDisplayStrategy(mContext);
        paramsDisplay = new ParamsDisplayStrategy(mContext, timeDisplay);
        paramsDisplay.addParams(uContext.getParams());
        runStateDisplay = new RunStateDisplayStrategy(uContext, paramsDisplay);
        colorBarDisplay = new ColorBarDisplayStrategy(uContext, runStateDisplay);
        fullScreenDisplay = new FullScreenDisplayStrategy(mContext, colorBarDisplay);
        measureDisplay = new MeasureDisplayStrategy(mContext, fullScreenDisplay);
        imageShowDisplay = new ImageShowDisplayStrategy(mContext);

        bmode = new BMode(uContext, fullScreenDisplay);
        mmode = new MMode(uContext, fullScreenDisplay);
        bbmode = new BBMode(uContext, fullScreenDisplay);
        bmmode = new BMMode(uContext, fullScreenDisplay);

        displayView = new DisplayView(mContext);
        wrapper.addView(displayView);

        displayView.setDisplayStrategy(bmode);
    }

    /**
     * 更改工作模式
     *
     * @param mode          新的工作模式
     * @param refreshSource 工作模式显示策略的刷新源
     */
    public void changeMode(Mode mode, Observable refreshSource) {
        switch (mode) {
            case MODE_B:
                displayView.setDisplayStrategy(bmode);
                bmode.addRefreshSource(refreshSource);
                mmode.removeRefreshSource(refreshSource);
                bbmode.removeRefreshSource(refreshSource);
                bmmode.removeRefreshSource(refreshSource);
                break;
            case MODE_M:
                displayView.setDisplayStrategy(mmode);
                mmode.addRefreshSource(refreshSource);
                bmode.removeRefreshSource(refreshSource);
                bbmode.removeRefreshSource(refreshSource);
                bmmode.removeRefreshSource(refreshSource);
                break;
            case MODE_BB:
                displayView.setDisplayStrategy(bbmode);
                bbmode.addRefreshSource(refreshSource);
                bmode.removeRefreshSource(refreshSource);
                mmode.removeRefreshSource(refreshSource);
                bmmode.removeRefreshSource(refreshSource);
                break;
            case MODE_BM:
                displayView.setDisplayStrategy(bmmode);
                bmmode.addRefreshSource(refreshSource);
                bmode.removeRefreshSource(refreshSource);
                mmode.removeRefreshSource(refreshSource);
                bbmode.removeRefreshSource(refreshSource);
                break;
        }
    }

    /**
     * 开始屏幕捕捉
     */
    public void startCaptureScreen() {
        displayView.startCaptureScreen();
    }

    public void drawText(String text, Paint p, int x, int y) {
        if (x < 0) {
            x = displayView.getWindowWidth() + x;
        }
        if (y < 0) {
            y = displayView.getWindowHeight() + y;
        }
        displayView.draw(new TextDrawCommand(text, p, x, y));
    }

    public void drawText(String text, int x, int y) {
        Paint p = new Paint();
        p.setTextSize(30);
        p.setColor(Color.WHITE);
        drawText(text, p, x, y);
    }

    public void showBackFat(float newFat) {
        if (newFat == -1) return;
        if (backFatCommand != null) {
            displayView.cancel(backFatCommand);
        }
        Paint p = new Paint();
        p.setTextSize(30);
        p.setColor(Color.CYAN);
        String newFatStr = "背膘：" + String.format("%.2f", newFat);
        backFatCommand = new TextDrawCommand(newFatStr, p,
                (int) (displayView.getWindowWidth() - p.measureText(newFatStr) - 60), 80);
        displayView.draw(backFatCommand);
    }

    public void showImage(File image) {
        imageShowDisplay.changeImage(image);
        displayView.setDisplayStrategy(imageShowDisplay);
    }

    public void startMeasure(ShapeMaker shapeMaker) {
        DisplayStrategy old;
        if ((old = displayView.getDisplayStrategy()) != measureDisplay) {
            measureDisplay.wrap(old);
            clearMeasures();
        }
        measureDisplay.addShapeMaker(shapeMaker);
        displayView.setDisplayStrategy(measureDisplay);
    }

    public void clearMeasures() {
        measureDisplay.removeCurrent();
        measureDisplay.removeAllShapeMaker();
    }

}
