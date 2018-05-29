package com.huizhi.manage.part.rong;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.huizhi.manage.R;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.login.LoginActivity;
import com.huizhi.manage.util.AppUtil;

import java.util.Date;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by CL on 2018/1/26.
 */

public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener{
    private Context context;

    public MyReceiveMessageListener(Context context){
        this.context = context;
    }

    @Override
    public boolean onReceived(Message message, int i) {
        Log.i("HuiZhi", "Come into Rong MyReceiveMessageListener");
        if(AppUtil.isAppRunning(context)){
            Log.i("HuiZhi", "Come into Rong message receive app is Running");

        }else {
            Log.i("HuiZhi", "Come into Rong message receive app is not Running");
            TextMessage content = (TextMessage)message.getContent();
            Log.i("HuiZhi", "Come into rong MyReceiveMessageListener userid:" + message.getSenderUserId() + "  message:" + content.getContent() + "  type:" + message.getConversationType() + " The receive status:" + message.getReceivedStatus().isListened() + " i:" + i);
            if(Conversation.ConversationType.PRIVATE.equals(message.getConversationType())){
//                sendPrivateNotification(context, message.getSenderUserId(), content.getContent());
            }else if(Conversation.ConversationType.GROUP.equals(message.getConversationType())){
//                sendGroupNotification(context, message.getSenderUserId());
            }

        }

        return false;
    }

    private void sendPrivateNotification(Context context, String userid, String content){
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.icon_logo)//设置小图标
                .setContentTitle(userid)
                .setContentText(content)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("IsMessage", true);
        intent.putExtra("Type", Constants.MSG_RECEIVE);
        intent.putExtra("Date", userid);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = pendingIntent;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        int id = (int)new Date().getTime();
        notificationManager.notify(id, notification);


    }

    private void sendGroupNotification(Context context, String userid){
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.icon_logo)//设置小图标
                .setContentTitle(userid)
                .setContentText("你有一条未读消息")
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        int id = (int)new Date().getTime();
        notificationManager.notify(id, notification);
    }
}
