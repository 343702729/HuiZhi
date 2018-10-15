package com.huizhi.manage.main;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.part.rong.MyReceiveMessageListener;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * Created by CL on 2018/1/4.
 */

public class MainApplication extends Application {
    public int localVersion;
    public String versionName;
    public static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
//        RongIM.init(this);
        instance = this;
        try {
            PackageInfo mPKinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            localVersion = mPKinfo.versionCode;
            versionName = mPKinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener(getApplicationContext()));
    }

    public void setRongInit(String key){
        Log.i("HuiZhi", "Come into load rong key:" + key);
        if(!TextUtils.isEmpty(key))
            RongIM.init(this, key);
    }

    public static MainApplication getInstance() {

        return instance;

    }

}
