package com.littlezheng.ultrasound.ultrasound.view.strategy.decorator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.Toast;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound.ultrasound.util.GraphUtils;
import com.littlezheng.ultrasound.ultrasound.view.measure.NonShapeMaker;
import com.littlezheng.ultrasound.ultrasound.view.measure.ShapeMaker;
import com.littlezheng.ultrasound.ultrasound.view.strategy.DisplayStrategy;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2017/8/23/023.
 */

public class MeasureStrategyDecorator extends AbstractDisplayStrategyDecorator {

    //绘图蒙版
    private Bitmap mask;
    private Canvas maskCanvas;
    private boolean maskInitialized;

    private List<ShapeMaker> shapeMakers = new CopyOnWriteArrayList<>();
    private ShapeMaker nonShapeMaker = new NonShapeMaker();
    private ShapeMaker shapeMaker = nonShapeMaker;

    public MeasureStrategyDecorator(Context context, DisplayStrategy strategy) {
        super(context,strategy);
    }

    public void initMask(int width, int height){
        if(!maskInitialized){
            mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
            maskCanvas = new Canvas(mask);
            maskInitialized = true;
        }
    }

    @Override
    public void onGLDraw(ICanvasGL canvas) {
        super.onGLDraw(canvas);

        GraphUtils.clearCanvas(maskCanvas);
        for(ShapeMaker shapeMaker : shapeMakers){
            shapeMaker.make(maskCanvas);
        }

        canvas.invalidateTextureContent(mask);
        canvas.drawBitmap(mask,0,0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                shapeMaker.click(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                shapeMaker.move(x, y);
                break;
        }
        return true;
    }

    public void addShapeMaker(ShapeMaker shapeMaker){
        if(shapeMaker == null) return;
        if(!this.shapeMaker.stepEnd()){
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
        if(shapeMaker.stepEnd()) return;
        if(shapeMakers.isEmpty()) return;
        shapeMakers.remove(shapeMakers.size()-1);
        shapeMaker = nonShapeMaker;
    }

}