package com.huizhi.manage.activity.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.NewNode;
import com.huizhi.manage.node.NewPageNode;
import com.huizhi.manage.request.home.HomeUserGetRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.pullableview.PullToRefreshLayout;
import com.huizhi.manage.wiget.view.LineNewView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/2.
 * 绘智新闻
 */

public class HomeNewsActivity extends Activity {
    private PullRefreshListener pullRefreshListener;
    private PullToRefreshLayout pullRL;
    private LinearLayout newsLL;
    private int page = 1;
    private int totalPages = 1;
    private int pagesize = 10;
    private String catrgoryId = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_news);
        catrgoryId = getIntent().getStringExtra("CategoryId");
        initViews();
        getDates(page, pagesize, handler);
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        TextView titleTV = findViewById(R.id.title_tv);
        if("2".equals(catrgoryId))
            titleTV.setText("绘智产品");
        else
            titleTV.setText("绘智新闻");

        pullRefreshListener = new PullRefreshListener();
        pullRL = (PullToRefreshLayout)findViewById(R.id.refreshview);
        pullRL.isPullUp(false);
        pullRL.setOnRefreshListener(pullRefreshListener);

        newsLL = (LinearLayout)findViewById(R.id.news_ll);

    }

    private void getDates(int page, int pagesize, Handler handler){
        HomeUserGetRequest getRequest = new HomeUserGetRequest();
        getRequest.getNewList(page, pagesize, catrgoryId, UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    private void refreshDates(){
        page = 1;
        getDates(page, pagesize, handler);
    }

    private void loadMoreDates(){
        if(page+1>totalPages)
            return;
        page++;
        getDates(page, pagesize, addHandler);
    }

    private void loadDataViews(List<NewNode> nodes){
        if(totalPages>page)
            pullRL.isPullUp(true);
        else
            pullRL.isPullUp(false);
        if(nodes==null)
            return;
        LineNewView newView = null;
        for(int i=0; i<nodes.size();){
            newView = new LineNewView(HomeNewsActivity.this);
            NewNode node1 = nodes.get(i);
            NewNode node2 = null;
            boolean isEnd = false;
            if(i+1<nodes.size())
                node2 = nodes.get(++i);
            else
                isEnd = true;
            newView.setDates(node1, node2);
            newsLL.addView(newView);
            i++;
            if(isEnd)
                break;
        }
    }

    private class PullRefreshListener implements PullToRefreshLayout.OnRefreshListener {
        private PullToRefreshLayout refreshLayout, loadLayout;
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            refreshLayout = pullToRefreshLayout;
            refreshDates();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            loadLayout = pullToRefreshLayout;
            loadMoreDates();
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
                    NewPageNode node = (NewPageNode)msg.obj;
                    totalPages = node.getTotPageCount();
                    newsLL.removeAllViews();
                    loadDataViews(node.getNews());
                    break;
            }
        }
    };

    private Handler addHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(pullRefreshListener!=null)
                pullRefreshListener.closeRefreshLoad();
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj==null)
                        return;
                    NewPageNode node = (NewPageNode)msg.obj;
                    totalPages = node.getTotPageCount();
                    loadDataViews(node.getNews());
                    break;
            }
        }
    };
}
