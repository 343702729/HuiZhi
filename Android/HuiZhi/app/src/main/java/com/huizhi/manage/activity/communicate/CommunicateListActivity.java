package com.huizhi.manage.activity.communicate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.request.main.MainRequest;
import com.huizhi.manage.util.AppUtil;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by CL on 2018/2/5.
 * 通讯列表
 */

public class CommunicateListActivity extends FragmentActivity {
    private ConversationListFragment listFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communicate_list);
        com.huizhi.manage.data.UserInfo.getInstance().setCommunicateInfo(refreshInfo);
        initViews();
        setAllUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("HuiZhi", "Come into communivcate list resume");
//        setAllUserInfo();
//        listFragment.onResume();
//        RongIM.getInstance().startConversationList(CommunicateListActivity.this);
        RongIM.getInstance().getConversationList(resultCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        com.huizhi.manage.data.UserInfo.getInstance().setCommunicateInfo(null);
    }

    private void initViews(){
        listFragment = (ConversationListFragment) ConversationListFragment.instantiate(this, ConversationListFragment.class.getName());
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")
                .build();
        listFragment.setUri(uri);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //将融云的Fragment界面加入到我们的页面。
        transaction.add(R.id.conversationlist, listFragment);
        transaction.commitAllowingStateLoss();

    }

    private BaseInfoUpdate refreshInfo = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            initViews();
        }
    };

    private RongIMClient.ResultCallback<List<Conversation>> resultCallback = new RongIMClient.ResultCallback<List<Conversation>>() {
        @Override
        public void onSuccess(List<Conversation> conversations) {
            if(conversations==null)
                return;
            MainRequest mainRequest = new MainRequest();
            for (int i=0; i<conversations.size(); i++){
                String targetid = conversations.get(i).getTargetId();

                mainRequest.getTalkItemUser(targetid, new UserInfoUpdate(targetid));

//                setRongUserInfo(conversations.get(i).getTargetId());
                /**
                UserNode user = com.huizhi.manage.data.UserInfo.getInstance().getTalkUserByTeacherId(targetid);

                Uri uri = null;
                UserInfo userInfo;
                if(user!=null){
                    if(!TextUtils.isEmpty(user.getHeadImgUrl())){
                        String headImg = AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl());
                        Log.i("HuiZhi", "The head img:" + headImg);
                        uri = Uri.parse(headImg);
                    }
                    userInfo = new UserInfo(targetid, user.getTeacherName(), uri);

                }else {
                    userInfo = new UserInfo(targetid, "群聊", uri);
                }

                RongIM.getInstance().refreshUserInfoCache(userInfo);
                 */
            }
//            Conversation conversation = new Conversation();
//            conversation.setTargetId("0d07fdd9-03bb-458a-a675-736b34c41077");
//            conversation.setConversationType(Conversation.ConversationType.DISCUSSION);
//            conversation.setConversationTitle("cs");
        }

        @Override
        public void onError(RongIMClient.ErrorCode errorCode) {

        }

        //设置容云用户信息
        private void setRongUserInfo(final String targetid) {
            Log.i("HuiZhi", "Come into rong user info targetid:" + targetid);
            if (RongIM.getInstance()!=null){
                Log.i("HuiZhi", "Come into rong getUserInfo 0");
                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

                    @Override
                    public UserInfo getUserInfo(String userid) {
                        Log.i("HuiZhi", "Come into rong getUserInfo userid:" + userid);
                        UserNode user = com.huizhi.manage.data.UserInfo.getInstance().getTalkUserByTeacherId(targetid);
                        Uri uri = null;
                        if(!TextUtils.isEmpty(user.getHeadImgUrl())){
                            String headImg = AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl());
                            Log.i("HuiZhi", "The head img:" + headImg);
                            uri = Uri.parse(headImg);
                        }

                        UserInfo userInfo = new UserInfo(targetid, user.getTeacherName(), uri);
                        RongIM.getInstance().refreshUserInfoCache(userInfo);
                        return userInfo;
                    }
                },true);
            }else {
                Log.i("HuiZhi", "Come into rong user info null");
            }


        }

    };

    private void setAllUserInfo(){
        List<UserNode> users = com.huizhi.manage.data.UserInfo.getInstance().getTalkUsers();
        if(users==null)
            return;
        for (int i=0; i<users.size(); i++){
//                setRongUserInfo(conversations.get(i).getTargetId());
//            UserNode user = com.huizhi.manage.data.UserInfo.getInstance().getUserByTeacherId(targetid);
            UserNode user = users.get(i);
            if(user.getType()==1)
                continue;
            String targetid = user.getTeacherId();

            Uri uri = null;
            UserInfo userInfo;
            if(user!=null){
                if(!TextUtils.isEmpty(user.getHeadImgUrl())){
                    String headImg = AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl());
                    Log.i("HuiZhi", "The head img:" + headImg);
                    uri = Uri.parse(headImg);
                }
                userInfo = new UserInfo(targetid, user.getTeacherName(), uri);

            }else {
                userInfo = new UserInfo(targetid, "群聊", uri);
            }

            RongIM.getInstance().refreshUserInfoCache(userInfo);
        }
    }

    private class UserInfoUpdate implements BaseInfoUpdate{
        private String targetid;

        public UserInfoUpdate(String targetid){
            this.targetid = targetid;
        }

        @Override
        public void update(Object object) {
            if(object==null)
                return;
            final UserNode user = (UserNode)object;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    Uri uri = null;
                    UserInfo userInfo;
                    if(user!=null){
                        if(!TextUtils.isEmpty(user.getHeadImgUrl())){
                            String headImg = AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl());
                            Log.i("HuiZhi", "The head img:" + headImg);
                            uri = Uri.parse(headImg);
                        }
                        userInfo = new UserInfo(targetid, user.getTeacherName(), uri);

                    }else {
                        userInfo = new UserInfo(targetid, "群聊", uri);
                    }

                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("HuiZhi", "Come into  communicate list activity result");
        if(Constants.RESULT_CODE == resultCode){
            initViews();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
