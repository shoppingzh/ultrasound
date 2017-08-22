package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.transfer.data.Param;
import com.littlezheng.ultrasound.ultrasound.util.GraphUtils;
import com.littlezheng.ultrasound.ultrasound.view.image.TextImage;
import com.littlezheng.ultrasound.ultrasound.view.strategy.AbsDisplayStrategy;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public class ParamsStrategyDecorator extends AbsDisplayStrategy {

    private static final String TAG = "ParamsStrategyDecorator";

    private DisplayStrategy displayStrategy;

    //参数
    private Param[] params;

    private TextImage paramsImg;

    public ParamsStrategyDecorator(Context context, DisplayStrategy displayStrategy, Param... params) {
        super(context);
        this.displayStrategy = displayStrategy;
        this.params = params;

        paramsImg = new TextImage(GraphUtils.getSimpleTextPaint(20f, Color.WHITE), params.length, 12, 10);
        paramsImg.setDrawPos(20, 100);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        displayStrategy.onGLDraw(canvas);

        paramsImg.drawRowsText(getParamsDesc());
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

}
