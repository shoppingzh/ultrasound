package com.littlezheng.proxyimageviewer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/19/019.
 */

public class Utils {

    public static List<Integer> listDrawableIds(){
        List<Integer> drawableIds = new ArrayList<>();
        Class clazz = R.drawable.class;
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            String name = field.getName();
            if(name.startsWith("img_")){
                try {
                    int id = field.getInt(clazz);
                    drawableIds.add(id);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return drawableIds;
    }

}
