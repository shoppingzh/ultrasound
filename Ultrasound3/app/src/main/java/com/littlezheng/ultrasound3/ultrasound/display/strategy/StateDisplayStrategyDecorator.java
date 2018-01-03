package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.graphics.Color;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.StateSwitcher;
import com.littlezheng.ultrasound3.ultrasound.display.image.TextImage;
import com.littlezheng.ultrasound3.ultrasound.util.GraphUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class StateDisplayStrategyDecorator extends BaseDisplayStrategyDecorator
        implements Observer {

    private UContext uContext;
    private StateSwitcher stateSwitcher;
    private TextImage stateText;

    public StateDisplayStrategyDecorator(UContext uContext, DisplayStrategy strategy) {
        super(uContext.getAndroidContext(), strategy);
        this.uContext = uContext;
        stateSwitcher = uContext.getStateSwitcher();
        stateText = new TextImage(GraphUtils.getSimpleTextPaint(50f, Color.WHITE), 2);
        //状态感知
        stateSwitcher.addObserver(this);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        stateText.setDrawPos(20, height - stateText.getHeight() - 15);
        draw();
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        canvas.invalidateTextureContent(stateText.getImage());
        canvas.drawBitmap(stateText.getImage(), stateText.getX(), stateText.getY());
    }

    @Override
    public void update(Observable o, Object arg) {
        draw();
    }

    private void draw() {
        stateText.drawText(stateSwitcher.isFrozen() ? "冻结" : "运行");
    }


}
