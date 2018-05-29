package com.huizhi.manage.activity.home.task;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.TaskCategoryNode;
import com.huizhi.manage.node.TaskSystemNode;
import com.huizhi.manage.request.home.HomeTaskGetRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.view.TaskSystemView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by CL on 2018/1/8.
 * 系统任务
 */

public class HomeTaskSystemMenuActivity extends Activity {
    private List<TaskCategoryNode> categoryNodes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_system_menu);
        initViews();
        getDates();
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

    }

    private void getDates(){
        HomeTaskGetRequest homeGetRequest = new HomeTaskGetRequest();
        homeGetRequest.getSystemTaskCategory(UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    private void setViews(List<TaskCategoryNode> categoryNodes){
        if(categoryNodes==null)
            return;
        LinearLayout bodyLL = findViewById(R.id.body_ll);
        for (TaskCategoryNode node:categoryNodes){
            TaskSystemView taskSystemView = new TaskSystemView(this);
            taskSystemView.setDates(node);
            bodyLL.addView(taskSystemView);
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj==null)
                        return;
                    categoryNodes = (List<TaskCategoryNode>)msg.obj;
                    HomeTaskGetRequest homeGetRequest = new HomeTaskGetRequest();
                    homeGetRequest.getSystemTasks(UserInfo.getInstance().getUser().getTeacherId(), handler);
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj==null)
                        return;
                    List<TaskSystemNode> systemNodes = (List<TaskSystemNode>)msg.obj;
                    Log.i("Task", "The tsn size:" + systemNodes.size());
                    sortTasks(systemNodes);
                    break;
            }
        }
    };

    private void sortTasks(List<TaskSystemNode> systemNodes){
        if(categoryNodes==null||systemNodes==null)
            return;
        Collections.sort(categoryNodes, new Comparator<TaskCategoryNode>() {
            @Override
            public int compare(TaskCategoryNode t0, TaskCategoryNode t1) {
                return t0.getSequenceNumber() - t1.getSequenceNumber();
            }
        });
        Collections.sort(systemNodes, new Comparator<TaskSystemNode>() {
            @Override
            public int compare(TaskSystemNode t0, TaskSystemNode t1) {
                return t0.getSequenceNumber() - t1.getSequenceNumber();
            }
        });
        for(TaskCategoryNode categoryNode:categoryNodes){
            String categoryId = categoryNode.getCategoryId();
            if(TextUtils.isEmpty(categoryId))
                continue;
            for(TaskSystemNode systemNode:systemNodes){
                if(categoryId.equals(systemNode.getCategoryId()))
                    categoryNode.addTaskSystemNode(systemNode);
            }
        }

        setViews(categoryNodes);
    }
}
