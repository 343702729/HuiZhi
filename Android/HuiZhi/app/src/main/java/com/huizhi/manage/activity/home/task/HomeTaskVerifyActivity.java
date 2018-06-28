package com.huizhi.manage.activity.home.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.task.LogAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.ContentEntryDialog;
import com.huizhi.manage.dialog.InfoDialog;
import com.huizhi.manage.dialog.JudgeDialog;
import com.huizhi.manage.dialog.OperateSelDialog;
import com.huizhi.manage.dialog.PersonSelDialog;
import com.huizhi.manage.node.TaskAccessory;
import com.huizhi.manage.node.TaskAssignNode;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.request.home.HomeTaskPostRequest;
import com.huizhi.manage.request.task.TaskGetRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.DipPxUtil;
import com.huizhi.manage.wiget.util.Utility;
import com.huizhi.manage.wiget.view.FileItemView;
import com.huizhi.manage.wiget.view.PictureItemView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imageloader.utils.L;

/**
 * Created by CL on 2018/1/16.
 * 任务审核
 */

public class HomeTaskVerifyActivity extends Activity {
    private ListView listView;
    private LogAdapter logAdapter;
    private TaskNode taskNode;
    private String taskId;
    private List<TaskAccessory> picList = new ArrayList<>(), fileList = new ArrayList<>();
    private LinearLayout picturesLL, filesLL;
    private String personSelId;
    private ScrollView scrollView;
    private ImageButton backBtn;
    private LinearLayout picsShowLL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_verify);
        initDates();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDates();
    }

    private void initDates(){
        taskId = getIntent().getStringExtra("TaskId");
        taskNode = (TaskNode)getIntent().getSerializableExtra("Item");
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        picsShowLL = findViewById(R.id.pics_show_ll);

        picturesLL = findViewById(R.id.pictures_ll);
        filesLL = findViewById(R.id.files_ll);

        scrollView = findViewById(R.id.scrollView);

        listView = (ListView)findViewById(R.id.listview_log);
    }

    private void setDates(TaskNode node){
        if(node==null)
            return;
        TextView titleTV = findViewById(R.id.title_tv);
        titleTV.setText(node.getTaskTitle());
        TextView createTimeTV = findViewById(R.id.createtime_tv);
        createTimeTV.setText(node.getStrCreateTime());
        TextView descriptionTV = findViewById(R.id.description_tv);
        descriptionTV.setText(node.getTaskDescription());
        TextView taskTypeTV = findViewById(R.id.task_type_tv);
        taskTypeTV.setText(node.getTaskType());
        personSelId = taskNode.getProcessingTeacherId();
        TextView createPerTV = findViewById(R.id.create_per_tv);
        createPerTV.setText(node.getProcessingTeacherName());
        Button operateBtn = findViewById(R.id.operate_btn);
        operateBtn.setOnClickListener(operateBtnClick);

        setTaskPics(node);

        if(3==taskNode.getTaskStatus()||5==taskNode.getTaskStatus()){
            operateBtn.setVisibility(View.GONE);
        }else {
            operateBtn.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(scrollView.getLayoutParams());
            params.setMargins(0, 0, 0, DipPxUtil.dip2px(this, 50));
            scrollView.setLayoutParams(params);
        }
        TextView limittimeTV = findViewById(R.id.limttime_tv);
        if(taskNode.isTimeLimit()){
            limittimeTV.setText("限时: " + taskNode.getStrPlanEndTime());
            limittimeTV.setVisibility(View.VISIBLE);
        }
        setTaskPriority(node.getPriority());
        setTaskStatus(node.getTaskStatus());
        if(!node.isForAllTeacher()&&node.getcCTeacherLst()!=null&&node.getcCTeacherLst().size()>0){
            setCcpersons(node.getcCTeacherLst());
        }
        setAssignList(node.getTaskAssignLst());
        setAccessories(node);
        addAccessories();
    }

    private void setTaskPics(TaskNode node){
        if(node==null||node.getTaskMainAccessoryLst()==null)
            return;
        picsShowLL.removeAllViews();
        for(TaskAccessory item:node.getTaskMainAccessoryLst()){
            PictureItemView picItemV = new PictureItemView(this);
            picItemV.setDatas(item, node.getTaskMainAccessoryLst());
            picsShowLL.addView(picItemV);
        }

    }

    private void setAccessories(TaskNode node){
        if(node==null)
            return;
        picList.clear();
        fileList.clear();

        for(TaskAccessory item:node.getTaskAccessoryLst()){
            if(1==item.getFileType()){
                picList.add(item);
            }else if(2==item.getFileType()){
                fileList.add(item);
            }
        }
    }

    /**
     * 图片&附件加载
     */
    private void addAccessories(){
        picturesLL.removeAllViews();
        filesLL.removeAllViews();
        if(picList!=null){
            for(TaskAccessory item:picList)
                addPicture(item, picList);
        }

        if(fileList!=null){
            for(TaskAccessory item:fileList)
                addFile(item);
        }
    }

    private void addPicture(TaskAccessory accessory, List<TaskAccessory> picList){
        PictureItemView picItemV = new PictureItemView(this);
        picItemV.setDatas(accessory, picList);
        picturesLL.addView(picItemV);
    }

    private void addFile(TaskAccessory accessory){
        FileItemView fileItemV = new FileItemView(this, true);
        fileItemV.setDatas(accessory, fileList, null);
        filesLL.addView(fileItemV);
    }

    private void setAssignList(List<TaskAssignNode> assignNodes){
        if(assignNodes==null)
            return;
        logAdapter = new LogAdapter(this, assignNodes);
        listView.setAdapter(logAdapter);
        Utility.setListViewHeightBasedOnChildren(listView);
    }

    private void getDates(){
        if(TextUtils.isEmpty(taskId))
            return;
        TaskGetRequest taskRequest = new TaskGetRequest();
        taskRequest.getTaskDetail(taskId, UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    private void setTaskPriority(int priority){
        TextView lowTV = findViewById(R.id.level_l_tv);
        TextView middleTV = findViewById(R.id.level_m_tv);
        TextView highTV = findViewById(R.id.level_h_tv);
        lowTV.setVisibility(View.GONE);
        middleTV.setVisibility(View.GONE);
        highTV.setVisibility(View.GONE);
        if(priority==1){
            lowTV.setVisibility(View.VISIBLE);
        }else if(priority==2){
            middleTV.setVisibility(View.VISIBLE);
        }else if(priority==3){
            highTV.setVisibility(View.VISIBLE);
        }
    }

    private void setTaskStatus(int status){
        TextView pendingTV = findViewById(R.id.task_pending_tv);
        TextView auditTV = findViewById(R.id.task_audit_tv);
        TextView completeTV = findViewById(R.id.task_complete_tv);
        TextView refusedTV = findViewById(R.id.task_refused_tv);
        TextView closedTV = findViewById(R.id.task_close_tv);
        pendingTV.setVisibility(View.GONE);
        auditTV.setVisibility(View.GONE);
        completeTV.setVisibility(View.GONE);
        refusedTV.setVisibility(View.GONE);
        closedTV.setVisibility(View.GONE);
        if(status==1){
            pendingTV.setVisibility(View.VISIBLE);
        }else if(status==2){
            auditTV.setVisibility(View.VISIBLE);
        }else if(status==3){
            completeTV.setVisibility(View.VISIBLE);
        }else if(status==4){
            refusedTV.setVisibility(View.VISIBLE);
        }else if(status==5){
            closedTV.setVisibility(View.VISIBLE);
        }
    }

    private void setCcpersons(List<UserNode> users){
        if(users==null||users.size()==0)
            return;
        LinearLayout ccpersLL = findViewById(R.id.ccpers_ll);
        TextView ccpersTV = findViewById(R.id.ccpers_tv);
        String names = users.get(0).getTeacherName() + "等" + users.size() + "人";
        ccpersTV.setText(names);
        ccpersLL.setVisibility(View.VISIBLE);
        String info = "";
        for(UserNode user:users){
            info = info + user.getTeacherName() + "、";
        }
        info = info.substring(0, info.length()-1);
        ccpersTV.setOnClickListener(new CcpersonsBtnClick(info));
    }

    private class CcpersonsBtnClick implements View.OnClickListener {
        private String info;

        CcpersonsBtnClick(String info){
            this.info = info;
        }

        @Override
        public void onClick(View view) {
            InfoDialog infoDialog = new InfoDialog(HomeTaskVerifyActivity.this);
            infoDialog.showView(view, "抄送人", info);
        }
    };

    private View.OnClickListener operateBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            OperateSelDialog operateSelDialog = new OperateSelDialog(HomeTaskVerifyActivity.this, operateUpdate, taskNode);
            operateSelDialog.showView(view);
        }
    };

    private BaseInfoUpdate operateUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            int index = (int)object;
            Log.i("Task", "The operate index is:" +index);
            if(1==index){           //审核通过
                verifyPassItem();
            }else if(2==index){     //审核不通过
                ContentEntryDialog entryDialog = new ContentEntryDialog(HomeTaskVerifyActivity.this, true, "原因不能为空", verifyFaileInfo);
                entryDialog.showView(backBtn, "原因");
            }else if(3==index){     //删除
                JudgeDialog judgeDialog = new JudgeDialog(HomeTaskVerifyActivity.this, deleteInfoUpdate);
                judgeDialog.showView(picturesLL, "是否要删除该任务");
//                deleteItem();
            }else if(4==index){     //编辑
                editItem();
            }else if(5==index){     //重新指派
                PersonSelDialog perSelDialog = new PersonSelDialog(HomeTaskVerifyActivity.this, personSelId, perSelInfoUpdate);
                perSelDialog.showView(picturesLL);
            }
        }
    };


    private BaseInfoUpdate verifyFaileInfo = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            String reason = (String)object;
            if(TextUtils.isEmpty(reason))
                return;
            verifyFailedItem(reason);
        }
    };

    private BaseInfoUpdate perSelInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            String rePer = (String)object;
            if(rePer.equals(personSelId)) {
                Toast.makeText(HomeTaskVerifyActivity.this, "操作失败，处理人没有发生变化", Toast.LENGTH_SHORT).show();
                return;
            }
            personSelId = (String)object;
            ContentEntryDialog contentEntryDialog = new ContentEntryDialog(HomeTaskVerifyActivity.this, reSendInfoUpdate);
            contentEntryDialog.showView(picturesLL, "指派原因");
        }
    };

    private BaseInfoUpdate reSendInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            String reason = (String)object;
            Log.i("HuiZhi", "The re send :" + reason);
            HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
            postRequest.postReassignTask(taskNode.getTaskId(), UserInfo.getInstance().getUser().getTeacherId(), personSelId, reason, "", handler);//taskNode.getProcessingTeacherId()
        }
    };

    private BaseInfoUpdate deleteInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            deleteItem();
        }
    };

    private void editItem(){
        Intent intent = new Intent();
        intent.setClass(this, HomeTaskVerifyEditActivity.class);
        intent.putExtra("TaskId", taskId);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Item", taskNode);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void deleteItem(){
        HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
        postRequest.postAdminTaskDelete(taskNode.getTaskId(), handler);
    }

    private void verifyPassItem(){
        HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
        postRequest.postVerifyPass(taskNode.getTaskId(), UserInfo.getInstance().getUser().getTeacherId(), "审核通过", getAccessoryListStr(taskNode.getTaskAccessoryLst()), handler);
    }

    private void verifyFailedItem(String reason){
        HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
        postRequest.postVerifyFailed(taskNode.getTaskId(), UserInfo.getInstance().getUser().getTeacherId(), 1, reason, getAccessoryListStr(taskNode.getTaskAccessoryLst()), handler);
    }

    private String getAccessoryListStr(List<TaskAccessory> accessories){
        if(accessories==null)
            return "";
        JSONArray jsonAr = new JSONArray();
        JSONObject jsonOb = null;
        try{
            for (TaskAccessory node:accessories){
                jsonOb = new JSONObject();
                jsonOb.put("AccessoryType", node.getAccessoryType());
                jsonOb.put("FileType", node.getFileType());
                jsonOb.put("FileUrl", node.getFileUrl());
                jsonOb.put("FileSize", node.getFileSize());
                jsonOb.put("FileName", node.getFileName());
                jsonAr.put(jsonOb);
                jsonOb = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonAr.toString();
    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj==null)
                        return;
                    taskNode = (TaskNode)msg.obj;
                    setDates(taskNode);
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    Toast.makeText(HomeTaskVerifyActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case Constants.MSG_SUCCESS_TWO:
                    if(msg.obj==null)
                        return;
                    Toast.makeText(HomeTaskVerifyActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case Constants.MSG_SUCCESS_THREE:
                    if(msg.obj==null)
                        return;
                    Toast.makeText(HomeTaskVerifyActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    getDates();
                    break;
                case Constants.MSG_FAILURE:
                    if(msg.obj==null)
                        return;
                    Toast.makeText(HomeTaskVerifyActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


}
