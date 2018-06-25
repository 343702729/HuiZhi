package com.huizhi.manage.activity.home.task;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.task.TaskDetailActivity;
import com.huizhi.manage.adapter.home.TaskItemAdapter;
import com.huizhi.manage.adapter.task.TaskAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.SchoolSelDialog;
import com.huizhi.manage.dialog.TaskFilterDialog;
import com.huizhi.manage.dialog.TaskSortDialog;
import com.huizhi.manage.fragment.TaskFragment;
import com.huizhi.manage.node.SchoolNode;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.TaskPageNode;
import com.huizhi.manage.request.home.HomeTaskGetRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.pullableview.PullToRefreshLayout;
import com.huizhi.manage.wiget.pullableview.PullableListView;
import com.huizhi.manage.wiget.swipe.SwipeListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/16.
 * 我的任务
 */

public class HomeTaskAllocationActivity extends Activity {
    private SwipeListView listView;
    private TaskItemAdapter taskAdapter;
    private PullRefreshListener pullRefreshListener;
    private PullToRefreshLayout pullToRefreshRL;
    private int currentPage = 1;
    private int totalePages = 1;
    private int pageSize = 10;
    private int taskStatus = 0;
    private int priority = 0;
    private String taskTitle = "";
    private List<TaskNode> taskNodes = new ArrayList<>();
    private TaskSortDialog taskSortDialog;
    private TaskFilterDialog taskFilterDialog;
//    private int priority = 0, createtime = 0, completetime = 0, limittime = 0;
    private int prioritySort = -1, creatTimeSort = -1, endTimeSort = -1, limitTimeSort = -1;
    private String isLimtTime, createTime, userSel;
    private String schoolId;
    private SchoolNode schoolNode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_allocation);
        UserInfo.getInstance().setTaskCreatInfo(taskCreateInfo);
        initViews();
        getDates(currentPage, pageSize, handler);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserInfo.getInstance().setTaskCreatInfo(null);
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        schoolId = UserInfo.getInstance().getUser().getSchoolId();
        schoolNode = UserInfo.getInstance().getSwitchSchool();

        Button switchBtn = findViewById(R.id.switch_btn);
        switchBtn.setOnClickListener(switchSchoolBtnClick);

        Button sortBtn = findViewById(R.id.sort_btn);
        sortBtn.setOnClickListener(itemBtnClick);
        ImageView sortIV = findViewById(R.id.sort_iv);
        taskSortDialog = new TaskSortDialog(this, sortBtn, sortIV, sortInfoUpdate);
        Button filterBtn = findViewById(R.id.filter_btn);
        filterBtn.setOnClickListener(itemBtnClick);
        ImageView filterIV = findViewById(R.id.filter_iv);
        taskFilterDialog = new TaskFilterDialog(this, filterBtn, filterIV, true, filterInfoUpdate);

        Button createBtn = (Button)findViewById(R.id.create_btn);
        createBtn.setOnClickListener(createBtnClick);
        pullToRefreshRL = (PullToRefreshLayout)findViewById(R.id.refreshview);
        pullToRefreshRL.isPullUp(false);
        pullRefreshListener = new PullRefreshListener();
        pullToRefreshRL.setOnRefreshListener(pullRefreshListener);
        listView = findViewById(R.id.listview);
        taskAdapter = new TaskItemAdapter(this, null, handler);
        listView.setAdapter(taskAdapter);
        listView.setOnItemClickListener(taskItemClick);
    }

    private View.OnClickListener switchSchoolBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SchoolSelDialog schoolSelDialog = new SchoolSelDialog(HomeTaskAllocationActivity.this, schoolId, infoUpdate);
            schoolSelDialog.showView(view);
        }

        BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;
                schoolNode = (SchoolNode)object;
            }
        };
    };

    private BaseInfoUpdate taskCreateInfo = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            Log.i("HuiZhi", "Come into reload tasks");
            getDates(currentPage, pageSize, handler);
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
            }
        }
    };

    private BaseInfoUpdate sortInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            int[] result = (int[])object;
            prioritySort = (int)result[0];
            creatTimeSort = (int)result[1];
            endTimeSort = (int)result[2];
            limitTimeSort = (int)result[3];
            refreshDates();
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
            isLimtTime = (String) result[2];
            createTime = (String) result[3];
            userSel = (String)result[4];
            refreshDates();
        }
    };

    private void getDates(int page, int pageSize, Handler handler){
        Log.i("HuiZhi", "The total pages:" + totalePages);
        HomeTaskGetRequest getRequest = new HomeTaskGetRequest();
        getRequest.getAdminTaskList(page, pageSize, UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), taskStatus, UserInfo.getInstance().getUser().getTeacherName(),
                taskTitle, priority, isLimtTime, createTime, userSel, getSortString(),  handler);
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

    private void refreshDates(){
        currentPage = 1;
        if(currentPage<totalePages)
            pullToRefreshRL.isPullUp(true);
        getDates(currentPage, pageSize, handler);
    }

    private void loadMoreDates(){
        if((currentPage+1)>totalePages) {
            pullToRefreshRL.isPullUp(false);
            return;
        }
        currentPage++;
        getDates(currentPage, pageSize, addHandler);
    }

    private AdapterView.OnItemClickListener taskItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            TaskNode node = (TaskNode)adapterView.getItemAtPosition(position);
            Intent intent = new Intent();
            intent.putExtra("TaskId", node.getTaskId());
            intent.setClass(HomeTaskAllocationActivity.this, HomeTaskVerifyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Item", node);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

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

    private View.OnClickListener createBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(HomeTaskAllocationActivity.this, HomeTaskAllotActivity.class);
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
                    TaskPageNode node = (TaskPageNode)msg.obj;
                    totalePages = node.getTotPageCount();
                    setMyInfo(node.getMyAssigned(), node.getMyToBeApprove());
                    updateViews(node.getTasks());
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj==null)
                        return;
                    String message = (String)msg.obj;
                    Toast.makeText(HomeTaskAllocationActivity.this, message, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MSG_FAILURE:

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
                    TaskPageNode node = (TaskPageNode)msg.obj;
                    totalePages = node.getTotPageCount();
                    setMyInfo(node.getMyAssigned(), node.getMyToBeApprove());
                    addViews(node.getTasks());
                    break;
                case Constants.MSG_FAILURE:

                    break;
            }
        }
    };

    private void setMyInfo(int assigned, int beApprove){
        TextView assignedTV = findViewById(R.id.assigned_tv);
        assignedTV.setText(String.valueOf(assigned));
        TextView beapproveTV = findViewById(R.id.beapprove_tv);
        beapproveTV.setText(String.valueOf(beApprove));
    }

    private void updateViews(List<TaskNode> nodes){
        if(nodes==null)
            return;
        if(currentPage<totalePages)
            pullToRefreshRL.isPullUp(true);
        taskNodes.clear();
        taskNodes.addAll(nodes);
        taskAdapter.updateAdapter(taskNodes);
    }

    private void addViews(List<TaskNode> nodes){
        if(nodes==null)
            return;
        taskNodes.addAll(nodes);
        taskAdapter.updateAdapter(taskNodes);
        if(currentPage+1>totalePages)
            pullToRefreshRL.isPullUp(false);
    }
}
