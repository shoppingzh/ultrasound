package com.littlezheng.ultrasound.ultrasound.util;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/8/22/022.
 */

public abstract class BitmapUtils {

    /**
     * 保存位图
     * @param bitmap 位图
     * @param path 保存到的路径
     * @param bmpName 位图名称
     * @return
     */
    public static boolean saveBitmap(Bitmap bitmap, File path, String bmpName){
        if(bitmap == null || bitmap.isRecycled()){
            return false;
        }
        if(!path.exists()){
            path.mkdirs();
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(new File(path,bmpName));
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    //
                }
            }
        }

        return true;
    }

}
