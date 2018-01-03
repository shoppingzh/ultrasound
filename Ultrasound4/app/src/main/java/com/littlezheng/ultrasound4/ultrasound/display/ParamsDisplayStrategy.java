package com.littlezheng.ultrasound4.ultrasound.display;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.graphics.image.TextImage;
import com.littlezheng.ultrasound4.framework.view.DisplayStrategy;
import com.littlezheng.ultrasound4.framework.view.MultipleDisplayStrategy;
import com.littlezheng.ultrasound4.ultrasound.component.Param;
import com.littlezheng.ultrasound4.ultrasound.util.GraphUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/9/20/020.
 */

public class ParamsDisplayStrategy extends MultipleDisplayStrategy implements Observer {

    private List<Param> params = new ArrayList<>();
    private TextImage paramText;

    public ParamsDisplayStrategy(Context context, DisplayStrategy strategy) {
        super(context, strategy);
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

    /**
     * 在视图中添加多个参数描述
     *
     * @param params
     */
    public void addParams(Param... params) {
        if (params == null || params.length <= 0) return;
        for (Param param : params) {
            addParam(param);
        }
    }

    /**
     * 添加多个参数描述
     *
     * @param params
     */
    public void addParams(Collection<Param> params) {
        if (params == null || params.isEmpty()) return;
        for (Param param : params) {
            addParam(param);
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
