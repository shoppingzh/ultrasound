package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Color;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.transfer.Param;
import com.littlezheng.ultrasound.ultrasound.util.GraphUtils;
import com.littlezheng.ultrasound.ultrasound.view.image.TextImage;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public class ParamsStrategyDecorator extends AbstractDisplayStrategyDecorator implements Observer {

    private static final String TAG = "ParamsStrategyDecorator";

    //参数
    private List<Param> params = new CopyOnWriteArrayList<>();

    private TextImage paramsImg;

    public ParamsStrategyDecorator(Context context, DisplayStrategy displayStrategy, Param... params) {
        super(context,displayStrategy);
        this.params.addAll(Arrays.asList(params));
        for(Param p : params){
            p.addObserver(this);
        }

        paramsImg = new TextImage(GraphUtils.getSimpleTextPaint(20f, Color.WHITE), params.length, 12, 10);
        paramsImg.setDrawPos(20, 100);
        paramsImg.drawRowsText(getParamsDesc());
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        canvas.invalidateTextureContent(paramsImg.getImage());
        canvas.drawBitmap(paramsImg.getImage(),paramsImg.getX(),paramsImg.getY());
    }

    private String getParamsDesc(){
        StringBuilder sb = new StringBuilder();
        for(Param p : params){
            if("深度".equals(p.getName())){
                sb.append("深度 : ");
                sb.append(p.getCurrValue()*10+30).append("mm");
                sb.append("\n");
                continue;
            }
            sb.append(p.getDesc());
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void update(Observable o, Object arg) {
        paramsImg.drawRowsText(getParamsDesc());
    }

}
