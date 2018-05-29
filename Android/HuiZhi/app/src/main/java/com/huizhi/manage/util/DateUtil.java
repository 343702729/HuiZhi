package com.huizhi.manage.util;

/**
 * Created by CL on 2018/1/22.
 */

public class DateUtil {

    public static String parseMinute(int min){
        if(min==0)
            return "";
        String str = "";
        double m = (double)min;
        String format;
        Object[] array;
        int days = (int)(m/(60*24));
        int hours = (int)(m/60-days*24);
        int minutes = (int)(m-hours*60-days*24*60);
        if(days>0){
            format = "%1$,d天%2$,d时%3$,d分";
            array = new Object[]{days, hours, minutes};
        }else if(hours>0){
            format = "%1$,d时%2$,d分";
            array = new Object[]{hours, minutes};
        }else {
            format = "%1$,d分";
            array = new Object[]{minutes};
        }

        return String.format(format, array);
    }

    public static String parseHour(int min){
        if(min==0)
            return "";
        String str = "";
        double m = (double)min;
        String format;
        Object[] array;
//        int days = (int)(m/(60*24));
        int hours = (int)(m/60);
        int minutes = (int)(m-hours*60);
        if(hours>0){
            format = "%1$,d时%2$,d分";
            array = new Object[]{hours, minutes};
        }else {
            format = "%1$,d分";
            array = new Object[]{minutes};
        }

        return String.format(format, array);
    }


}
