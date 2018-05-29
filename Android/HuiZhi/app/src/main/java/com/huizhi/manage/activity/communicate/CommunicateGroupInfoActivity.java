package com.huizhi.manage.activity.communicate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.ContentEntryDialog;
import com.huizhi.manage.dialog.JudgeDialog;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.view.CommunicateGrouUsersView;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;

/**
 * Created by CL on 2018/3/4.
 * 群聊信息
 */

public class CommunicateGroupInfoActivity extends Activity {
    private String title, targetId;
    private boolean isCreater = false;
    private TextView titleTV;
    private LinearLayout usersLL;
    private List<String> discussPers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate_group_info);
        initDates();
        initViews();
    }

    private void initDates(){
        title = getIntent().getStringExtra("Title");
        targetId = getIntent().getStringExtra("TargetId");
        isCreater = getIntent().getBooleanExtra("IsCreater", false);
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        LinearLayout titleLL = findViewById(R.id.name_ll);
        titleLL.setOnClickListener(titleBtnClick);
        titleTV = findViewById(R.id.name_tv);
        titleTV.setText(title);

        usersLL = findViewById(R.id.users_ll);
        RongIMClient.getInstance().getDiscussion(targetId, resultCallback);

        Button dismissBtn = findViewById(R.id.dismiss_btn);

        if(isCreater) {
            dismissBtn.setText("退出并解散群");
            dismissBtn.setOnClickListener(dismissBtnClick);
            dismissBtn.setVisibility(View.VISIBLE);
        }else{
            dismissBtn.setText("退出");
            dismissBtn.setOnClickListener(deleteBtnClick);
            dismissBtn.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener deleteBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JudgeDialog judgeDialog = new JudgeDialog(CommunicateGroupInfoActivity.this, deleteInfo);
            judgeDialog.showView(view, "是否确定退出该群");

        }

        private BaseInfoUpdate deleteInfo = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                RongIMClient.getInstance().quitDiscussion(targetId, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i("HuiZhi", "Come 123 into dismiss per index:");
                        Intent intent = new Intent();
                        setResult(Constants.RESULT_CODE, intent);
                        finish();
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.i("HuiZhi", "Come into dimmiss per error:" + errorCode.name());
                    }
                });
            }
        };
    };

    private View.OnClickListener dismissBtnClick = new View.OnClickListener() {
        int index = 0;

        @Override
        public void onClick(View view) {

            for(final String perSelId:discussPers){
                RongIM.getInstance().removeMemberFromDiscussion(targetId, perSelId, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {
                        index++;
                        Log.i("HuiZhi", "Come into dismiss per index:" + index);
                        if(index==discussPers.size()-1){
                            finish();
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.i("HuiZhi", "Come into dimmiss per error:" + errorCode.name());
                    }
                });
            }
        }
    };

    private View.OnClickListener titleBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ContentEntryDialog entryDialog = new ContentEntryDialog(CommunicateGroupInfoActivity.this, infoUpdate);
            entryDialog.showView(view, title, "群聊名称");
        }

        BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;
                final String name = (String)object;
                if(TextUtils.isEmpty(name))
                    return;
                RongIM.getInstance().setDiscussionName(targetId, name, new RongIMClient.OperationCallback(){
                    @Override
                    public void onSuccess() {
                        title = name;
                        titleTV.setText(title);
//                        handler.sendEmptyMessage(1);

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }
        };
    };

    private RongIMClient.ResultCallback<Discussion> resultCallback = new RongIMClient.ResultCallback<Discussion>(){
        @Override
        public void onSuccess(Discussion discussion) {
            discussPers = discussion.getMemberIdList();
            Log.i("HuiZhi", "The ids list size:" + discussPers.size());
            addUsersView(discussPers);
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {

        }
    };

    private void addUsersView(List<String> ids){
        usersLL.removeAllViews();
        if(ids==null||ids.size()==0)
            return;
        CommunicateGrouUsersView grouUsersView;
        for(int i=0; i<ids.size();){
            grouUsersView = new CommunicateGrouUsersView(CommunicateGroupInfoActivity.this, targetId, ids, isCreater, userInfoUpdate);
            String userid1 = null, userid2 = null, userid3 = null, userid4 = null, userid5 = null;
            userid1 = ids.get(i);
            int index = -1;
            if(i+1<ids.size())
                userid2 = ids.get(++i);
            else {
                index = 2;
                grouUsersView.setDates(userid1, userid2, userid3, userid4, userid5, index);
                usersLL.addView(grouUsersView);
                break;
            }
            if(i+1<ids.size())
                userid3 = ids.get(++i);
            else {
                index = 3;
                grouUsersView.setDates(userid1, userid2, userid3, userid4, userid5, index);
                usersLL.addView(grouUsersView);
                break;
            }
            if(i+1<ids.size())
                userid4 = ids.get(++i);
            else {
                index = 4;
                grouUsersView.setDates(userid1, userid2, userid3, userid4, userid5, index);
                usersLL.addView(grouUsersView);
                break;
            }
            if(i+1<ids.size())
                userid5 = ids.get(++i);
            else {
                index = 5;
                grouUsersView.setDates(userid1, userid2, userid3, userid4, userid5, index);
                usersLL.addView(grouUsersView);
                CommunicateGrouUsersView grouUsersView1 = new CommunicateGrouUsersView(CommunicateGroupInfoActivity.this, targetId, ids, isCreater, userInfoUpdate);
                if(isCreater){
                    grouUsersView1.setDates(null, null, null, null, null, 0);
                    usersLL.addView(grouUsersView1);
                }
                break;
            }

            grouUsersView.setDates(userid1, userid2, userid3, userid4, userid5, index);
            usersLL.addView(grouUsersView);
            if(i+1==ids.size()){
                index = 1;
                CommunicateGrouUsersView grouUsersView1 = new CommunicateGrouUsersView(CommunicateGroupInfoActivity.this, targetId, ids, isCreater, userInfoUpdate);
                grouUsersView1.setDates(null, null, null, null, null, index);
                usersLL.addView(grouUsersView1);
            }
            i++;
        }
    }

    private BaseInfoUpdate userInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            boolean flage = (boolean)object;
            if(flage)
                RongIMClient.getInstance().getDiscussion(targetId, resultCallback);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                titleTV.setText(title);
            }
        }
    };
}
