package com.littlezheng.ultrasound4.ultrasound.display.draw;

import android.graphics.Paint;

import com.chillingvan.canvasgl.ICanvasGL;
import com.littlezheng.ultrasound4.framework.graphics.image.TextImage;

/**
 * Created by Administrator on 2017/9/26/026.
 */

public class TextDrawCommand implements DrawCommand {

    private TextImage textImage;
    private int x;
    private int y;

    public TextDrawCommand(String text, Paint p, int x, int y) {
        this.x = x;
        this.y = y;
        float width = p.measureText(text);
        textImage = new TextImage(p, (int) width, (int) p.getTextSize());
        textImage.drawText(text);
    }

    @Override
    public void execute(ICanvasGL canvas) {
        canvas.drawBitmap(textImage.getImage(), x, y);
    }

    public int getTextWidth() {
        return textImage.getWidth();
    }

    public int getTextHeight() {
        return textImage.getHeight();
    }

}
