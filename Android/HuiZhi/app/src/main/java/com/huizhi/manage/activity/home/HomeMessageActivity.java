package com.huizhi.manage.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.task.TaskDetailActivity;
import com.huizhi.manage.adapter.home.MessageAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.fragment.TaskFragment;
import com.huizhi.manage.node.MessageNode;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.request.home.HomeUserGetRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.pullableview.PullToRefreshLayout;
import com.huizhi.manage.wiget.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/25.
 * 消息通知
 */

public class HomeMessageActivity extends Activity {
    private PullableListView listView;
    private PullRefreshListener pullRefreshListener;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_message);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDates();
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        pullRefreshListener = new PullRefreshListener();
        listView = (PullableListView)findViewById(R.id.listview);
        PullToRefreshLayout pullRL = (PullToRefreshLayout)findViewById(R.id.refreshview);
        pullRL.isPullUp(false);
        pullRL.setOnRefreshListener(pullRefreshListener);
        messageAdapter = new MessageAdapter(this, null);
        listView.setAdapter(messageAdapter);
        listView.setOnItemClickListener(messageItemClick);
    }

    private void getDates(){
        HomeUserGetRequest getRequest = new HomeUserGetRequest();
        getRequest.getUserMessageList(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), handler);
    }

    private void setDateViews(List<MessageNode> nodes){
        messageAdapter.updateAdapter(nodes);
    }

    private AdapterView.OnItemClickListener messageItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            MessageNode node = (MessageNode)adapterView.getItemAtPosition(position);
            Intent intent = new Intent();
            intent.setClass(HomeMessageActivity.this, HomeMessageInfoActivity.class);
            intent.putExtra("Id", node.getNoticeId());
            startActivity(intent);
        }
    };

    private class PullRefreshListener implements PullToRefreshLayout.OnRefreshListener {
        private PullToRefreshLayout refreshLayout, loadLayout;
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            refreshLayout = pullToRefreshLayout;
            getDates();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            loadLayout = pullToRefreshLayout;
//            handler.sendEmptyMessageDelayed(2, 3000);
        }

        public void closeRefreshLoad(){
            if(refreshLayout!=null)
                refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            if(loadLayout!=null)
                loadLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(pullRefreshListener!=null)
                pullRefreshListener.closeRefreshLoad();
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj==null)
                        return;
                    List<MessageNode> nodes = (List<MessageNode>)msg.obj;
                    setDateViews(nodes);
                    break;
            }
        }
    };
}
