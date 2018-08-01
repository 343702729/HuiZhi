package com.huizhi.manage.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.activity.home.HomeMessageInfoActivity;
import com.huizhi.manage.activity.home.HomeNewsInfoActivity;
import com.huizhi.manage.activity.home.course.CourseInfoActivity;
import com.huizhi.manage.activity.home.course.CourseListActivity;
import com.huizhi.manage.activity.home.task.HomeTaskAgencyActivity;
import com.huizhi.manage.activity.home.task.HomeTaskVerifyActivity;
import com.huizhi.manage.activity.task.TaskDetailActivity;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.main.MainActivity;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by CL on 2018/1/26.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "HuiZhi";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Log.i(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.i(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.i(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.i(TAG, "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.i(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.i(TAG, "[MyReceiver] 用户点击打开了通知");
                String[] datas = getDates(bundle);
                goActivity(datas, context, bundle);
                //打开自定义的Activity
//                Intent i = new Intent(context, TestActivity.class);
//                i.putExtras(bundle);
//                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                context.startActivity(i);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.i(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.i(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
                Log.i(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e){

        }
    }

    private String[] getDates(Bundle bundle){
        String[] strs = new String[2];
        try {
            JSONObject jsonOb = new JSONObject((bundle.getString(JPushInterface.EXTRA_EXTRA)));
            strs[0] = JSONUtil.parseString(jsonOb, "type");
            strs[1] = JSONUtil.parseString(jsonOb, "bizid");
        }catch (Exception e){
            return null;
        }
        return  strs;
    }

    private void goActivity(String[] dates, Context context, Bundle bundle){
        if(dates==null)
            return;
        if(AppUtil.isAppRunning(context)&& UserInfo.getInstance().isLogin()){
            Log.i(TAG, "Come into app is running");
            if("31".equals(dates[0])||"32".equals(dates[0])||"33".equals(dates[0])||"42".equals(dates[0])){
                Log.i(TAG, "Come into app reintent");
                Intent intent = new Intent(context, TaskDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                intent.putExtra("TaskId", dates[1]);
                context.startActivity(intent);
            }else if("41".equals(dates[0])){
                Intent intent = new Intent(context, HomeTaskVerifyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                intent.putExtra("TaskId", dates[1]);
                context.startActivity(intent);
            }else if("2".equals(dates[0])){
                Intent intent = new Intent(context, HomeNewsInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                intent.putExtra("Id", dates[1]);
                Intent[] intents = {intent};
                context.startActivity(intent);
            }else if("1".equals(dates[0])){
                Intent intent = new Intent(context, HomeMessageInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                intent.putExtra("Id", dates[1]);
                Intent[] intents = {intent};
                context.startActivity(intent);
            }else if("51".equals(dates[0])){
                Intent intent = new Intent(context, HomeTaskAgencyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }else if("60".equals(dates[0])){
                Intent intent = new Intent();
                intent.setClass(context, CourseInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("LessonNum", dates[1]);
                context.startActivity(intent);
            }
        }else{
            Log.i(TAG, "Come into app is not running");
            if("60".equals(dates[0])||"31".equals(dates[0])||"32".equals(dates[0])||"33".equals(dates[0])||"41".equals(dates[0])||"42".equals(dates[0])||"2".equals(dates[0])||"1".equals(dates[0])){
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(Constants.PACKAG_ENAME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("IsMessage", true);
                intent.putExtra("Type", Integer.parseInt(dates[0]));
                intent.putExtra("Date", dates[1]);
                context.startActivity(intent);
            }
        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.i(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//        }
    }

}
