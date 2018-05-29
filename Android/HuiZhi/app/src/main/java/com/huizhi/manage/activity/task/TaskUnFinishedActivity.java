package com.huizhi.manage.activity.task;

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
import android.widget.EditText;
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
import com.huizhi.manage.dialog.PersonSelDialog;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.request.task.TaskPostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.PictureUtil;

/**
 * Created by CL on 2017/12/16.
 */

public class TaskUnFinishedActivity extends Activity {
    private TaskNode taskNode;
    private EditText reasonET;
    private TextView personTV;
    private ImageView personIV;
    private String personSelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_unfinished);
        initDates();
        initViews();
    }

    private void initDates(){
        taskNode = (TaskNode)getIntent().getSerializableExtra("Item");
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));
        if(taskNode==null)
            return;
        TextView titleTV = findViewById(R.id.title_tv);
        titleTV.setText(taskNode.getTaskTitle());
        TextView createTTV = findViewById(R.id.createtime_tv);
        createTTV.setText(taskNode.getStrCreateTime());
        TextView descriptionTV = findViewById(R.id.description_tv);

        personTV = findViewById(R.id.person_tv);
        personIV = findViewById(R.id.person_iv);
        UserNode adminNode = UserInfo.getInstance().getUserByTeacherId(taskNode.getCreateTeacherId());
        personSelId = taskNode.getCreateTeacherId();
        LinearLayout personSelLL = findViewById(R.id.person_sel_ll);
        personSelLL.setOnClickListener(personSelClick);
        if(adminNode!=null){
            personTV.setText(adminNode.getTeacherName());
            AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
            try{
//                asyncBitmapLoader.showPicByVolleyRequest(TaskUnFinishedActivity.this, AsyncFileUpload.getInstance().getFileUrl(adminNode.getHeadImgUrl()), personIV);
                Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personIV, AsyncFileUpload.getInstance().getFileUrl(adminNode.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                    @Override
                    public void imageLoad(ImageView imageView, Bitmap bitmap) {
                        imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                    }
                });
                if(bitmap!=null){
                    personIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            }catch (Exception e){

            }
        }
        descriptionTV.setText(taskNode.getTaskDescription());
        reasonET = findViewById(R.id.reason_et);
        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(submitBtnClick);
    }

    /**
     * 审核人
     */
    private View.OnClickListener personSelClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PersonSelDialog perSelDialog = new PersonSelDialog(TaskUnFinishedActivity.this, personSelId, true, personsInfoUpdate);
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
//                        asyncBitmapLoader.showPicByVolleyRequest(HomeTaskVerifyEditActivity.this, URLData.getUrlFile(UserInfo.getInstance().getUser().getHeadImgUrl()), personIV);
                        Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personIV, AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                            @Override
                            public void imageLoad(ImageView imageView, Bitmap bitmap) {
//                                imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                                Log.i("HuiZhi", "The pic:" + bitmap);
                                imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                            }
                        });
                        Log.i("HuiZhi", "The 1 pic:" + bitmap);
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

    private View.OnClickListener submitBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String taskId = taskNode.getTaskId();
            String userId = UserInfo.getInstance().getUser().getTeacherId();
            int resultType = 1;
            String assignReason = reasonET.getText().toString();
            if(TextUtils.isEmpty(assignReason)){
                Toast.makeText(TaskUnFinishedActivity.this, "备注信息不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            TaskPostRequest postRequest = new TaskPostRequest();
            postRequest.postTaskUnFinish(taskId, userId, resultType, personSelId, assignReason, null, handler);
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
                        Toast.makeText(TaskUnFinishedActivity.this, message, Toast.LENGTH_LONG).show();
                        finish();
                    }
                    break;
                case Constants.MSG_FAILURE:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(TaskUnFinishedActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    };
}
