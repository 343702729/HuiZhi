package com.huizhi.manage.activity.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.task.LogAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.TaskAccessory;
import com.huizhi.manage.node.TaskAssignNode;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.request.common.FileGetRequest;
import com.huizhi.manage.request.task.TaskGetRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.DateUtil;
import com.huizhi.manage.util.DipPxUtil;
import com.huizhi.manage.wiget.util.Utility;
import com.huizhi.manage.wiget.view.FileItemView;
import com.huizhi.manage.wiget.view.PictureItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/14.
 * 任务详情
 */

public class TaskDetailActivity extends Activity{
    private ListView listView;
    private LogAdapter logAdapter;
    private String taskId;
    private LinearLayout picturesLL, filesLL;
    private ScrollView scrollView;
    private List<TaskAccessory> picList = new ArrayList<>(), fileList = new ArrayList<>();
    private boolean isJoin = false;
    private LinearLayout picsShowLL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        getIntentData();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetailData();
    }

    private void getIntentData(){
        taskId = getIntent().getStringExtra("TaskId");
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        picsShowLL = findViewById(R.id.pics_show_ll);

        picturesLL = findViewById(R.id.pictures_ll);
        filesLL = findViewById(R.id.files_ll);

        scrollView = findViewById(R.id.scrollView);

        listView = (ListView)findViewById(R.id.listview_log);

    }

    /**
     * 获取任务详情
     */
    private void getDetailData(){
        if(TextUtils.isEmpty(taskId))
            return;
        TaskGetRequest taskRequest = new TaskGetRequest();
        taskRequest.getTaskDetail(taskId, UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    private void setViewsData(TaskNode node){
        if(node==null)
            return;
        Button finishedBtn = (Button)findViewById(R.id.task_do);
        finishedBtn.setOnClickListener(new ExecuteTaskClick(node));

        Button unfinishedBtn = (Button)findViewById(R.id.task_undo);
        unfinishedBtn.setOnClickListener(new ExecuteTaskClick(node));

        TextView titleTV = findViewById(R.id.title_tv);
        titleTV.setText(node.getTaskTitle());
        TextView createTTV = findViewById(R.id.createtime_tv);
        createTTV.setText(node.getStrCreateTime());
        TextView descriptionTV = findViewById(R.id.description_tv);
        descriptionTV.setText(node.getTaskDescription());
        TextView createPerTV = findViewById(R.id.create_per_tv);
        TextView taskTypeTV = findViewById(R.id.task_type_tv);
        taskTypeTV.setText(node.getTaskType());
        TextView limittimeTV = findViewById(R.id.limttime_tv);
        if(node.isTimeLimit()){
            limittimeTV.setText("限时: " + node.getStrPlanEndTime());
            limittimeTV.setVisibility(View.VISIBLE);
        }
        isJoin = node.getIsJoin();

        UserNode user = UserInfo.getInstance().getUserByTeacherId(node.getCreateTeacherId());
        if(user!=null)
            createPerTV.setText(user.getTeacherName());
        setTaskPics(node);
        setTaskPriority(node.getPriority());
        setTaskStatus(node.getTaskStatus(), node.isMoreProcessors());
        setAssignList(node.getTaskAssignLst());
        setAccessories(node);
        setStatusTime(node);
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

    private void setTaskOperate(int status){
        ImageView secondIV = findViewById(R.id.secnd_iv);
        ImageView thirdIV = findViewById(R.id.third_iv);
        View lineV = findViewById(R.id.line_v);
        TextView secondTV = findViewById(R.id.second_tv);
        TextView thirdTV = findViewById(R.id.third_tv);

        if(status==2){
            secondTV.setText("处理完成");
            thirdTV.setText("待审核");
            thirdTV.setTextColor(getResources().getColor(R.color.text_light_blue));
            secondIV.setBackground(getResources().getDrawable(R.mipmap.icon_task_done));
            thirdIV.setBackground(getResources().getDrawable(R.mipmap.icon_task_doing));
            lineV.setBackgroundColor(getResources().getColor(R.color.text_light_blue));
        }else if(status==3){
            secondTV.setText("处理完成");
            thirdTV.setText("审核完成");
            thirdTV.setTextColor(getResources().getColor(R.color.text_light_blue));
            secondIV.setBackground(getResources().getDrawable(R.mipmap.icon_task_done));
            thirdIV.setBackground(getResources().getDrawable(R.mipmap.icon_task_done));
            lineV.setBackgroundColor(getResources().getColor(R.color.text_light_blue));
        }else if(status==4){
            secondTV.setText("已处理");
            thirdTV.setText("待审核");
            thirdTV.setTextColor(getResources().getColor(R.color.text_light_blue));
            secondIV.setBackground(getResources().getDrawable(R.mipmap.icon_task_done));
            thirdIV.setBackground(getResources().getDrawable(R.mipmap.icon_task_doing));
            lineV.setBackgroundColor(getResources().getColor(R.color.text_light_blue));
        }else if(status==5){
            secondTV.setText("处理完成");
            thirdTV.setText("审核完成");
            thirdTV.setTextColor(getResources().getColor(R.color.text_light_blue));
            secondIV.setBackground(getResources().getDrawable(R.mipmap.icon_task_done));
            thirdIV.setBackground(getResources().getDrawable(R.mipmap.icon_task_done));
            lineV.setBackgroundColor(getResources().getColor(R.color.text_light_blue));
        }
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

    private void setTaskStatus(int status, boolean isMoreProcessors){
        setTaskOperate(status);
        TextView pendingTV = findViewById(R.id.task_pending_tv);
        TextView auditTV = findViewById(R.id.task_audit_tv);
        TextView completeTV = findViewById(R.id.task_complete_tv);
        TextView refusedTV = findViewById(R.id.task_refused_tv);
        TextView closedTV = findViewById(R.id.task_close_tv);
        LinearLayout bottmLL = findViewById(R.id.bottom_ll);

//        TextView timeSH
        pendingTV.setVisibility(View.GONE);
        auditTV.setVisibility(View.GONE);
        completeTV.setVisibility(View.GONE);
        refusedTV.setVisibility(View.GONE);
        closedTV.setVisibility(View.GONE);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(scrollView.getLayoutParams());
        params.setMargins(0, 0, 0, 0);
        if(status==1){
            params.setMargins(0, 0, 0, DipPxUtil.dip2px(this, 50));
            pendingTV.setVisibility(View.VISIBLE);
            bottmLL.setVisibility(View.VISIBLE);
        }else if(status==2){
            auditTV.setVisibility(View.VISIBLE);
            bottmLL.setVisibility(View.GONE);
        }else if(status==3){
            completeTV.setVisibility(View.VISIBLE);
            bottmLL.setVisibility(View.GONE);
        }else if(status==4){
            refusedTV.setVisibility(View.VISIBLE);
            bottmLL.setVisibility(View.GONE);
        }else if(status==5){
            closedTV.setVisibility(View.VISIBLE);
            bottmLL.setVisibility(View.GONE);
        }else {
            closedTV.setVisibility(View.VISIBLE);
            bottmLL.setVisibility(View.GONE);
        }
        if(isJoin){
            params.setMargins(0, 0, 0, 0);
            bottmLL.setVisibility(View.GONE);
        }
        if(isMoreProcessors&&status==1){
            LinearLayout undoLL = findViewById(R.id.task_undo_ll);
            undoLL.setVisibility(View.GONE);
            params.setMargins(0, 0, 0, DipPxUtil.dip2px(this, 50));
            pendingTV.setVisibility(View.VISIBLE);
            bottmLL.setVisibility(View.VISIBLE);
        }
        scrollView.setLayoutParams(params);
    }

    private void setStatusTime(TaskNode node){
        TextView timeFP = findViewById(R.id.time_fp_tv);
        TextView timeCL = findViewById(R.id.time_cl_tv);
        TextView timeSH = findViewById(R.id.time_sh_tv);
        timeFP.setText(DateUtil.parseHour(node.getTakeTimeFromCreateTask()));
        timeCL.setText(DateUtil.parseHour(node.getTakeTimeForFinishTask()));
        timeSH.setText(DateUtil.parseHour(node.getTakeTimeForApproveTask()));
//        if(node.getTaskStatus()==1){
//            timeFP.setText(DateUtil.parseMinute(node.getTakeTimeFromCreateTask()));
//        }else if(node.getTaskStatus()==2){
//            timeCL.setText(DateUtil.parseMinute(node.getTakeTimeForFinishTask()));
//        }else if(node.getTaskStatus()==3){
//            timeSH.setText(DateUtil.parseMinute(node.getTakeTimeForApproveTask()));
//        }
    }

    private void setTimes(){
        //任务分配now - strCreateTime

        //处理完成strCompleteTime-strCreateTime

        //审核完成strApprovalTime-strCreateTime
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

    private class ExecuteTaskClick implements View.OnClickListener{
        private TaskNode node;

        public ExecuteTaskClick(TaskNode node){
            this.node = node;
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.task_do:
                    Intent intent1 = new Intent();
                    intent1.setClass(TaskDetailActivity.this, TaskFinishedActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("Item", node);
                    intent1.putExtras(bundle1);
                    startActivity(intent1);
                    break;
                case R.id.task_undo:
                    Intent intent2 = new Intent();
                    intent2.setClass(TaskDetailActivity.this, TaskUnFinishedActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("Item", node);
                    intent2.putExtras(bundle2);
                    startActivity(intent2);
                    break;
            }
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
                    TaskNode node = (TaskNode)msg.obj;
                    setViewsData(node);
                    break;
            }
        }
    };

}
