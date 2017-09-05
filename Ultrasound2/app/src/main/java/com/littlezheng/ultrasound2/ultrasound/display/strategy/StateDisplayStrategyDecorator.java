package com.littlezheng.ultrasound2.ultrasound.display.strategy;

import android.content.Context;
import android.graphics.Color;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound2.ultrasound.component.StateController;
import com.littlezheng.ultrasound2.ultrasound.component.UContext;
import com.littlezheng.ultrasound2.ultrasound.display.image.TextImage;
import com.littlezheng.ultrasound2.ultrasound.util.GraphUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/3/003.
 */

public class StateDisplayStrategyDecorator extends BaseDisplayStrategyDecorator implements Observer{

    private UContext uContext;
    private StateController stateController;
    private TextImage stateText;

    public StateDisplayStrategyDecorator(UContext uContext, DisplayStrategy strategy) {
        super(uContext.getContext(), strategy);
        this.uContext = uContext;
        stateController = uContext.getStateController();
        stateText = new TextImage(GraphUtils.getSimpleTextPaint(50f, Color.WHITE), 2);
        //状态感知
        stateController.addObserver(this);
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
        stateText.setDrawPos(20, height-stateText.getHeight()-15);
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

    private void draw(){
        stateText.drawText(stateController.isFrozen() ? "冻结" : "运行");
    }


}
