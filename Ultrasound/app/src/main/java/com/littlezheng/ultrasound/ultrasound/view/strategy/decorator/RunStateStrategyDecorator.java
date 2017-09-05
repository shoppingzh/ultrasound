package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Color;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.transfer.RunController;
import com.littlezheng.ultrasound.ultrasound.util.GraphUtils;
import com.littlezheng.ultrasound.ultrasound.view.image.TextImage;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public class RunStateStrategyDecorator extends AbstractDisplayStrategyDecorator implements Observer{

    private RunController runController;

    private TextImage runState;

    public RunStateStrategyDecorator(Context context, DisplayStrategy displayStrategy, RunController runController) {
        super(context,displayStrategy);
        this.runController = runController;
        runController.addObserver(this);

        runState = new TextImage(GraphUtils.getSimpleTextPaint(45f, Color.WHITE), 2);
        runState.drawText(runController.isFreeze() ? "冻结" : "运行");
    }

    public void init(int width, int height){
        runState.setDrawPos(20, height-runState.getHeight()-10);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);


        canvas.invalidateTextureContent(runState.getImage());
        canvas.drawBitmap(runState.getImage(),runState.getX(),runState.getY());
    }

    @Override
    public void update(Observable o, Object arg) {
        runState.drawText(runController.isFreeze() ? "冻结" : "运行");
    }
}
