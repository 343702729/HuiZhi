package com.huizhi.manage.activity.home.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.task.TaskDetailActivity;
import com.huizhi.manage.adapter.task.TaskAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.TaskPageNode;
import com.huizhi.manage.request.home.HomeTaskGetRequest;
import com.huizhi.manage.request.task.TaskGetRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.pullableview.PullToRefreshLayout;
import com.huizhi.manage.wiget.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/19.
 * 待办任务
 */

public class HomeTaskAgencyActivity extends Activity {
    public PullableListView listView;
    private TaskAdapter taskAdapter;
    private PullRefreshListener pullRefreshListener;
    private PullToRefreshLayout pullToRefreshRL;
//    private int currentPage = 1;
//    private int totalePages = 1;
//    private int pageSize = 10;
    private int taskStatus = 1;
    private int priority = 0;
    private String taskTitle = "";
    private List<TaskNode> taskNodes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_agency);
        initViews();
        getDates();
//        getDates(currentPage, pageSize, taskStatus, taskTitle, priority, handler);
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        pullToRefreshRL = (PullToRefreshLayout)findViewById(R.id.refreshview);
        pullToRefreshRL.isPullUp(false);
        pullRefreshListener = new PullRefreshListener();
        pullToRefreshRL.setOnRefreshListener(pullRefreshListener);
        listView = findViewById(R.id.listview);
        taskAdapter = new TaskAdapter(this, null);
        listView.setAdapter(taskAdapter);
        listView.setOnItemClickListener(taskItemClick);
    }

    private void getDates(int page, int pageSize, int taskstatus, String tasktitle, int priority, Handler handler){
        HomeTaskGetRequest getRequest = new HomeTaskGetRequest();
        getRequest.getAdminTaskList(page, pageSize, UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), taskstatus, UserInfo.getInstance().getUser().getTeacherName(),
                tasktitle, priority, "", "", "", "",  handler);


    }

    private void getDates(){
        TaskGetRequest taskRequest = new TaskGetRequest();
        taskRequest.getTaskList(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), priority, taskStatus, "", "", "", handler);
    }

    private void refreshDates(){
        getDates();
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
        }

        public void closeRefreshLoad(){
            if(refreshLayout!=null)
                refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            if(loadLayout!=null)
                loadLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    };

    private AdapterView.OnItemClickListener taskItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            TaskNode node = (TaskNode)adapterView.getItemAtPosition(position);
//            Intent intent = new Intent();
//            intent.putExtra("TaskId", node.getTaskId());
//            intent.setClass(HomeTaskAgencyActivity.this, HomeTaskVerifyActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("Item", node);
//            intent.putExtras(bundle);
//            startActivity(intent);
            Intent intent = new Intent();
            intent.setClass(HomeTaskAgencyActivity.this, TaskDetailActivity.class);
            intent.putExtra("TaskId", node.getTaskId());
            startActivity(intent);
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
                    taskNodes = (List<TaskNode>)msg.obj;
                    addViews(taskNodes);
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj==null)
                        return;
                    String message = (String)msg.obj;
                    Toast.makeText(HomeTaskAgencyActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MSG_FAILURE:

                    break;
            }
        }
    };

    private void addViews(List<TaskNode> nodes){
        if(nodes==null)
            return;
        taskAdapter.updateAdapter(nodes);
    }
}
