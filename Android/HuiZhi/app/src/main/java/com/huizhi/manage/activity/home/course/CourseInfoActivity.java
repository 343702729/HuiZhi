package com.huizhi.manage.activity.home.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.ViewPagerAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.CourseNode;
import com.huizhi.manage.request.home.HomeCourseGetRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.course.SignMode;
import com.huizhi.manage.wiget.course.StandardMode;

import java.util.ArrayList;
import java.util.List;

public class CourseInfoActivity extends Activity {
    private String lessonNum;
    private ViewPager viewPager;
    private LinearLayout signSelLL;
    private TextView standardTV, signTV;
    private StandardMode standardMode;
    private SignMode signMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_info);
        initDatas();
        initViews();
        getDatas();
    }

    private void initDatas(){
        lessonNum = getIntent().getStringExtra("LessonNum");
    }

    private void initViews() {
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));
        ImageButton settingIB = findViewById(R.id.setting_ib);
        settingIB.setOnClickListener(settingBtnClick);

        signSelLL = findViewById(R.id.sign_type_ll);

        standardTV = findViewById(R.id.standard_tv);
        signTV = findViewById(R.id.sign_tv);
        standardTV.setOnClickListener(btnClick);
        signTV.setOnClickListener(btnClick);

        initPageView();
    }

    private void getDatas(){
        HomeCourseGetRequest getRequest = new HomeCourseGetRequest();

        getRequest.getCourseInfo(UserInfo.getInstance().getUser().getTeacherName(), lessonNum, handler);
    }

    private void initPageView(){
        viewPager = findViewById(R.id.viewpager);
        LayoutInflater inflater = getLayoutInflater();
        List<View> views = new ArrayList<>();
        standardMode = new StandardMode(this);
        signMode = new SignMode(this, lessonNum);
        views.add(standardMode);
        views.add(signMode);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(pagerAdapter);
    }

    private View.OnClickListener settingBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(CourseInfoActivity.this, CourseSettingActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.standard_tv){
                setSelectBtn(0);
                viewPager.setCurrentItem(0);
            }else if(view.getId()==R.id.sign_tv){
                setSelectBtn(1);
                viewPager.setCurrentItem(1);
            }
        }

        private void setSelectBtn(int index){
            View standardV = findViewById(R.id.standard_v);
            View signV = findViewById(R.id.sign_v);

            if(index==0){
                signSelLL.setVisibility(View.GONE);
                standardTV.setTextColor(getResources().getColor(R.color.light_blue_d));
                standardV.setBackgroundColor(getResources().getColor(R.color.light_blue_d));
                signTV.setTextColor(getResources().getColor(R.color.dark_gray));
                signV.setBackgroundColor(getResources().getColor(R.color.white));
            }else if(index==1){
                signSelLL.setVisibility(View.VISIBLE);
                standardTV.setTextColor(getResources().getColor(R.color.dark_gray));
                standardV.setBackgroundColor(getResources().getColor(R.color.white));
                signTV.setTextColor(getResources().getColor(R.color.light_blue_d));
                signV.setBackgroundColor(getResources().getColor(R.color.light_blue_d));
            }
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    CourseNode node = (CourseNode)msg.obj;
                    setViewsData(node);
                    break;
            }
        }
    };

    private void setViewsData(CourseNode node){
        if(node==null)
            return;
        TextView titleTV = findViewById(R.id.title_tv);
        TextView allstuTV = findViewById(R.id.allstu_tv);
        TextView signedTV = findViewById(R.id.signed_tv);
        TextView leavestuTV = findViewById(R.id.leavestu_tv);
        TextView publishwTV = findViewById(R.id.publishwork_tv);
        TextView commentedTV = findViewById(R.id.commented_tv);
        TextView completionrTV = findViewById(R.id.completionrate_tv);

        titleTV.setText(node.getLessonName());
        allstuTV.setText(String.valueOf(node.getAllStuCount()));
        signedTV.setText(String.valueOf(node.getSignedCount()));
        leavestuTV.setText(String.valueOf(node.getLeaveStuCount()));
        publishwTV.setText(String.valueOf(node.getPublishWorkCount()));
        commentedTV.setText(String.valueOf(node.getCommentedCount()));
        completionrTV.setText(node.getCompletionRate());

        if(standardMode!=null)
            standardMode.setDatas(node.getStudentNodes());
        if(signMode!=null)
            signMode.setDatas(node.getStudentNodes());
    }
}
