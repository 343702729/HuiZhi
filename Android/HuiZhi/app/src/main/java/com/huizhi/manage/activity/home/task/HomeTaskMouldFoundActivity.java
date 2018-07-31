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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.ViewPagerAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.JudgeDialog;
import com.huizhi.manage.dialog.LoadingProgress;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.request.home.HomeTaskGetRequest;
import com.huizhi.manage.request.home.HomeTaskPostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.view.MouldStaffView;
import com.huizhi.manage.wiget.view.MouldTaskView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CL on 2017/12/19.
 * 日常任务模板
 */

public class HomeTaskMouldFoundActivity extends Activity{
    private Button rwBtn, ryBtn;
    private ViewPager viewPager;
    private List<TaskMouldNode> subTasks = new ArrayList<>();
    private MouldTaskView taskV;
    private MouldStaffView staffV;
    private TaskMouldNode mouldNode;
    private EditText nameET;
    private boolean isEdit = false;
    private LoadingProgress loadingProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_mould_found);
        initDates();
        initViews();
        getDates();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initDates(){
        mouldNode = (TaskMouldNode)getIntent().getSerializableExtra("Item");
        isEdit = getIntent().getBooleanExtra("Edit", false);
        Log.i("Task", "The isEdit:" + isEdit);
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(backBtnClick);

        Button addBtn = findViewById(R.id.add_task_btn);
        addBtn.setOnClickListener(addTaskBtnClick);

        nameET = findViewById(R.id.name_et);

        rwBtn = (Button)findViewById(R.id.rw_btn);
        rwBtn.setOnClickListener(itemBtnClick);
        ryBtn = (Button)findViewById(R.id.ry_btn);
        ryBtn.setOnClickListener(itemBtnClick);

        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(submitBtnClick);

        initPageView();
    }

    private void setViewsDate(TaskMouldNode mouldNode){
        nameET.setText(mouldNode.getCategoryName());
        subTasks = mouldNode.getTaskTemplateList();
        if(subTasks!=null){
            int i = 0;
            for(TaskMouldNode node:subTasks){
                String uniqueId = String.valueOf(new Date().getTime() + ++i);
                node.setUniqueId(uniqueId);
            }
            if(taskV!=null)
                taskV.updateViews(subTasks);
            if(staffV!=null)
                staffV.updateViews(subTasks);
        }
    }

    private void getDates(){
        if(mouldNode!=null){
            HomeTaskGetRequest getRequest = new HomeTaskGetRequest();
            getRequest.getMouldTaskDetail(mouldNode.getCategoryId(), handler);
        }
    }

    private View.OnClickListener backBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("HuiZhi", "Come into mould back:" + isEdit);
            backDo(view);
        }


    };

    private void backDo(View view){
        if(isEdit)
            finish();
        else {
            String title = nameET.getText().toString();
            if(!TextUtils.isEmpty(title)||subTasks.size()>=0) {
                JudgeDialog judgeDialog = new JudgeDialog(HomeTaskMouldFoundActivity.this, infoUpdate);
                judgeDialog.showView(view, "您创建的任务模板尚未保存，是否退出？");
            }else {
                finish();
            }

        }
    }

    BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            finish();
        }
    };

    private View.OnClickListener addTaskBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(HomeTaskMouldFoundActivity.this, HomeTaskMouldSubFoundActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    };

    private void initPageView(){
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
        List<View> views = new ArrayList<>();
        taskV = new MouldTaskView(this, false, deleteInfoUpdate);
        staffV = new MouldStaffView(this);
//        View staffV = inflater.inflate(R.layout.item_user_task_mould_staff, null);
        views.add(taskV);
        views.add(staffV);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(pagerAdapter);
    }

    private BaseInfoUpdate deleteInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            TaskMouldNode node = (TaskMouldNode)object;
            deleteTaskNode(node);
            HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
            postRequest.postMouldTaskItemDelete(node.getTemplateId(), handler);
        }
    };

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
    };

    private View.OnClickListener submitBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = nameET.getText().toString();
            if(TextUtils.isEmpty(name)){
                Toast.makeText(HomeTaskMouldFoundActivity.this, "模板名称不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            loadingProgress = new LoadingProgress(HomeTaskMouldFoundActivity.this, null);
            loadingProgress.showView(view);
            HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
            if(isEdit){
                postRequest.postMouldTaskEdit(UserInfo.getInstance().getUser().getTeacherId(), mouldNode.getCategoryId(), name, getTasksJS(subTasks), handler);
            }else {
                postRequest.postMouldTaskCreate(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getSwitchSchool().getSchoolId(), name, getTasksJS(subTasks), handler);
            }
        }

        private String getTasksJS(List<TaskMouldNode> nodes){
            JSONArray jsonAr = new JSONArray();
            JSONObject jsonOb = null;
            try{
                for (TaskMouldNode node:nodes){
                    jsonOb = new JSONObject();
                    jsonOb.put("TemplateId", node.getTemplateId());
                    jsonOb.put("TaskTitle", node.getTaskTitle());
                    jsonOb.put("TaskDescription", node.getTaskDescription());
                    jsonOb.put("Priority", node.getPriority());
                    jsonOb.put("ExecuteTeacherId", node.getExecuteTeacherId());
                    jsonAr.put(jsonOb);
                    jsonOb = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return jsonAr.toString();
        }

    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(loadingProgress!=null)
                loadingProgress.closeView();
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj==null)
                        return;
                    TaskMouldNode node = (TaskMouldNode)msg.obj;
                    setViewsDate(node);
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    Toast.makeText(HomeTaskMouldFoundActivity.this, "模板提交成功", Toast.LENGTH_SHORT).show();
                    if(UserInfo.getInstance().getTaskCreatInfo()!=null){
                        UserInfo.getInstance().getTaskCreatInfo().update(true);
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("HuiZhi", "come 123 into task custom result");
        if(requestCode==Constants.REQUEST_CODE && resultCode==Constants.RESULT_CODE){
            Log.i("HuiZhi", "come into task custom result");
            TaskMouldNode taskNode = (TaskMouldNode)data.getSerializableExtra("Item");
            if(taskNode!=null){
                Log.i("HuiZhi", "The task title is:" + taskNode.getTaskTitle());
                updateTaskNode(taskNode);
//                addSubTaskViews();
            }
        }

    }

    private void updateTaskNode(TaskMouldNode node){
        if(node==null)
            return;
        String uniqueId = node.getUniqueId();
        if(TextUtils.isEmpty(uniqueId))
            return;
        boolean isUpdate = false;
        for(TaskMouldNode itemN:subTasks){
            if(uniqueId.equals(itemN.getUniqueId())){
                int index = subTasks.indexOf(itemN);
                subTasks.remove(itemN);
                subTasks.add(index, node);
                isUpdate = true;
                break;
            }
        }
        if(!isUpdate)
            subTasks.add(node);
        if(taskV!=null)
            taskV.updateViews(subTasks);
        if(staffV!=null)
            staffV.updateViews(subTasks);
    }

    private void deleteTaskNode(TaskMouldNode node){
        if(node==null)
            return;
        Log.i("HuiZhi", "The delete node title:" + node.getTaskTitle());
        String uniqueId = node.getUniqueId();
        if(TextUtils.isEmpty(uniqueId))
            return;
        for(TaskMouldNode itemN:subTasks){
            if(uniqueId.equals(itemN.getUniqueId())){
                subTasks.remove(itemN);
                break;
            }
        }
        if(taskV!=null)
            taskV.updateViews(subTasks);
        if(staffV!=null)
            staffV.updateViews(subTasks);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            backDo(nameET);
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
