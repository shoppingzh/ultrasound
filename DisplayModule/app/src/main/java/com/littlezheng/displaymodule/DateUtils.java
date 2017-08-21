package com.littlezheng.displaymodule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zxp on 2017/7/19.
 */

public class DateUtils {

    public static String getDateTimeStr(){
//        Calendar calendar = Calendar.getInstance();
//        StringBuffer sb = new StringBuffer();
//        sb.append(calendar.get(Calendar.YEAR)).append("-");
//        sb.append(calendar.get(Calendar.MONTH)).append("-");
//        sb.append(calendar.get(Calendar.DAY_OF_MONTH)).append(" ");
//        sb.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
//        sb.append(calendar.get(Calendar.MINUTE)).append(":");
//        sb.append(calendar.get(Calendar.SECOND));

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    public static String getDateStr(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }



}
