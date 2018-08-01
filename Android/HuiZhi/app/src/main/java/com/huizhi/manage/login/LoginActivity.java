package com.huizhi.manage.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.HomeMessageInfoActivity;
import com.huizhi.manage.activity.home.HomeNewsInfoActivity;
import com.huizhi.manage.activity.home.course.CourseInfoActivity;
import com.huizhi.manage.activity.home.task.HomeTaskAgencyActivity;
import com.huizhi.manage.activity.home.task.HomeTaskVerifyActivity;
import com.huizhi.manage.activity.task.TaskDetailActivity;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.main.MainActivity;
import com.huizhi.manage.main.MainApplication;
import com.huizhi.manage.request.common.FileGetRequest;
import com.huizhi.manage.request.login.LoginRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.SharedPrefsUtil;
import com.jaeger.library.StatusBarUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by CL on 2017/12/13.
 * 用户登录
 */

public class LoginActivity extends Activity {
    private EditText accountET, passwordET;
    private boolean isMessage = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initDates();
        initViews();
        autoLogin();
        getUploadToken();
//        initJPush();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initDates(){
        try{
            isMessage = getIntent().getBooleanExtra("IsMessage", false);
        }catch (Exception e){

        }

    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        accountET = (EditText)findViewById(R.id.account_et);
        passwordET = (EditText)findViewById(R.id.password_et);
        Button loginBtn = (Button)findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(loginBtnClick);
    }

    private void getUploadToken(){
        FileGetRequest getRequest = new FileGetRequest();
        getRequest.getFileUploadToken(null);
    }

    private void autoLogin(){
        String account = SharedPrefsUtil.getValue(this,"Account", "");
        String password = SharedPrefsUtil.getValue(this,"Password", "");
        if(TextUtils.isEmpty(account)||TextUtils.isEmpty(password))
            return;
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.loginRequest(account, password, UserInfo.getInstance().getPhoneType(), UserInfo.getInstance().getRegistrationId(), handler);
    }

    private View.OnClickListener loginBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String account = accountET.getText().toString();
            String password = passwordET.getText().toString();

            SharedPrefsUtil.putValue(LoginActivity.this, "Account", account);
            SharedPrefsUtil.putValue(LoginActivity.this, "Password", password);
//            account = "wang";
//            password = "000000";
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.loginRequest(account, password, UserInfo.getInstance().getPhoneType(), UserInfo.getInstance().getRegistrationId(), handler);//UserInfo.getInstance().getRegistrationId()
        }
    };



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
//                    Toast.makeText(LoginActivity.this, UserInfo.getInstance().getUser().getAlias(), Toast.LENGTH_LONG).show();
                    JPushInterface.setAlias(getApplicationContext(), UserInfo.getInstance().getUser().getAlias(), mAliasCallback);
                    break;
                case 2:

                    break;
                case Constants.MSG_SUCCESS:
//                    UserInfo.getInstance().setChatToken("123321123");
                    if(0==UserInfo.getInstance().getUser().getJobStatus()) {
                        ((MainApplication)getApplication()).setRongInit(UserInfo.getInstance().getUser().getAppKey());
                        initJPush();
                        UserInfo.getInstance().setLogin(true);
                        JPushInterface.setAlias(getApplicationContext(), UserInfo.getInstance().getUser().getAlias(), mAliasCallback);
//                        Toast.makeText(LoginActivity.this, UserInfo.getInstance().getUser().getAlias(), Toast.LENGTH_LONG).show();
//                        JPushInterface.setAlias(getApplicationContext(), 0, UserInfo.getInstance().getUser().getAlias());

                        goActivity();
                    }else {
                        Toast.makeText(LoginActivity.this, "您的账号异常，无法为您登录", Toast.LENGTH_LONG).show();
                    }
                    break;
                case Constants.MSG_FAILURE:
                    String message = (String)msg.obj;
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String s, Set<String> set) {
            switch (code){
                case 6002:
                    handler.sendMessageDelayed(handler.obtainMessage(1), 1000 * 6);
                    break;
            }

        }
    };



    private void initJPush(){
        try{
            if (JPushInterface.isPushStopped(this))
                JPushInterface.resumePush(this);
            else
                JPushInterface.init(this);
//            UserInfo.getInstance().setRegistrationId(JPushInterface.getRegistrationID(getApplicationContext()));
//            Log.i("HuiZhi", "The jpush registration id:" + UserInfo.getInstance().getRegistrationId());
//            JPushInterface.init(getApplicationContext());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void goActivity(){
        Log.i("HuiZhi", "Come into detail go:" + isMessage);
        if(!isMessage){
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            int type = getIntent().getIntExtra("Type", 0);
            String date = getIntent().getStringExtra("Date");
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.i("HuiZhi", "Come into detail type:"+ type);
            if(31==type||32==type||33==type||42==type){
                Log.i("HuiZhi", "Come into detail go");
                Intent intent = new Intent(this, TaskDetailActivity.class);
                intent.putExtra("TaskId", date);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                Intent[] intents = {mainIntent, intent};
                startActivities(intents);
                finish();
            }else if(41==type){
                Intent intent = new Intent(this, HomeTaskVerifyActivity.class);
                intent.putExtra("TaskId", date);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                Intent[] intents = {mainIntent, intent};
                startActivities(intents);
                finish();
            }else if(2==type){
                Intent intent = new Intent(this, HomeNewsInfoActivity.class);
                intent.putExtra("Id", date);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                Intent[] intents = {mainIntent, intent};
                startActivities(intents);
                finish();
            }else if(1==type){
                Intent intent = new Intent(this, HomeMessageInfoActivity.class);
                intent.putExtra("Id", date);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                Intent[] intents = {mainIntent, intent};
                startActivities(intents);
                finish();
            }else if(51==type){
                Intent intent = new Intent(this, HomeTaskAgencyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                Intent[] intents = {mainIntent, intent};
                startActivities(intents);
                finish();
            }else if(60==type){
                Intent intent = new Intent();
                intent.setClass(this, CourseInfoActivity.class);
                intent.putExtra("LessonNum", date);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                Intent[] intents = {mainIntent, intent};
                startActivities(intents);
                finish();
            }else if(Constants.MSG_RECEIVE == type){
                Log.i("HuiZhi", "Come into message go:" + date);
                mainIntent.putExtra("Index", 3);
                mainIntent.putExtra("Date", date);
                mainIntent.putExtra("IsChat", true);
                startActivity(mainIntent);
                finish();
            }
        }
    }
}
