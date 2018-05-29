package com.huizhi.manage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.communicate.CommunicateActivity;
import com.huizhi.manage.adapter.communicate.CommunicateUsersAdapter;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.CommunicateUserNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.wiget.pullableview.PullToRefreshLayout;
import com.huizhi.manage.wiget.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by CL on 2017/12/13.
 */

public class CommunicateFragment extends Fragment{
    private View messageLayout;
    private Activity activity;
    private ListView listView;
    private CommunicateUsersAdapter usersAdapter;
    private Fragment mConversationFragment = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_communicate, container, false);
        activity = getActivity();
        initViews();
        return messageLayout;
    }

    private void initViews(){
        initListViews();

    }

    @Override
    public void onResume() {
        super.onResume();
        initRongMessage();
    }

    private void initListViews(){
        listView = messageLayout.findViewById(R.id.listview);
        listView.setOnItemClickListener(listItemClick);
        usersAdapter = new CommunicateUsersAdapter(activity, UserInfo.getInstance().getTeamUsers());
        listView.setAdapter(usersAdapter);
    }

    private class PullRefreshListener implements PullToRefreshLayout.OnRefreshListener {
        private PullToRefreshLayout refreshLayout, loadLayout;
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            refreshLayout = pullToRefreshLayout;
            handler.sendEmptyMessageDelayed(1, 3000);
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            loadLayout = pullToRefreshLayout;
            handler.sendEmptyMessageDelayed(2, 3000);
        }

        public void closeRefreshLoad(){
            if(refreshLayout!=null)
                refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            if(loadLayout!=null)
                loadLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    };

    private AdapterView.OnItemClickListener listItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            Intent intent = new Intent();
//            intent.setClass(activity, CommunicateActivity.class);
//            activity.startActivity(intent);
            UserNode userNode = (UserNode)usersAdapter.getItem(i);
            RongIM.getInstance().startPrivateChat(activity, userNode.getTeacherId(), userNode.getTeacherName());
        }
    };

    /**
     * 融云消息接收，及初始化
     */
    private void initRongMessage() {
        int unreadCount1 = RongIM.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, "123");
        int unreadCount2 = RongIM.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, "456");
        Log.i("HuiZhi", "The 123 message1:" + unreadCount1 + "   message2:" + unreadCount2);
//        final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
//                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
//                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE};
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener, conversationTypes);
////              RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener1, Conversation.ConversationType.APP_PUBLIC_SERVICE);
//            }
//        }, 500);
    }

    public RongIM.OnReceiveUnreadCountChangedListener mCountListener = new RongIM.OnReceiveUnreadCountChangedListener() {
        @Override
        public void onMessageIncreased(int count) {
            if (count == 0) {
                Log.i("HuiZhi", "The message 1 coun:" + count);
//                mUnreadNumView.setVisibility(View.GONE);
            } else if (count > 0 && count < 100) {
                Log.i("HuiZhi", "The message 2 coun:" + count);
//                mUnreadNumView.setVisibility(View.VISIBLE);
//                mUnreadNumView.setText(count + "");
            } else {
                Log.i("HuiZhi", "The message 3 coun:" + count);
//                mUnreadNumView.setVisibility(View.VISIBLE);
//                mUnreadNumView.setText(R.string.no_read_message);
            }
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
            }
        }
    };
}
