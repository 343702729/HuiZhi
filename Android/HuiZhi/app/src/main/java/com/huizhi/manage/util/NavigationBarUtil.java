package com.huizhi.manage.util;

import android.view.View;
import android.view.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by CL on 2017/12/13.
 */

/**
 * 6.0 以上有效果
 */
public class NavigationBarUtil {

    public static void MIUISetStatusBarLightMode(Window window, boolean dark) {
//        if(dark){
//            window.getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }else{
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        }
    }
}
