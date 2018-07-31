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
import com.huizhi.manage.dialog.PersonSelDialog;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.PictureUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by CL on 2018/1/9.
 * 创建任务
 */

public class HomeTaskMouldSubFoundActivity extends Activity {
    private Button priHBtn, priMbtn, priLBtn;
    private int priority = 3;
    private TaskMouldNode taskNode;
    private FrameLayout titleFL, descriptionFL;
    private TextView titleTV, descriptionTV, personTV;
    private ImageView personIV;
    private String personSelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_mould_sub_found);
        getIntentDate();
        initViews();
    }

    private void getIntentDate(){
        taskNode = (TaskMouldNode)getIntent().getSerializableExtra("Item");
    }

    private void initViews() {
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
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

        LinearLayout limiTimeLL = findViewById(R.id.timelimit_ll);
        limiTimeLL.setVisibility(View.GONE);

        if(taskNode!=null){
            titleTV.setText(taskNode.getTaskTitle());
            descriptionTV.setText(taskNode.getTaskDescription());
            priorityBtnSel(taskNode.getPriority());
            setPersonInfo(taskNode.getExecuteTeacherId());
        }
        Button sureBtn = findViewById(R.id.sure_btn);
        sureBtn.setOnClickListener(sureBtnClick);
    }

    private View.OnClickListener titleFLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("Task", "title tv click");
            ContentEntryDialog ceDialog = new ContentEntryDialog(HomeTaskMouldSubFoundActivity.this,  infoUpdate);
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

    private View.OnClickListener descriptionFLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("Task", "title tv click");
            ContentEntryDialog ceDialog = new ContentEntryDialog(HomeTaskMouldSubFoundActivity.this,  infoUpdate);
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

    /**
     * 处理人
     */
    private View.OnClickListener personSelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PersonSelDialog perSelDialog = new PersonSelDialog(HomeTaskMouldSubFoundActivity.this, personSelId, personsInfoUpdate);
            perSelDialog.showView(view);
        }

        private BaseInfoUpdate personsInfoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object!=null){
                    setPersonInfo((String)object);
                }else{
                    setPersonInfo(null);
                }
            }
        };
    };

    private void setPersonInfo(String personId){
        personSelId = personId;
        if(TextUtils.isEmpty(personId)){
            personTV.setVisibility(View.GONE);
            personIV.setVisibility(View.GONE);
            return;
        }
        UserNode user = UserInfo.getInstance().getTaskUserByTeacherId(personId);
        if(user==null)
            return;
        personTV.setText(user.getTeacherName());
        personIV.setImageBitmap(null);
        personIV.setBackgroundResource(R.mipmap.user_icon);
        try {
            AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
//            asyncBitmapLoader.showPicByVolleyRequest(HomeTaskMouldSubFoundActivity.this, URLData.getUrlFile(UserInfo.getInstance().getUser().getHeadImgUrl()), personIV);
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

    private View.OnClickListener sureBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String title = titleTV.getText().toString();
            if(TextUtils.isEmpty(title)){
                Toast.makeText(HomeTaskMouldSubFoundActivity.this, "任务标题不能为空！", Toast.LENGTH_LONG).show();
                return;
            }
            String description = descriptionTV.getText().toString();
//            if(TextUtils.isEmpty(description)){
//                Toast.makeText(HomeTaskMouldSubFoundActivity.this, "任务描述不能为空！", Toast.LENGTH_LONG).show();
//                return;
//            }

            if(TextUtils.isEmpty(personSelId)){
                Toast.makeText(HomeTaskMouldSubFoundActivity.this, "处理人不能为空！", Toast.LENGTH_LONG).show();
                return;
            }

            if(taskNode==null)
                taskNode = new TaskMouldNode();
            taskNode.setTaskTitle(title);
            taskNode.setTaskDescription(description);
            taskNode.setExecuteTeacherId(personSelId);
            taskNode.setTeacherId(UserInfo.getInstance().getUser().getTeacherId());
            taskNode.setPriority(priority);
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            taskNode.setStrCreateTime(date);
            if(TextUtils.isEmpty(taskNode.getUniqueId())) {
                String uniqueId = String.valueOf(new Date().getTime());
                Log.i("Task", "The unique id is:" + uniqueId);
                taskNode.setUniqueId(uniqueId);
            }
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("Item", taskNode);
            intent.putExtras(bundle);
            setResult(Constants.RESULT_CODE, intent);
            finish();
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(HomeTaskMouldSubFoundActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                    break;
                case Constants.MSG_FAILURE:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(HomeTaskMouldSubFoundActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
}
