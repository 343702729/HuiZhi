package com.huizhi.manage.activity.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.task.TaskAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.LoadingProgress;
import com.huizhi.manage.dialog.TaskFilterDialog;
import com.huizhi.manage.dialog.TaskSortDialog;
import com.huizhi.manage.fragment.TaskFragment;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.TaskSummaryNode;
import com.huizhi.manage.request.home.HomeUserGetRequest;
import com.huizhi.manage.request.task.TaskGetRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.pullableview.PullToRefreshLayout;
import com.huizhi.manage.wiget.pullableview.PullableListView;

import java.util.List;

public class TaskCenterActivity extends Activity {
    private Activity activity;
    private PullableListView listView;
    private TaskAdapter taskAdapter;
    private PullRefreshListener pullRefreshListener;
    private TaskSortDialog taskSortDialog;
    private TaskFilterDialog taskFilterDialog;
    private int priority = 0, taskStatus = 0;
    private int prioritySort = -1, creatTimeSort = -1, endTimeSort = -1, limitTimeSort = -1;
    private String  isLimtTime = "", createTime = "", sortString = "";
    private boolean isDTotal = false, isJTotal = false;
    private TextView taskTTTV, todayTTTV, taskTITV, todayTITV;
    private LoadingProgress loadingProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_task);
        activity = this;
        initViews();
//        getDates();
        getTaskListData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDates();
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));
        Button sortBtn = findViewById(R.id.sort_btn);
        sortBtn.setOnClickListener(itemBtnClick);
        ImageView sortIV = findViewById(R.id.sort_iv);
        taskSortDialog = new TaskSortDialog(activity, sortBtn, sortIV, sortInfoUpdate);
        Button filterBtn = findViewById(R.id.filter_btn);
        filterBtn.setOnClickListener(itemBtnClick);
        ImageView filterIV = findViewById(R.id.filter_iv);
        taskFilterDialog = new TaskFilterDialog(activity, filterBtn, filterIV, filterInfoUpdate);
        taskTTTV = findViewById(R.id.task_total_tv);
        taskTITV = findViewById(R.id.task_total_t_tv);
        todayTTTV = findViewById(R.id.today_total_tv);
        todayTITV = findViewById(R.id.today_total_t_tv);
        taskTTTV.setOnClickListener(itemBtnClick);
        todayTTTV.setOnClickListener(itemBtnClick);
        initListViews();
    }

    private void getDates(){
        HomeUserGetRequest getRequest = new HomeUserGetRequest();
        getRequest.getUserTaskSummary(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), handler);
    }

    private void initListViews(){
        pullRefreshListener = new PullRefreshListener();
        listView = (PullableListView)findViewById(R.id.listview);
        PullToRefreshLayout pullRL = (PullToRefreshLayout)findViewById(R.id.refreshview);
        pullRL.isPullUp(false);
        pullRL.setOnRefreshListener(pullRefreshListener);
        taskAdapter = new TaskAdapter(activity, null);
        listView.setAdapter(taskAdapter);
        listView.setOnItemClickListener(taskItemClick);
    }

    private class PullRefreshListener implements PullToRefreshLayout.OnRefreshListener {
        private PullToRefreshLayout refreshLayout, loadLayout;
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            refreshLayout = pullToRefreshLayout;
            getTaskListData();
//            handler.sendEmptyMessageDelayed(1, 3000);
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

    private View.OnClickListener itemBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("HuiZhi", "Come into item btn click");
            if(view.getId()==R.id.sort_btn){
                if(!taskSortDialog.isShow())
                    taskSortDialog.showView(view);
                else
                    taskSortDialog.closeView();
            }else if(view.getId()==R.id.filter_btn){
                if(!taskFilterDialog.isShow())
                    taskFilterDialog.showView(view);
                else
                    taskFilterDialog.closeView();
            }else if(view.getId()==R.id.task_total_tv){
                Log.i("HuiZhi", "Task total click");
                if(!isDTotal) {
                    isDTotal = true;
                    isJTotal = false;
                    taskTTTV.setTextColor(getResources().getColor(R.color.light_d_red));
                    taskTITV.setTextColor(getResources().getColor(R.color.light_d_red));
                    todayTTTV.setTextColor(getResources().getColor(R.color.light_black));
                    todayTITV.setTextColor(getResources().getColor(R.color.dark_gray));
                    priority = 0; taskStatus = 1; isLimtTime = ""; createTime = "";
                    prioritySort = 1; creatTimeSort = 1; endTimeSort = 1; limitTimeSort = 1;
                    getTaskListData(priority, taskStatus, isLimtTime, createTime);
                }else {
                    isDTotal = false;
                    taskTTTV.setTextColor(getResources().getColor(R.color.light_black));
                    taskTITV.setTextColor(getResources().getColor(R.color.dark_gray));
                    priority = 0; taskStatus = 0; isLimtTime = ""; createTime = "";
                    prioritySort = -1; creatTimeSort = -1; endTimeSort = -1; limitTimeSort = -1;
                    getTaskListNDData();
//                    getTaskListData(priority, taskStatus, isLimtTime, createTime);
                }
            }else if(view.getId()==R.id.today_total_tv){
                Log.i("HuiZhi", "Toady total click");
                if(!isJTotal) {
                    isJTotal = true;
                    isDTotal = false;
                    todayTTTV.setTextColor(getResources().getColor(R.color.light_d_red));
                    todayTITV.setTextColor(getResources().getColor(R.color.light_d_red));
                    taskTTTV.setTextColor(getResources().getColor(R.color.light_black));
                    taskTITV.setTextColor(getResources().getColor(R.color.dark_gray));
                    priority = 0; taskStatus = 1; isLimtTime = ""; createTime = "1";
                    prioritySort = 1; creatTimeSort = 1; endTimeSort = 1; limitTimeSort = 1;
                    getTaskListData(priority, taskStatus, isLimtTime, createTime);
                }else {
                    isJTotal = false;
                    todayTTTV.setTextColor(getResources().getColor(R.color.light_black));
                    todayTITV.setTextColor(getResources().getColor(R.color.dark_gray));
                    priority = 0; taskStatus = 0; isLimtTime = ""; createTime = "";
                    prioritySort = -1; creatTimeSort = -1; endTimeSort = -1; limitTimeSort = -1;
                    getTaskListNDData();
//                    getTaskListData(priority, taskStatus, isLimtTime, createTime);
                }

            }
        }
    };

    private BaseInfoUpdate sortInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            //prioritySort = 0, creatTimeSort = 0, endTimeSort = 0, completeTimeSort = 0;
            int[] result = (int[])object;
            prioritySort = (int)result[0];
            creatTimeSort = (int)result[1];
            endTimeSort = (int)result[2];
            limitTimeSort = (int)result[3];

            getTaskListData();
        }
    };

    private BaseInfoUpdate filterInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            Object[] result = (Object[])object;
            priority = (int)result[0];
            taskStatus = (int)result[1];
            isLimtTime = (String)result[2];
            createTime = (String) result[3];
            getTaskListData();
        }
    };

    private AdapterView.OnItemClickListener taskItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            TaskNode node = (TaskNode)adapterView.getItemAtPosition(position);
            Intent intent = new Intent();
            intent.putExtra("TaskId", node.getTaskId());
            intent.setClass(activity, TaskDetailActivity.class);
            startActivity(intent);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    if(pullRefreshListener!=null)
                        pullRefreshListener.closeRefreshLoad();
                    break;
                case Constants.MSG_SUCCESS:
//                    loadingProgress.closeView();
                    if(pullRefreshListener!=null)
                        pullRefreshListener.closeRefreshLoad();
                    if(msg.obj == null)
                        return;
                    List<TaskNode> nodes = (List<TaskNode>)msg.obj;
                    taskAdapter.updateAdapter(nodes);
                    break;
                case Constants.MSG_SUCCESS_TWO:
                    if(msg.obj==null)
                        return;
                    TaskSummaryNode summaryNode = (TaskSummaryNode)msg.obj;
                    updateTaskInfo(summaryNode);
                    break;
            }
        }
    };

    /**
     * 获取任务列表
     */
    private void getTaskListData(){
//        loadingProgress = new LoadingProgress(activity, null);
//        loadingProgress.showView(taskTTTV);
        TaskGetRequest taskRequest = new TaskGetRequest();
        taskRequest.getTaskList(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), priority, taskStatus, isLimtTime, createTime, getSortString(), handler);
    }

    private void getTaskListNDData(){
        TaskGetRequest taskRequest = new TaskGetRequest();
        taskRequest.getTaskList(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), priority, taskStatus, isLimtTime, createTime, getSortString(), handler);
    }

    private void getTaskListData(int priority, int taskStatus, String isLimtTime, String createTime){
        TaskGetRequest taskRequest = new TaskGetRequest();
        taskRequest.getTaskList(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), priority, taskStatus, isLimtTime, createTime, getDescSortString(), handler);
    }

    private String getSortString(){
        StringBuilder sb = new StringBuilder();
        if(prioritySort==0){
            sb.append("Priority asc,");
        }else if(prioritySort==1){
            sb.append("Priority desc,");
        }

        if(creatTimeSort==0){
            sb.append("CreateTime asc,");
        }else if(creatTimeSort==1){
            sb.append("CreateTime desc,");
        }

        if(endTimeSort==0){
            sb.append("PlanEndTime asc,");
        }else if(endTimeSort==1){
            sb.append("PlanEndTime desc,");
        }

        if(limitTimeSort==0){
            sb.append("CompleteTime asc");
        }else if(limitTimeSort==1){
            sb.append("CompleteTime desc");
        }

        String str = sb.toString();
        if(TextUtils.isEmpty(str))
            return str;
        if(str.endsWith(","))
            str = str.substring(0, str.length()-1);
        return str;
    }

    private String getDescSortString(){
        StringBuilder sb = new StringBuilder();

        sb.append("Priority desc,");
        sb.append("CreateTime desc,");
        sb.append("PlanEndTime desc,");
        sb.append("CompleteTime desc");

        String str = sb.toString();
        if(TextUtils.isEmpty(str))
            return str;
        if(str.endsWith(","))
            str = str.substring(0, str.length()-1);
        return str;
    }

    private void updateTaskInfo(TaskSummaryNode node){
        TextView totalTTV = findViewById(R.id.task_total_tv);
        totalTTV.setText(String.valueOf(node.getTotalToDoTaskCount()));
        TextView todayTTV = findViewById(R.id.today_total_tv);
        todayTTV.setText(String.valueOf(node.getTotalTodayToDoTaskCount()));
    }
}
