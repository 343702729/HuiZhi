package com.huizhi.manage.activity.communicate;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.main.MainActivity;
import com.huizhi.manage.util.AppUtil;

import java.util.List;
import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Created by CL on 2018/1/4.
 */

public class CommunicateActivity extends FragmentActivity {
    private String title, targetId, createId;
    private Conversation.ConversationType conversationType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final TextView titleTV = findViewById(R.id.title_tv);

        RongIM.getInstance().getDiscussion(targetId, new RongIMClient.ResultCallback<Discussion>(){
            @Override
            public void onSuccess(Discussion discussion) {
                createId = discussion.getCreatorId();
                Log.i("HuiZhi", "user id:" + UserInfo.getInstance().getUser().getTeacherId() +  "  The createid:" + createId);
                title = discussion.getName();
                titleTV.setText(title);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void initViews(){
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(backBtnClick);
        title = getIntent().getData().getQueryParameter("title");
        targetId = getIntent().getData().getQueryParameter("targetId");
        TextView titleTV = findViewById(R.id.title_tv);
        titleTV.setText(title);
        conversationType = Conversation.ConversationType.valueOf(getIntent().getData().getLastPathSegment().
                toUpperCase(Locale.getDefault()));
        if(conversationType == Conversation.ConversationType.DISCUSSION){
            ImageButton infoBtn = findViewById(R.id.info_btn);
            infoBtn.setVisibility(View.VISIBLE);
            infoBtn.setOnClickListener(groupInfoBtnClick);
        }
        Log.i("HuiZhi", "The title:" + title + "  targetId:" + targetId + "  type:" + conversationType.getName());
        getHoistoryMsg();
    }

    private View.OnClickListener backBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!isTaskRoot()) {
                Log.i("HuiZhi", "Come into comunicate finish");
                finish();
                return;
            }
            Intent intent = new Intent();
            intent.setClass(CommunicateActivity.this, MainActivity.class);
            intent.putExtra("Index", 3);
            startActivity(intent);
            finish();
        }
    };

    private View.OnClickListener groupInfoBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(CommunicateActivity.this, CommunicateGroupInfoActivity.class);
            intent.putExtra("Title", title);
            intent.putExtra("TargetId", targetId);
            if(UserInfo.getInstance().getUser().getTeacherId().equals(createId))
                intent.putExtra("IsCreater", true);
            else
                intent.putExtra("IsCreater", false);
            startActivityForResult(intent, Constants.REQUEST_CODE);
//            startActivity(intent);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(!isTaskRoot())
                return super.onKeyDown(keyCode, event);
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            intent.putExtra("Index", 3);
            startActivity(intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
//        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Constants.REQUEST_CODE == requestCode && Constants.RESULT_CODE == resultCode){
            if(UserInfo.getInstance().getCommunicateInfo()!=null)
                UserInfo.getInstance().getCommunicateInfo().update(true);
            finish();
        }
    }

    private void getHoistoryMsg(){

        RongIMClient.getInstance().getHistoryMessages(conversationType, targetId, "RC:TxtMsg", -1, 20, new RongIMClient.ResultCallback<List<Message>>(){
            @Override
            public void onSuccess(List<Message> messages) {
                if(messages==null)
                    Log.i("HuiZhi", "The message null");
                else
                    Log.i("HuiZhi", "The messages size:" + messages.size() + "   ss:" + messages);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

        Log.i("HuiZhi", "The targetid:" + targetId);
        /**
        RongIM.getInstance().getHistoryMessages(conversationType, targetId, "RC:TxtMsg", -1, 20, new RongIMClient.ResultCallback<List<Message>>(){
            @Override
            public void onSuccess(List<Message> messages) {
                if(messages==null)
                    Log.i("HuiZhi", "The message null");
                else
                    Log.i("HuiZhi", "The messages size:" + messages.size() + "   ss:" + messages);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
         */
    }
}
