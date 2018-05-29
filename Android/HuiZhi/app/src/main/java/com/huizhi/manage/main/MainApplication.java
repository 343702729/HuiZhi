package com.huizhi.manage.main;

import android.app.Application;
import android.content.Context;
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

    @Override
    public void onCreate() {
        super.onCreate();
//        RongIM.init(this);

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener(getApplicationContext()));
    }

    public void setRongInit(String key){
        Log.i("HuiZhi", "Come into load rong key:" + key);
        if(!TextUtils.isEmpty(key))
            RongIM.init(this, key);
    }

}
