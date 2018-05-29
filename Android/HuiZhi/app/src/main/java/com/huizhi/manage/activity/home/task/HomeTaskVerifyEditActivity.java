package com.huizhi.manage.activity.home.task;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.ContentEntryDialog;
import com.huizhi.manage.dialog.PersonMultSelDialog;
import com.huizhi.manage.dialog.PersonSelDialog;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.request.home.HomeTaskPostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.PictureUtil;
import com.huizhi.manage.wiget.SwitchView;
import com.huizhi.manage.wiget.datepicker.CustomDatePicker;
import com.huizhi.manage.wiget.view.SubTaskItemView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by CL on 2018/1/19.
 * 任务编辑
 */

public class HomeTaskVerifyEditActivity extends Activity {
    private TaskNode taskNode;
    private String taskId;
    private Button priHBtn, priMbtn, priLBtn;
    private FrameLayout limtTimeFL;
    private SwitchView switchBtn;
    private TextView limtTimeTV;
    private int priority = 3;
    private boolean isLimitTime = false;
    private FrameLayout titleFL, descriptionFL;
    private TextView titleTV, descriptionTV, personTV, personCTV;
    private ImageView personIV, personCIV;
    private String personSelId, personCSelId = "";
    private LinearLayout personCCLL;;
    private List<TaskNode> subTasks = new ArrayList<>();
    private LinearLayout subTaskLL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_verify_edit);
        initDates();
        initViews();
    }

    private void initDates(){
        taskId = getIntent().getStringExtra("TaskId");
        taskNode = (TaskNode)getIntent().getSerializableExtra("Item");
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        titleTV = findViewById(R.id.title_tv);
        titleFL = findViewById(R.id.title_ll);
        titleFL.setOnClickListener(titleFLClick);

        descriptionTV = findViewById(R.id.description_tv);
        descriptionFL = findViewById(R.id.description_fl);
        descriptionFL.setOnClickListener(descriptionFLClick);

        personTV = findViewById(R.id.person_tv);
        personIV = findViewById(R.id.person_iv);

        LinearLayout personSelLL = findViewById(R.id.person_sel_ll);
        personSelLL.setOnClickListener(personSelClick);

        priHBtn = (Button)findViewById(R.id.pro_h_btn);
        priHBtn.setOnClickListener(priorityBtnClick);
        priMbtn = (Button)findViewById(R.id.pro_m_btn);
        priMbtn.setOnClickListener(priorityBtnClick);
        priLBtn = (Button)findViewById(R.id.pro_l_btn);
        priLBtn.setOnClickListener(priorityBtnClick);
        switchBtn = findViewById(R.id.switch_btn);
        switchBtn.setOnStateChangedListener(timeSwitchChange);
        limtTimeFL = findViewById(R.id.timelimit_fl);
        limtTimeFL.setOnClickListener(timeLimitClick);
        limtTimeTV = findViewById(R.id.limttime_tv);
        personCCLL = findViewById(R.id.person_c_ll);
        personCTV = findViewById(R.id.person_c_tv);
        personCIV = findViewById(R.id.person_c_iv);
        LinearLayout personCSelLL = findViewById(R.id.person_c_sel_ll);
        personCSelLL.setOnClickListener(personCSelClick);

        subTaskLL = findViewById(R.id.sub_task_ll);

        if(taskNode!=null){
            titleTV.setText(taskNode.getTaskTitle());
            descriptionTV.setText(taskNode.getTaskDescription());
            priorityBtnSel(taskNode.getPriority());
            setPersonInfo(taskNode.getProcessingTeacherId());
            if(taskNode.isTimeLimit()) {
                isLimitTime = taskNode.isTimeLimit();
                switchBtn.setState(true);
                limtTimeFL.setVisibility(View.VISIBLE);
                limtTimeTV.setText(taskNode.getStrPlanEndTime());
            }

            //taskNode.getTaskCreateType()==1&&
            if(!taskNode.isForAllTeacher()&&taskNode.getcCTeacherLst()!=null&&taskNode.getcCTeacherLst().size()>0){
                List<UserNode> users = taskNode.getcCTeacherLst();
                setCCPers(users);
                personCCLL.setVisibility(View.VISIBLE);
            }
            if(taskNode.getSubTaskNodes()!=null) {
                subTasks = taskNode.getSubTaskNodes();
                addSubTaskViews();
            }
            Log.i("HuiZhi", "The sub nodes:" +subTasks.size());
        }

        ImageButton subTaskAddBtn = findViewById(R.id.add_subtask_btn);
        subTaskAddBtn.setOnClickListener(addSubTaskClick);

        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(submitBtnClick);
    }

    private View.OnClickListener priorityBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.pro_h_btn:
                    priorityBtnSel(3);
                    break;
                case R.id.pro_m_btn:
                    priorityBtnSel(2);
                    break;
                case R.id.pro_l_btn:
                    priorityBtnSel(1);
                    break;
            }
        }
    };

    private void priorityBtnSel(int index){
        priority = index;
        //setBackground(context.getResources().getDrawable(R.drawable.frame_daily_do));
        priHBtn.setBackground(getResources().getDrawable(R.drawable.frame_light_gray));
        priMbtn.setBackground(getResources().getDrawable(R.drawable.frame_light_gray));
        priLBtn.setBackground(getResources().getDrawable(R.drawable.frame_light_gray));
        priHBtn.setTextColor(getResources().getColor(R.color.gray));
        priMbtn.setTextColor(getResources().getColor(R.color.gray));
        priLBtn.setTextColor(getResources().getColor(R.color.gray));
        if(index==3){
            priHBtn.setBackground(getResources().getDrawable(R.drawable.frame_bg_red));
            priHBtn.setTextColor(getResources().getColor(R.color.white));
        }else if(index==2){
            priMbtn.setBackground(getResources().getDrawable(R.drawable.frame_bg_red));
            priMbtn.setTextColor(getResources().getColor(R.color.white));
        }else if(index==1){
            priLBtn.setBackground(getResources().getDrawable(R.drawable.frame_bg_red));
            priLBtn.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void setCCPers(List<UserNode> users){
        if(users==null||users.size()==0)
            return;
        if(users.size()>1){
            personCTV.setText(users.get(0).getTeacherName() + "等" + users.size() + "人");
        }else {
            personCTV.setText(users.get(0).getTeacherName());
        }
        for(UserNode user:users){
            personCSelId = personCSelId + user.getTeacherId() + ",";
        }
        try {
            UserNode user = UserInfo.getInstance().getUserByTeacherId(users.get(0).getTeacherId());
            AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
            Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personCIV, AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                    imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            });
            if(bitmap!=null){
                personCIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        personCTV.setVisibility(View.VISIBLE);
        personCIV.setVisibility(View.VISIBLE);
    }

    /**
     * 任务标题
     */
    private View.OnClickListener titleFLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("Task", "title tv click");
            ContentEntryDialog ceDialog = new ContentEntryDialog(HomeTaskVerifyEditActivity.this,  infoUpdate);
            ceDialog.showView(view, titleTV.getText().toString(), "任务标题");
        }

        private BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;
                titleTV.setText((String)object);
            }
        };
    };

    /**
     * 任务描述
     */
    private View.OnClickListener descriptionFLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("Task", "title tv click");
            ContentEntryDialog ceDialog = new ContentEntryDialog(HomeTaskVerifyEditActivity.this,  infoUpdate);
            ceDialog.showView(view, descriptionTV.getText().toString(), "任务描述");
        }

        private BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;
                descriptionTV.setText((String)object);
            }
        };
    };

    private void setPersonInfo(String personId){
        if(taskNode.isForAllTeacher()) {
            personSelId = "1";
            personTV.setText("所有人");
            personTV.setVisibility(View.VISIBLE);
            personIV.setVisibility(View.VISIBLE);
            return;
        }
        personSelId = personId;
        if(TextUtils.isEmpty(personId)){
            personTV.setVisibility(View.GONE);
            personIV.setVisibility(View.GONE);
            return;
        }
        UserNode user = UserInfo.getInstance().getUserByTeacherId(personId);
        if(user==null){
            personTV.setVisibility(View.GONE);
            personIV.setVisibility(View.GONE);
            return;
        }
        personTV.setText(user.getTeacherName());
        try {
            AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
//            asyncBitmapLoader.showPicByVolleyRequest(HomeTaskVerifyEditActivity.this, URLData.getUrlFile(UserInfo.getInstance().getUser().getHeadImgUrl()), personIV);
            Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personIV, AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                    imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            });
            if(bitmap!=null){
                personIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        personTV.setVisibility(View.VISIBLE);
        personIV.setVisibility(View.VISIBLE);
    }

    /**
     * 处理人
     */
    private View.OnClickListener personSelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PersonSelDialog perSelDialog = null;
//            if(taskNode.getTaskCreateType()==1){
//                perSelDialog = new PersonSelDialog(HomeTaskVerifyEditActivity.this, personSelId, false, true, personsInfoUpdate);
//            }else {
//                perSelDialog = new PersonSelDialog(HomeTaskVerifyEditActivity.this, personSelId, personsInfoUpdate);
//            }
            perSelDialog = new PersonSelDialog(HomeTaskVerifyEditActivity.this, personSelId, false, true, personsInfoUpdate);
            perSelDialog.showView(view);
        }

        private BaseInfoUpdate personsInfoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
//                if(object!=null){
//                    personSelId = (String)object;
//                    UserNode user = UserInfo.getInstance().getUserByTeacherId(personSelId);
//                    personTV.setText(user.getTeacherName());
//                    personIV.setImageBitmap(null);
//                    personIV.setBackgroundResource(R.mipmap.user_icon);
//                    try {
//                        AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
//                        Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personIV, AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
//                            @Override
//                            public void imageLoad(ImageView imageView, Bitmap bitmap) {
//                                Log.i("HuiZhi", "The pic:" + bitmap);
//                                imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
//                            }
//                        });
//                        Log.i("HuiZhi", "The 1 pic:" + bitmap);
//                        if(bitmap!=null){
//                            personIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
//                        }
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                    personTV.setVisibility(View.VISIBLE);
//                    personIV.setVisibility(View.VISIBLE);
//                }else{
//                    personSelId = null;
//                    personTV.setVisibility(View.GONE);
//                    personIV.setVisibility(View.GONE);
//                }

                if(object!=null){
                    personSelId = (String)object;
                    personIV.setImageBitmap(null);
                    personIV.setBackgroundResource(R.mipmap.user_icon);
                    if("1".equals(personSelId)){
                        Log.i("HuiZhi", "Come into person select all");
                        personTV.setText("所有人");
                        personCCLL.setVisibility(View.GONE);
                    }else {
                        UserNode user = UserInfo.getInstance().getUserByTeacherId(personSelId);
                        personTV.setText(user.getTeacherName());

                        try {
                            AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
                            Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personIV, AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                                @Override
                                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                                    imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                                }
                            });
                            if(bitmap!=null){
                                personIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
//                        if(taskNode.getTaskCreateType()==1)
                        personCCLL.setVisibility(View.VISIBLE);
                    }

                    personTV.setVisibility(View.VISIBLE);
                    personIV.setVisibility(View.VISIBLE);
                }else{
                    personSelId = null;
                    personTV.setVisibility(View.GONE);
                    personIV.setVisibility(View.GONE);
                }
            }
        };
    };

    /**
     * 抄送人
     */
    private View.OnClickListener personCSelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PersonMultSelDialog multSelDialog = new PersonMultSelDialog(HomeTaskVerifyEditActivity.this, personCSelId, infoUpdate);
            multSelDialog.showView(view);
        }

        private BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    personCSelId = "";
                else
                    personCSelId = (String)object;
                Log.i("HuiZhi", "Come into personCSelId:" + personCSelId);
                if(TextUtils.isEmpty(personCSelId)){
                    personCTV.setVisibility(View.GONE);
                    personCIV.setVisibility(View.GONE);
                }else {
                    personCIV.setImageBitmap(null);
                    personCIV.setBackgroundResource(R.mipmap.user_icon);
                    String[] perids = personCSelId.split(",");
                    UserNode user = UserInfo.getInstance().getUserByTeacherId(perids[0]);
                    if(perids.length>1){
                        personCTV.setText(user.getTeacherName() + "等" + perids.length + "人");
                    }else {
                        personCTV.setText(user.getTeacherName());
                    }

                    try {
                        AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
                        Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personCIV, AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                            @Override
                            public void imageLoad(ImageView imageView, Bitmap bitmap) {
                                imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                            }
                        });
                        if(bitmap!=null){
                            personCIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    personCTV.setVisibility(View.VISIBLE);
                    personCIV.setVisibility(View.VISIBLE);
                }

            }
        };
    };

    private SwitchView.OnStateChangedListener timeSwitchChange = new SwitchView.OnStateChangedListener() {
        @Override
        public void toggleToOn() {
            switchBtn.toggleSwitch(true);
            limtTimeTV.setText("");
            isLimitTime = true;
            limtTimeFL.setVisibility(View.VISIBLE);
        }

        @Override
        public void toggleToOff() {
            switchBtn.toggleSwitch(false);
            limtTimeFL.setVisibility(View.GONE);
            limtTimeTV.setText("");
            isLimitTime = false;
        }
    };

    private View.OnClickListener timeLimitClick = new View.OnClickListener() {
        private CustomDatePicker customDatePicker, customDatePicker1;

        @Override
        public void onClick(View view) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            String now = sdf.format(new Date());
            initDatePicker();
            customDatePicker.show(now);
        }

        private void initDatePicker() {
            customDatePicker = new CustomDatePicker(HomeTaskVerifyEditActivity.this, new CustomDatePicker.ResultHandler() {
                @Override
                public void handle(String time) { // 回调接口，获得选中的时间
                    limtTimeTV.setText(time);
                }
            }, "2017-01-01 00:00", "2037-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
            customDatePicker.showSpecificTime(true); // 显示时和分
            customDatePicker.setIsLoop(true); // 允许循环滚动
        }
    };

    private View.OnClickListener addSubTaskClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(HomeTaskVerifyEditActivity.this, HomeTaskCustomSubFoundActivity.class);
            intent.putExtra("ParentId", taskId);
            startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    };

    private View.OnClickListener submitBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String createTechId = UserInfo.getInstance().getUser().getTeacherId();
            String title = titleTV.getText().toString();
            if(TextUtils.isEmpty(title)){
                Toast.makeText(HomeTaskVerifyEditActivity.this, "任务标题不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            String description = descriptionTV.getText().toString();
            if(TextUtils.isEmpty(description)){
                Toast.makeText(HomeTaskVerifyEditActivity.this, "任务描述不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if(TextUtils.isEmpty(personSelId)){
                Toast.makeText(HomeTaskVerifyEditActivity.this, "请选择处理人", Toast.LENGTH_LONG).show();
                return;
            }
            int isTimeLimit = 0;
            String planEndTime = "";
            if(isLimitTime) {
                isTimeLimit = 1;
                planEndTime = limtTimeTV.getText().toString();
            };

            String assignReason = "";
            taskNode.setTaskTitle(title);
            taskNode.setTaskDescription(description);
            taskNode.setTimeLimit(isLimitTime);
            taskNode.setStrPlanEndTime(planEndTime);
            taskNode.setPriority(priority);
            if("1".equals(personSelId)){
                taskNode.setAtAll(personSelId);
                taskNode.setProcessingTeacherId("");
            }else {
                taskNode.setAtAll("0");
                taskNode.setProcessingTeacherId(personSelId);
            }
//            taskNode.setProcessingTeacherId(personSelId);
            taskNode.setCcTeacherId(personCSelId);
            List<TaskNode> items = new ArrayList<>();
            items.add(taskNode);
            items.addAll(subTasks);
            HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
            postRequest.postAdminTaskEdit(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), getTasksJS(items), handler);
        }

        private String getTasksJS(List<TaskNode> nodes){
            JSONArray jsonAr = new JSONArray();
            JSONObject jsonOb = null;
            try{
                for (TaskNode node:nodes){
                    jsonOb = new JSONObject();
                    jsonOb.put("TaskId", node.getTaskId());
                    jsonOb.put("ParentTaskId", node.getParentId());
                    jsonOb.put("TaskTitle", node.getTaskTitle());
                    jsonOb.put("TaskDescription", node.getTaskDescription());
                    if(node.isTimeLimit())
                        jsonOb.put("IsTimeLimit", 1);
                    else
                        jsonOb.put("IsTimeLimit", 0);
                    jsonOb.put("PlanEndTime", node.getStrPlanEndTime());
                    jsonOb.put("Priority", node.getPriority());
                    jsonOb.put("ProcessingTeacherId", node.getProcessingTeacherId());
                    jsonOb.put("CCTeacherId", node.getCcTeacherId());
                    jsonOb.put("AssignReason", "");
                    jsonOb.put("AtAll", node.getAtAll());
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
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj==null)
                        return;
                    Toast.makeText(HomeTaskVerifyEditActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.REQUEST_CODE && resultCode==Constants.RESULT_CODE){
            Log.i("Task", "come into task custom result");
            TaskNode taskNode = (TaskNode)data.getSerializableExtra("Item");
            if(taskNode!=null){
                Log.i("Task", "The task title is:" + taskNode.getTaskTitle());
                updateTaskNode(taskNode);
                addSubTaskViews();
            }
        }

    }

    private void updateTaskNode(TaskNode node){
        if(node==null)
            return;
        String uniqueId = node.getTaskId();
        if(TextUtils.isEmpty(uniqueId))
            return;
        for(TaskNode itemN:subTasks){
            if(uniqueId.equals(itemN.getTaskId())){
                int index = subTasks.indexOf(itemN);
                subTasks.remove(itemN);
                subTasks.add(index, node);
                return;
            }
        }
        subTasks.add(node);
    }

    private void addSubTaskViews(){
        Log.i("Task", "The sub task size is:" + subTasks.size());
        //subTaskLL
        subTaskLL.removeAllViews();
        for(TaskNode node:subTasks){
            SubTaskItemView subTaskV = new SubTaskItemView(this);
            Log.i("HuiZhi", "The task node taksid:" +taskId);
            subTaskV.setDates(node, taskId);
            subTaskLL.addView(subTaskV);
        }
    }
}
