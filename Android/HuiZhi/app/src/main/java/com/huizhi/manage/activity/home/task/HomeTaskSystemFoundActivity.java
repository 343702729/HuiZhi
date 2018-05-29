package com.huizhi.manage.activity.home.task;

import android.app.Activity;
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
import com.huizhi.manage.dialog.LoadingProgress;
import com.huizhi.manage.dialog.PersonSelDialog;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.TaskSystemNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.request.home.HomeTaskPostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.PictureUtil;
import com.huizhi.manage.wiget.SwitchView;
import com.huizhi.manage.wiget.datepicker.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by CL on 2017/12/20.
 * 创建系统任务
 */

public class HomeTaskSystemFoundActivity extends Activity {
    private TaskSystemNode taskSystemNode;
    private Button priHBtn, priMbtn, priLBtn;
    private int priority = 3;
    private boolean isLimitTime = false;
    private FrameLayout limtTimeFL;
    private SwitchView switchBtn;
    private TextView limtTimeTV, personTV;
    private ImageView personIV;
    private String personSelId;
    private LoadingProgress loadingProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_system_found);
        getIntentDate();
        initViews();
    }

    private void getIntentDate(){
        taskSystemNode = (TaskSystemNode)getIntent().getSerializableExtra("Item");
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));
        TextView titleTV = findViewById(R.id.title_tv);
        if(taskSystemNode!=null)
            titleTV.setText(taskSystemNode.getTaskTitle());

        LinearLayout perSelLL = findViewById(R.id.person_sel_ll);
        perSelLL.setOnClickListener(personSelClick);
        personTV = findViewById(R.id.person_tv);
        personIV = findViewById(R.id.person_iv);

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

        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(submitBtnClick);
    }

    private View.OnClickListener personSelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("Task", "Come into person select");
            PersonSelDialog perSelDialog = new PersonSelDialog(HomeTaskSystemFoundActivity.this, personSelId, personsInfoUpdate);
            perSelDialog.showView(view);
        }

        private BaseInfoUpdate personsInfoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object!=null){
                    personSelId = (String)object;
                    UserNode user = UserInfo.getInstance().getUserByTeacherId(personSelId);
                    personTV.setText(user.getTeacherName());
                    personIV.setImageBitmap(null);
                    personIV.setBackgroundResource(R.mipmap.user_icon);
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
            customDatePicker = new CustomDatePicker(HomeTaskSystemFoundActivity.this, new CustomDatePicker.ResultHandler() {
                @Override
                public void handle(String time) { // 回调接口，获得选中的时间
                    limtTimeTV.setText(time);
                }
            }, "2017-01-01 00:00", "2037-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
            customDatePicker.showSpecificTime(true); // 显示时和分
            customDatePicker.setIsLoop(true); // 允许循环滚动
        }
    };

    private View.OnClickListener submitBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String sysTaskId = taskSystemNode.getTaskId();
            String createTechId = UserInfo.getInstance().getUser().getTeacherId();
            if(TextUtils.isEmpty(personSelId)){
                Toast.makeText(HomeTaskSystemFoundActivity.this, "请选择处理人", Toast.LENGTH_LONG).show();
                return;
            }
            int isTimeLimit = 0;
            String planEndTime = "";
            if(isLimitTime) {
                isTimeLimit = 1;
                planEndTime = limtTimeTV.getText().toString();
            };
            int priorty = priority;
            String assignReason = "";
            loadingProgress = new LoadingProgress(HomeTaskSystemFoundActivity.this, null);
            loadingProgress.showView(view);
            HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
            postRequest.postSystemTask(sysTaskId, createTechId, UserInfo.getInstance().getUser().getSchoolId(), isTimeLimit, planEndTime, priorty, personSelId, assignReason, handler);
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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(loadingProgress!=null)
                loadingProgress.closeView();
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(HomeTaskSystemFoundActivity.this, message, Toast.LENGTH_LONG).show();
                        if(UserInfo.getInstance().getTaskCreatInfo()!=null){
                            UserInfo.getInstance().getTaskCreatInfo().update(true);
                        }
                        finish();;
                    }
                    break;
                case Constants.MSG_FAILURE:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(HomeTaskSystemFoundActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
}
