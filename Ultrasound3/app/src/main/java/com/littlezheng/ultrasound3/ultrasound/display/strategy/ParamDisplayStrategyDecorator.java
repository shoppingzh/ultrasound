package com.littlezheng.ultrasound3.ultrasound.display.strategy;

import android.graphics.Color;
import android.graphics.Paint;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound3.ultrasound.UContext;
import com.littlezheng.ultrasound3.ultrasound.base.Param;
import com.littlezheng.ultrasound3.ultrasound.display.image.TextImage;
import com.littlezheng.ultrasound3.ultrasound.util.GraphUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/2/002.
 */

public class ParamDisplayStrategyDecorator extends BaseDisplayStrategyDecorator implements Observer {

    private List<Param> params = new ArrayList<>();
    private TextImage paramText;

    public ParamDisplayStrategyDecorator(UContext uContext, DisplayStrategy strategy) {
        super(uContext.getAndroidContext(), strategy);
        initTextImage();

        params.add(uContext.getParam(UContext.PARAM_CONTRAST));
        params.add(uContext.getParam(UContext.PARAM_LIGHTNESS));
        params.add(uContext.getParam(UContext.PARAM_GAIN));
        params.add(uContext.getParam(UContext.PARAM_NEAR_GAIN));
        params.add(uContext.getParam(UContext.PARAM_FAR_GAIN));
        params.add(uContext.getParam(UContext.PARAM_DEPTH));
        params.add(uContext.getParam(UContext.PARAM_SPEED));
        initTextImage();
    }

    private void initTextImage() {
        int count = params.size();
        Paint p = GraphUtils.getSimpleTextPaint(20, Color.WHITE);
        int wid = 0;
        for (Param param : params) {
            int currWid = (int) Math.ceil(p.measureText(param.desc()));
            if (currWid > wid) wid = currWid;
            //注册观察者，让该显示策略能感知参数变化
            param.addObserver(this);
        }

        paramText = new TextImage(p, count, wid, 10, false);
        paramText.setDrawPos(20, 80);
        paramText.drawRowsText(getParamsDesc());
    }

    @Override
    public void init(int width, int height) {
        super.init(width, height);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        canvas.invalidateTextureContent(paramText.getImage());
        canvas.drawBitmap(paramText.getImage(), paramText.getX(), paramText.getY());
    }

    /**
     * 在视图中添加一个参数描述
     *
     * @param param
     */
    public void addParam(Param param) {
        if (!params.contains(param)) {
            params.add(param);
            initTextImage();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        paramText.drawRowsText(getParamsDesc());
    }

    private String[] getParamsDesc() {
        String[] desc = new String[params.size()];
        int i = 0;
        for (Param param : params) {
            desc[i++] = param.desc();
        }

        return desc;
    }

}
