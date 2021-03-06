package com.littlezheng.ultrasound3.ultrasound.display.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;


/**
 * Created by zxp on 2017/7/24.
 */

public class TextImage extends Image {

    private static final String TAG = "TextImage";

    private Paint p;
    private boolean multiRows = false;
    private int rows;
    private int rowSpacing;

    /**
     * @param p     文字画笔
     * @param words 文字数量
     */
    public TextImage(Paint p, int words) {
        super();
        this.p = p;
        float textSize = p.getTextSize();
        wid = (int) Math.ceil(textSize * words);
        hei = (int) Math.ceil(textSize + textSize / 10);
        adjustWidthAndHeight();
        bmp = Bitmap.createBitmap(wid, hei, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bmp);
    }

    /**
     * @param p   文字画笔
     * @param wid 图片宽度
     * @param hei 图片高度
     */
    public TextImage(Paint p, int wid, int hei) {
        super();
        this.p = p;
        this.wid = wid;
        hei += hei + p.getTextSize() / 10;
        this.hei = hei;
        adjustWidthAndHeight();
        bmp = Bitmap.createBitmap(this.wid, this.hei, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bmp);
    }

    /**
     * @param p          文字画笔
     * @param rows       文字行数
     * @param maxWords   最大的文字数量
     * @param rowSpacing 行间距
     */
    public TextImage(Paint p, int rows, int maxWords, int rowSpacing) {
        super();
        this.p = p;
        this.rows = rows;
        this.rowSpacing = rowSpacing;
        multiRows = true;
        float textSize = p.getTextSize();
        wid = (int) Math.ceil(textSize * maxWords);
        hei = (int) ((int) Math.ceil(textSize * rows) + textSize / 10 + rowSpacing * (rows - 1));
        adjustWidthAndHeight();
        bmp = Bitmap.createBitmap(wid, hei, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bmp);
    }

    /**
     * @param p
     * @param rows
     * @param maxWid
     * @param rowSpacing
     * @param nul
     */
    public TextImage(Paint p, int rows, int maxWid, int rowSpacing, boolean nul) {
        super();
        this.p = p;
        this.rows = rows;
        this.rowSpacing = rowSpacing;
        multiRows = true;
        float textSize = p.getTextSize();
        wid = maxWid;
        hei = (int) ((int) Math.ceil(textSize * rows) + textSize / 10 + rowSpacing * (rows - 1));
        adjustWidthAndHeight();
        bmp = Bitmap.createBitmap(wid, hei, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bmp);
    }

    private void adjustWidthAndHeight() {
        if (wid <= 0) wid = 1;
        if (hei <= 0) hei = 1;
    }

    /**
     * 绘制单行文字
     *
     * @param text 文字内容
     * @return
     */
    public boolean drawText(String text) {
        if (multiRows) return false;
        if (text == null || text.isEmpty()) return false;
        clear(canvas);
        canvas.drawText(text, 0, p.getTextSize(), p);
        return true;
    }


    /**
     * 绘制多行文字
     *
     * @param texts 文字数组，数组下标表示行数
     * @return
     */
    public boolean drawRowsText(String... texts) {
        if (!multiRows) return false;
        if (texts == null || texts.length <= 0) return false;
        int len = texts.length;
        if (len > rows) {
            return false;
        }
        clear(canvas);
        for (int i = 0; i < len; i++) {
            int spacing = (i == 0) ? 0 : rowSpacing;
            canvas.drawText(texts[i], 0, p.getTextSize() * (i + 1) + spacing * i, p);
        }
        return true;
    }

    /**
     * 绘制用\n回车符分隔的多行文字
     *
     * @param text
     * @return
     */
    public boolean drawRowsText(String text) {
        if (!multiRows) return false;
        if (text == null || text.isEmpty()) return false;
        String[] part = text.split("\n");
        return drawRowsText(part);
    }

    public Paint getPaint() {
        return p;
    }

    public boolean isMultiRows() {
        return multiRows;
    }

    public int getRows() {
        return rows;
    }

    public int getRowSpacing() {
        return rowSpacing;
    }

}
