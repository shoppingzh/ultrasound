package com.littlezheng.ultrasound4.ultrasound.display.workmode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.graphics.image.TextImage;
import com.littlezheng.ultrasound4.framework.view.DisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.SampledData;
import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.UImage;
import com.littlezheng.ultrasound4.ultrasound.display.UMultipleDisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.util.GraphUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/22/022.
 */

public abstract class WorkModeDisplayStrategy extends UMultipleDisplayStrategy implements Observer {

    static Bitmap b1 = Bitmap.createBitmap(SampledData.THIRD_SAMPLE_MAX_WIDTH,
            SampledData.THIRD_SAMPLE_MAX_HEIGHT, Bitmap.Config.ARGB_4444);
    static Bitmap b2 = b1.copy(Bitmap.Config.ARGB_4444, true);
    static Bitmap m = Bitmap.createBitmap(500, SampledData.ORIGINAL_FRAME_HEIGHT,
            Bitmap.Config.ARGB_4444);
    static Map<Integer, Rect> bSrcMap = new HashMap<>();    //b图像每个深度对应的显示区域

    static {
        for (int i = 0; i < 20; i++) {
            int thirdSampleWid = SampledData.getThirdSampleWidth(i);
            int displayWid = SampledData.getDisplayWidth(i);
            int displayHei = SampledData.getDisplayHeight(i);
            Rect src = new Rect(thirdSampleWid - displayWid, 0, displayWid, displayHei);

            bSrcMap.put(i, src);
        }
    }

    private TextImage modeText;

    public WorkModeDisplayStrategy(UContext uContext, DisplayStrategy displayStrategy) {
        super(uContext, displayStrategy);
        Paint p = GraphUtils.getSimpleTextPaint(25, Color.WHITE);
        modeText = new TextImage(p, (int) p.measureText("模式：B/M"), 25);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        modeText.setDrawPos(width - modeText.getWidth() - 80, 20);
        // 模式的转变一定对应显示策略的转变
        // 因此onSurfaceChanged方法一定会在模式变化时被调用
        modeText.drawText("模式：" + uContext.getModeSwitcher().getMode());
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);
        //将模式信息绘制在右上角
        canvas.invalidateTextureContent(modeText.getImage());
        canvas.drawBitmap(modeText.getImage(), modeText.getX(), modeText.getY());
    }

    @Override
    public final void update(Observable o, Object arg) {
        if (!(arg instanceof UImage)) return;
        refresh((UImage) arg);
    }

    /**
     * 添加图像刷新源
     *
     * @param source
     */
    public final void addRefreshSource(Observable source) {
        source.addObserver(this);
    }

    /**
     * 删除图像刷新源
     */
    public final void removeRefreshSource(Observable source) {
        source.deleteObserver(this);
    }

    /**
     * 刷新图像
     *
     * @param uImage 新的图像信息
     */
    protected abstract void refresh(UImage uImage);

}
