package com.littlezheng.ultrasound2.ultrasound.display.strategy;

import android.content.Context;
import android.graphics.Color;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound2.ultrasound.component.ModeController;
import com.littlezheng.ultrasound2.ultrasound.component.UContext;
import com.littlezheng.ultrasound2.ultrasound.display.image.TextImage;
import com.littlezheng.ultrasound2.ultrasound.util.GraphUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public abstract class ModeDisplayStrategyDecorator extends BaseDisplayStrategyDecorator
        implements Observer{

    protected UContext uContext;
    protected ModeController modeController;

    private TextImage modeText;

    public ModeDisplayStrategyDecorator(UContext uContext, DisplayStrategy strategy) {
        super(uContext.getContext(), strategy);
        this.uContext = uContext;
        modeController = uContext.getModeController();

        modeText = new TextImage(GraphUtils.getSimpleTextPaint(35f, Color.WHITE), 5);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        modeText.setDrawPos(width-modeText.getWidth()-50, 20);
        modeText.drawText("模式："+modeController.getMode());
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        canvas.invalidateTextureContent(modeText.getImage());
        canvas.drawBitmap(modeText.getImage(), modeText.getX(), modeText.getY());
    }

    /**
     * 添加像素刷新源
     * @param observable
     */
    public void addPixelsRefreshSource(Observable observable){
        observable.addObserver(this);
    }

    /**
     * 删除像素刷新源
     * @param observable
     */
    public void removePixelsRefreshSource(Observable observable){
        observable.deleteObserver(this);
    }

}
