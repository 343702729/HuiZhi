package com.huizhi.manage.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.huizhi.manage.data.Constants;
import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by CL on 2018/1/29.
 */

public class AppUtil {

    private static MyContentObserver mNavigationStatusObserver;

    public static void hideKeyBoard(Context context, View view){
        Log.i("HuiZhi", "Come into hide key board");
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        // 获取软键盘的显示状态
//        boolean isOpen=imm.isActive();
//        if(isOpen){
//            // 如果软键盘已经显示，则隐藏，反之则显示
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }

    public static boolean isAppRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(Constants.PACKAG_ENAME)) {
                return true;
            }
        }
        return false;
    }

    public static void setNavigationBar(Activity context){
        StatusBarUtil.setTranslucentForImageViewInFragment(context, null);
    }

    public static void setNavigationBarAlpha(Activity context, int alpha){
        StatusBarUtil.setTranslucentForImageViewInFragment(context, alpha, null);
    }

    /**
     * 是否是华为
     */
    public static boolean isHUAWEI() {
        return android.os.Build.MANUFACTURER.equals("HUAWEI");
    }

    public static void navigationBarDo(Activity context){
        if(!checkDeviceHasNavigationBar(context))
            return;
        Log.i("Bar", "come into navigationBarDo");
        setScreenHeight(context);
        context.getContentResolver().registerContentObserver(Settings.System.getUriFor("navigationbar_is_min"), true, getNavigationStatusObserver(context));

    }

    private static ContentObserver getNavigationStatusObserver(Activity context){
        if(mNavigationStatusObserver==null)
            mNavigationStatusObserver = new MyContentObserver(context, new Handler());
        return mNavigationStatusObserver;
    }

    /**
     * 获取是否存在NavigationBar
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    /**
     * 获取虚拟功能键高度
     * @param context
     * @return
     */
    public static int getVirtualBarHeigh(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setScreenHeight(Activity context){
        if(checkDeviceHasNavigationBar(context)){
//            int navHeight = getVirtualBarHeigh(context)/2;
            Display display = context.getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
            Window window = context.getWindow();
            WindowManager.LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值
            windowLayoutParams.height = (int) display.getHeight();
            windowLayoutParams.gravity = Gravity.TOP;
        }
    }

    private static class MyContentObserver extends ContentObserver{
        private Activity context;

        public MyContentObserver(Activity context, Handler handler){
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            int navigationBarIsMin = Settings.System.getInt(context.getContentResolver(),
                    "navigationbar_is_min", 0);
            Log.i("Bar", "come into ContentObserver:" + navigationBarIsMin);
            if (navigationBarIsMin == 1) {
                //导航键隐藏了

                Toast.makeText(context, "导航键隐藏了", Toast.LENGTH_LONG).show();
                setScreenHeight(context);
            } else {
                //导航键显示了
                Toast.makeText(context, "导航键显示了", Toast.LENGTH_LONG).show();
                setScreenHeight(context);
            }
        }
    }
}
