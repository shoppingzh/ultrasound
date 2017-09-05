package com.littlezheng.ultrasound2.ultrasound.display.strategy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound2.ultrasound.component.ParamController;
import com.littlezheng.ultrasound2.ultrasound.component.UContext;
import com.littlezheng.ultrasound2.ultrasound.display.image.TextImage;
import com.littlezheng.ultrasound2.ultrasound.util.GraphUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/2/002.
 */

public class ParamDisplayStrategyDecorator extends BaseDisplayStrategyDecorator implements Observer{

    private List<ParamController> params = new ArrayList<>();
    private TextImage paramText;

    public ParamDisplayStrategyDecorator(UContext uContext, DisplayStrategy strategy) {
        super(uContext.getContext(), strategy);
        initTextImage();

        addParam(uContext.getContrast());
        addParam(uContext.getLightness());
        addParam(uContext.getGain());
        addParam(uContext.getNearGain());
        addParam(uContext.getFarGain());
        addParam(uContext.getDepth());
        addParam(uContext.getSpeed());
    }

    private void initTextImage() {
        int count = params.size();
        Paint p = GraphUtils.getSimpleTextPaint(20, Color.WHITE);
        int wid = 0;
        for(ParamController pc : params){
            int currWid = (int) Math.ceil(p.measureText(pc.desc()));
            if(currWid > wid) wid = currWid;
            //注册观察者，让该显示策略能感知参数变化
            pc.addObserver(this);
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
     * @param param
     */
    public void addParam(ParamController param){
        if(!params.contains(param)){
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
        for(ParamController pc : params){
            desc[i++] = pc.desc();
        }

        return desc;
    }

}
