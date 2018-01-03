package com.littlezheng.ultrasound4.ultrasound.display;

import android.graphics.Color;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.graphics.image.TextImage;
import com.littlezheng.ultrasound4.framework.view.DisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.StateSwitcher;
import com.littlezheng.ultrasound4.ultrasound.util.GraphUtils;

import java.util.Observable;
import java.util.Observer;


/**
 * Created by Administrator on 2017/9/23/023.
 */

public class RunStateDisplayStrategy extends UMultipleDisplayStrategy implements Observer {

    private TextImage runStateText;
    private StateSwitcher stateSwitcher;

    public RunStateDisplayStrategy(UContext uContext, DisplayStrategy strategy) {
        super(uContext, strategy);
        stateSwitcher = uContext.getStateSwitcher();
        runStateText = new TextImage(GraphUtils.getSimpleTextPaint(40, Color.WHITE), 2);

        //设置状态感知
        stateSwitcher.addObserver(this);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        runStateText.setDrawPos(20, height - runStateText.getHeight() - 20);
        drawRunState();
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        canvas.invalidateTextureContent(runStateText.getImage());
        canvas.drawBitmap(runStateText.getImage(), runStateText.getX(), runStateText.getY());
    }

    @Override
    public void update(Observable o, Object arg) {
        drawRunState();
    }

    private void drawRunState() {
        runStateText.drawText(stateSwitcher.isFrozen() ? "冻结" : "运行");
    }

}
