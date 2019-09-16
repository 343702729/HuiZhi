package com.huizhi.manage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.HomeEmailInfoActivity;
import com.huizhi.manage.activity.home.task.HomeTaskAgencyActivity;
import com.huizhi.manage.activity.home.task.HomeTaskAllocationActivity;
import com.huizhi.manage.activity.user.UserHeadPortraitEdit;
import com.huizhi.manage.activity.user.UserHuiZhiAboutActivity;
import com.huizhi.manage.activity.user.UserPasswordEditActivity;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.JudgeDialog;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.login.LoginActivity;
import com.huizhi.manage.node.EmailInfoNode;
import com.huizhi.manage.request.home.HomeUserGetRequest;
import com.huizhi.manage.util.SharedPrefsUtil;
import com.huizhi.manage.util.TLog;
import com.huizhi.manage.wiget.GlideCircleTransform;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

public class NewUserFragment extends Fragment {
    private View messageLayout;
    private Activity activity;

    private EmailInfoNode emailInfoNode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_new_user, container, false);
        activity = getActivity();
        initViews();
        getDates();
        return messageLayout;
    }

    private void initViews(){
        LinearLayout dbLL = messageLayout.findViewById(R.id.user_db_ll);
        dbLL.setOnClickListener(itemOnClick);
        LinearLayout fpLL = messageLayout.findViewById(R.id.user_fp_ll);
        fpLL.setOnClickListener(itemOnClick);
        LinearLayout yjLL = messageLayout.findViewById(R.id.user_yj_ll);
        yjLL.setOnClickListener(itemOnClick);

        LinearLayout txLL = messageLayout.findViewById(R.id.user_tx_ll);
        txLL.setOnClickListener(itemOnClick);
        LinearLayout mmLL = messageLayout.findViewById(R.id.user_xx_ll);
        mmLL.setOnClickListener(itemOnClick);
        LinearLayout gyLL = messageLayout.findViewById(R.id.user_gy_ll);
        gyLL.setOnClickListener(itemOnClick);

        TextView nameTV = messageLayout.findViewById(R.id.name_tv);
        nameTV.setText(UserInfo.getInstance().getUser().getTeacherName());
        TextView roleTV = messageLayout.findViewById(R.id.role_tv);
        roleTV.setText(UserInfo.getInstance().getUser().getRoleTypeName());
        TextView schoolTV = messageLayout.findViewById(R.id.school_tv);
        schoolTV.setText(UserInfo.getInstance().getUser().getSchoolName());

        TextView logoutBtn = messageLayout.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(logoutBtnClick);

        String headImg = AsyncFileUpload.getInstance().getFileUrl(UserInfo.getInstance().getUser().getHeadImgUrl());
        TLog.log("The headimg is:" + headImg);
        ImageView headIV = messageLayout.findViewById(R.id.user_iv);
        Glide.with(activity).load(headImg)
                //圆形
                .transform(new GlideCircleTransform(activity))
                .into(headIV);
    }

    private void getDates() {
        HomeUserGetRequest getRequest = new HomeUserGetRequest();
        getRequest.getEmailInfo(UserInfo.getInstance().getUser().getEmail(), handler);
    }

    private View.OnClickListener itemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.user_db_ll://代办任务
                    intent = new Intent();
                    intent.setClass(activity, HomeTaskAgencyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_fp_ll://任务分配
                    intent = new Intent();
                    intent.setClass(activity, HomeTaskAllocationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_yj_ll://未读邮件
                    intent = new Intent();
                    intent.setClass(activity, HomeEmailInfoActivity.class);
                    if(emailInfoNode!=null)
                        intent.putExtra("ItemUrl", emailInfoNode.getMailboxUrl());
                    startActivity(intent);
                    break;
                case R.id.user_tx_ll://修改头像
                    intent = new Intent();
                    intent.setClass(activity, UserHeadPortraitEdit.class);
                    startActivity(intent);
                    break;
                case R.id.user_xx_ll:
                    intent = new Intent();
                    intent.setClass(activity, UserPasswordEditActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_gy_ll:
                    intent = new Intent();
                    intent.setClass(activity, UserHuiZhiAboutActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    private View.OnClickListener logoutBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JudgeDialog judgeDialog = new JudgeDialog(activity, infoUpdate);
            judgeDialog.showView(view, "是否确认退出应用");
        }

        private BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                SharedPrefsUtil.putValue(activity, "Account", "");
                SharedPrefsUtil.putValue(activity, "Password", "");
                UserInfo.getInstance().setLogin(false);
                RongIM.getInstance().logout();
                JPushInterface.stopPush(activity);
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
                Intent intent = new Intent();
                intent.setClass(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
//            System.exit(0);
            }
        };
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS_THREE:
                    if(msg.obj==null)
                        return;
                    emailInfoNode = (EmailInfoNode)msg.obj;
//                    setEmailInfo(emailInfoNode);
                    break;
            }
        }
    };
}
