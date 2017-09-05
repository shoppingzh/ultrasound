package com.littlezheng.drawingboard.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.Toast;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.drawingboard.NonShapeMaker;
import com.littlezheng.drawingboard.api.GraphUtils;
import com.littlezheng.drawingboard.api.ShapeMaker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public class MeasureStrategy extends AbsDisplayStrategy {

    //绘图蒙版
    private Bitmap mask;
    private Canvas maskCanvas;

    private List<ShapeMaker> shapeMakers = new ArrayList<>();
    private ShapeMaker nonShapeMaker = new NonShapeMaker();
    private ShapeMaker shapeMaker = nonShapeMaker;

    private DisplayStrategy strategy;

    public MeasureStrategy(Context context, DisplayStrategy strategy) {
        super(context);
        this.strategy = strategy;
    }

    public void initMask(int width, int height){
        mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        maskCanvas = new Canvas(mask);
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        strategy.onGLDraw(canvas);

        GraphUtils.clearCanvas(maskCanvas);
        for(ShapeMaker shapeMaker : shapeMakers){
            shapeMaker.make(maskCanvas);
        }

        canvas.invalidateTextureContent(mask);
        canvas.drawBitmap(mask,0,0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                shapeMaker.click(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                shapeMaker.reset(x, y);
                break;
        }
        return true;
    }

    public void addShapeMaker(ShapeMaker shapeMaker){
        if(shapeMaker == null) return;
        if(!this.shapeMaker.makeEnd()){
            Toast.makeText(mContext,"请先完成上一个测量！",Toast.LENGTH_SHORT).show();
            return;
        }
        this.shapeMaker = shapeMaker;
        shapeMakers.add(shapeMaker);
    }

    public void removeAllShapeMaker() {
        shapeMakers.clear();
        shapeMaker = nonShapeMaker;
    }

    public void removeCurrent(){
        if(shapeMaker.makeEnd()) return;
        if(shapeMakers.isEmpty()) return;
        shapeMakers.remove(shapeMakers.size()-1);
        shapeMaker = nonShapeMaker;
    }




}
