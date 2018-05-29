package com.huizhi.manage.activity.home.task;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.TaskMouldPagerAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.JudgeDialog;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.request.home.HomeTaskGetRequest;
import com.huizhi.manage.request.home.HomeTaskPostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.view.MouldStaffView;
import com.huizhi.manage.wiget.view.MouldTaskView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CL on 2018/2/26.
 * 日常任务模板
 */

public class HomeTaskMouldInfoActivity extends Activity {
    private String taskId;
    private TaskMouldNode taskMouldNode;
    private Button rwBtn, ryBtn;
    private ViewPager viewPager;
    private List<TaskMouldNode> subTasks = new ArrayList<>();
    private MouldTaskView taskV;
    private MouldStaffView staffV;
    private TaskMouldNode mouldNode;
    private TextView nameET;
    private boolean isEdit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_mould_info);
        initDates();
        initViews();
    }

    private void initDates(){
        taskId = getIntent().getStringExtra("Id");
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

        nameET = findViewById(R.id.name_et);

        rwBtn = (Button)findViewById(R.id.rw_btn);
        rwBtn.setOnClickListener(itemBtnClick);
        ryBtn = (Button)findViewById(R.id.ry_btn);
        ryBtn.setOnClickListener(itemBtnClick);

        Button editBtn = findViewById(R.id.edit_btn);
        editBtn.setOnClickListener(itemBtnClick);
        Button createBtn = findViewById(R.id.create_task_btn);
        createBtn.setOnClickListener(itemBtnClick);
        initPageView();
    }

    private void initPageView(){
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
        List<View> views = new ArrayList<>();
        taskV = new MouldTaskView(this, true);
        staffV = new MouldStaffView(this, true);
//        View staffV = inflater.inflate(R.layout.item_user_task_mould_staff, null);
        views.add(taskV);
        views.add(staffV);
        TaskMouldPagerAdapter pagerAdapter = new TaskMouldPagerAdapter(views);
        viewPager.setAdapter(pagerAdapter);
    }

    private View.OnClickListener itemBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rw_btn:   //1
                    setSelectBtn(1);
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.ry_btn:   //2
                    setSelectBtn(2);
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.edit_btn:
                    Intent intent = new Intent();
                    intent.setClass(HomeTaskMouldInfoActivity.this, HomeTaskMouldFoundActivity.class);
                    intent.putExtra("Edit", true);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Item", taskMouldNode);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.create_task_btn:
                    JudgeDialog judgeDialog = new JudgeDialog(HomeTaskMouldInfoActivity.this, createTaskInfo);
                    judgeDialog.showView(view, "是否创建任务");

                    break;
            }

        }

        private void setSelectBtn(int index){
            rwBtn.setTextColor(Resources.getSystem().getColor(android.R.color.darker_gray));
            ryBtn.setTextColor(Resources.getSystem().getColor(android.R.color.darker_gray));
            View rwV = (View)findViewById(R.id.rw_line);
            rwV.setBackgroundColor(Resources.getSystem().getColor(android.R.color.darker_gray));
            View ryV = (View)findViewById(R.id.ry_line);
            ryV.setBackgroundColor(Resources.getSystem().getColor(android.R.color.darker_gray));
            if(index==1){
                rwBtn.setTextColor(getResources().getColor(R.color.text_light_blue));
                rwV.setBackgroundColor(getResources().getColor(R.color.text_light_blue));
            }else{
                ryBtn.setTextColor(getResources().getColor(R.color.text_light_blue));
                ryV.setBackgroundColor(getResources().getColor(R.color.text_light_blue));
            }
        }

        private BaseInfoUpdate createTaskInfo = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(taskMouldNode==null)
                    return;
                HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
                postRequest.postCreateMouldTask(taskMouldNode.getCategoryId(), UserInfo.getInstance().getUser().getTeacherId(), "", handler);
            }
        };
    };

    private void getDates(){
        Log.i("HuiZhi", "Come into load");
        if(!TextUtils.isEmpty(taskId)){
            HomeTaskGetRequest getRequest = new HomeTaskGetRequest();
            getRequest.getMouldTaskDetail(taskId, handler);
        }
    }

    private void setViewsDate(TaskMouldNode mouldNode){
        nameET.setText(mouldNode.getCategoryName());
        subTasks = mouldNode.getTaskTemplateList();
        if(subTasks!=null){
            for(TaskMouldNode node:subTasks){
                String uniqueId = String.valueOf(new Date().getTime());
                node.setUniqueId(uniqueId);
            }
            if(taskV!=null)
                taskV.updateViews(subTasks);
            if(staffV!=null)
                staffV.updateViews(subTasks);
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
                    taskMouldNode = (TaskMouldNode)msg.obj;
                    setViewsDate(taskMouldNode);
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj==null)
                        return;
                    String message = (String)msg.obj;
                    Toast.makeText(HomeTaskMouldInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };
}
