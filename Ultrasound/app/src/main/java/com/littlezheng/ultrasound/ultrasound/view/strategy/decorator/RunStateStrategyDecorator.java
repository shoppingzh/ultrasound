package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Color;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.transfer.data.RunController;
import com.littlezheng.ultrasound.ultrasound.util.GraphUtils;
import com.littlezheng.ultrasound.ultrasound.view.image.TextImage;
import com.littlezheng.ultrasound.ultrasound.view.strategy.AbsDisplayStrategy;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public class RunStateStrategyDecorator extends AbsDisplayStrategy {

    private DisplayStrategy displayStrategy;
    private RunController runController;

    private TextImage runState;

    public RunStateStrategyDecorator(Context context, DisplayStrategy displayStrategy, RunController runController) {
        super(context);
        this.displayStrategy = displayStrategy;
        this.runController = runController;

        runState = new TextImage(GraphUtils.getSimpleTextPaint(45f, Color.WHITE), 2);
    }

    public void init(int width, int height){
        runState.setDrawPos(20, height-runState.getHeight()-10);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        displayStrategy.onGLDraw(canvas);

        runState.drawText(runController.isFreeze() ? "冻结" : "运行");
        canvas.invalidateTextureContent(runState.getImage());
        canvas.drawBitmap(runState.getImage(),runState.getX(),runState.getY());
    }

}
