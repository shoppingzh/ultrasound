package com.littlezheng.ultrasound4.ultrasound.display.workmode;

import android.graphics.RectF;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.view.DisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.SampledData;
import com.littlezheng.ultrasound4.ultrasound.UContext;
import com.littlezheng.ultrasound4.ultrasound.component.UImage;

/**
 * Created by Administrator on 2017/9/23/023.
 */

public class BMode extends WorkModeDisplayStrategy {

    private int depth;
    private RectF bDst;

    public BMode(UContext uContext, DisplayStrategy displayStrategy) {
        super(uContext, displayStrategy);
        //获取当前深度
        depth = uContext.getParam(UContext.PARAM_DEPTH).getCurrValue();
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        float hei = Math.round(height * 0.9f), wid = Math.round((width * 0.9f));
        float left = (width - wid) / 2, top = (height - hei) / 2;
        bDst = new RectF(left, top, wid + left, hei + top);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        canvas.drawBitmap(b1, bSrcMap.get(depth), bDst);
        canvas.invalidateTextureContent(b1);

        //让图像显示在最下方，以免遮挡其它信息
        super.onGLDraw(canvas);
    }

    @Override
    protected void refresh(UImage uImage) {
        depth = uImage.getDepth();
        b1.setPixels(uImage.getPixels(), 0, SampledData.THIRD_SAMPLE_MAX_WIDTH, 0, 0,
                SampledData.THIRD_SAMPLE_MAX_WIDTH, SampledData.THIRD_SAMPLE_MAX_HEIGHT);
    }

}
