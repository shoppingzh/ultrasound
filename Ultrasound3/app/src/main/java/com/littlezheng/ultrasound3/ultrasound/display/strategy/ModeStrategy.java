package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.graphics.Color;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.ModeSwitcher;
import com.littlezheng.ultrasound3.ultrasound.display.image.TextImage;
import com.littlezheng.ultrasound3.ultrasound.util.GraphUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/9/009.
 */

public abstract class ModeStrategy extends BaseDisplayStrategyDecorator
        implements Observer {

    protected UContext uContext;
    protected ModeSwitcher modeSwitcher;

    protected boolean refreshed;

    private TextImage modeText;

    public ModeStrategy(UContext uContext, DisplayStrategy strategy) {
        super(uContext.getAndroidContext(), strategy);
        this.uContext = uContext;
        modeSwitcher = uContext.getModeSwitcher();
        modeText = new TextImage(GraphUtils.getSimpleTextPaint(35f, Color.WHITE), 5);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        modeText.setDrawPos(width - modeText.getWidth() - 50, 20);
        modeText.drawText("模式：" + modeSwitcher.getMode());
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);
        canvas.invalidateTextureContent(modeText.getImage());
        canvas.drawBitmap(modeText.getImage(), modeText.getX(), modeText.getY());

        if (refreshed) {
            refresh(canvas);
            refreshed = false;
        }

    }

    @Override
    public final void update(Observable o, Object arg) {
        refreshed = true;
    }

    /**
     * 添加像素刷新源
     *
     * @param observable
     */
    public void addPixelsRefreshSource(Observable observable) {
        observable.addObserver(this);
    }

    /**
     * 删除像素刷新源
     *
     * @param observable
     */
    public void removePixelsRefreshSource(Observable observable) {
        observable.deleteObserver(this);
    }

    /**
     * 刷新像素的具体逻辑
     *
     * @param canvas
     */
    protected abstract void refresh(ICanvasGL canvas);

}
