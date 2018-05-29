package com.huizhi.manage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.user.UserHeadPortraitEdit;
import com.huizhi.manage.activity.user.UserHuiZhiAboutActivity;
import com.huizhi.manage.activity.user.UserPasswordEditActivity;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.JudgeDialog;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.login.LoginActivity;
import com.huizhi.manage.util.PictureUtil;
import com.huizhi.manage.util.SharedPrefsUtil;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;

/**
 * Created by CL on 2017/12/13.
 */

public class UserFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private ImageView headIV;
    private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_user, container, false);
        activity = this.getActivity();
        initViews();
        setHeadPortrait();
        return messageLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeadPortrait();
    }

    private void initViews(){
        headIV = messageLayout.findViewById(R.id.user_iv);
        TextView nameTV = (TextView)messageLayout.findViewById(R.id.name_tv);
        nameTV.setText(UserInfo.getInstance().getUser().getTeacherName());
        TextView roleTV = messageLayout.findViewById(R.id.role_tv);
        roleTV.setText(UserInfo.getInstance().getUser().getRoleTypeName());

        //修改头像
        LinearLayout txLL = messageLayout.findViewById(R.id.user_tx_ll);
        txLL.setOnClickListener(itemClick);
        //修改密码
        LinearLayout mmLL = messageLayout.findViewById(R.id.user_xx_ll);
        mmLL.setOnClickListener(itemClick);
        //关于绘智
        LinearLayout gyLL = messageLayout.findViewById(R.id.user_gy_ll);
        gyLL.setOnClickListener(itemClick);

        Button logoutBtn = messageLayout.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(logoutBtnClick);

    }

    private View.OnClickListener itemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
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

    /**
     * 用户头像加载
     */
    private void setHeadPortrait(){
        String headImg = AsyncFileUpload.getInstance().getFileUrl(UserInfo.getInstance().getUser().getHeadImgUrl());
        try {
            Bitmap bitmap = asyncBitmapLoader.loadBitmap(headIV, headImg, new AsyncBitmapLoader.ImageCallBack() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                    imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            });
            if(bitmap!=null){
                headIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

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

}
