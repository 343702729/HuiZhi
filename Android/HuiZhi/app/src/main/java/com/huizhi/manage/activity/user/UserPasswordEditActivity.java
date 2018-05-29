package com.huizhi.manage.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.request.user.UserPostRequest;
import com.huizhi.manage.util.AppUtil;

/**
 * Created by CL on 2018/2/6.
 * 密码修改
 */

public class UserPasswordEditActivity extends Activity {
    private EditText oldPasswordET, newPasswordET, surePasswordET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_password_edit);
        initViews();
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        oldPasswordET = findViewById(R.id.password_old_et);
        newPasswordET = findViewById(R.id.password_new_et);
        surePasswordET = findViewById(R.id.password_sure_et);

        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(submitBtnClick);
    }

    private View.OnClickListener submitBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String oldPass = oldPasswordET.getText().toString();
            if(TextUtils.isEmpty(oldPass)){
                Toast.makeText(UserPasswordEditActivity.this, "原密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String newPass = newPasswordET.getText().toString();
            if(TextUtils.isEmpty(newPass)){
                Toast.makeText(UserPasswordEditActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            String surePass = surePasswordET.getText().toString();
            if(TextUtils.isEmpty(surePass)){
                Toast.makeText(UserPasswordEditActivity.this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!newPass.equals(surePass)){
                Toast.makeText(UserPasswordEditActivity.this, "两次密码不一样", Toast.LENGTH_SHORT).show();
                return;
            }
            UserPostRequest postRequest = new UserPostRequest();
            postRequest.postUserPasswordEdit(UserInfo.getInstance().getUser().getTeacherId(), oldPass, newPass, handler);
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
                    String message = (String)msg.obj;
                    Toast.makeText(UserPasswordEditActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                break;
            }
        }
    };
}
