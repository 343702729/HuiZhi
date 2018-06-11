package com.huizhi.manage.activity.home.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.ViewPagerAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.CourseFilterDialog;
import com.huizhi.manage.node.CourseNode;
import com.huizhi.manage.request.home.HomeCourseGetRequest;
import com.huizhi.manage.request.home.HomeCoursePostRequest;
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
    private View lineV;
    private CourseNode courseNode;
    private Button teacherSignBtn, assistantSignBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_info);
        initDatas();
        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
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

        Button filterBtn = findViewById(R.id.filter_btn);
        filterBtn.setOnClickListener(filterBtnClick);

        teacherSignBtn = findViewById(R.id.teacher_sign_btn);
        assistantSignBtn = findViewById(R.id.assistant_sign_btn);
        teacherSignBtn.setOnClickListener(signBtnClick);
        assistantSignBtn.setOnClickListener(signBtnClick);


        lineV = findViewById(R.id.line_v);

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
        standardMode = new StandardMode(this, lessonNum);
        signMode = new SignMode(this, lessonNum, infoUpdate);
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

    private View.OnClickListener filterBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CourseFilterDialog filterDialog = new CourseFilterDialog(CourseInfoActivity.this, null);
            filterDialog.showView(view);
        }
    };

    private View.OnClickListener signBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("HuiZhi", "Come into sign click");
            if(courseNode==null)
                return;
            if(view.getId()==R.id.teacher_sign_btn){
                if(courseNode.isSignInTeacher())
                    return;
                HomeCoursePostRequest postRequest = new HomeCoursePostRequest();
                postRequest.postTeacherSign(lessonNum, handler);
            }else if(view.getId()==R.id.assistant_sign_btn){
                if(courseNode.isSignInTutor())
                    return;
                HomeCoursePostRequest postRequest = new HomeCoursePostRequest();
                postRequest.postTutorSign(lessonNum, handler);
            }
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
//                signSelLL.setVisibility(View.GONE);
                standardTV.setTextColor(getResources().getColor(R.color.light_blue_d));
                standardV.setBackgroundColor(getResources().getColor(R.color.light_blue_d));
                signTV.setTextColor(getResources().getColor(R.color.dark_gray));
                signV.setBackgroundColor(getResources().getColor(R.color.white));
            }else if(index==1){
//                signSelLL.setVisibility(View.VISIBLE);
                standardTV.setTextColor(getResources().getColor(R.color.dark_gray));
                standardV.setBackgroundColor(getResources().getColor(R.color.white));
                signTV.setTextColor(getResources().getColor(R.color.light_blue_d));
                signV.setBackgroundColor(getResources().getColor(R.color.light_blue_d));
            }
        }
    };

    private BaseInfoUpdate infoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            boolean flage = (boolean)object;
            if(flage)
                getDatas();
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String mesg;
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    courseNode = (CourseNode)msg.obj;
                    setViewsData(courseNode);
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    mesg = (String)msg.obj;
                    if(!TextUtils.isEmpty(mesg)){
                        if(courseNode!=null)
                            courseNode.setSignInTeacher(true);
                        teacherSignBtn.setBackgroundResource(R.drawable.frame_bg_s_red);
                        teacherSignBtn.setTextColor(getResources().getColor(R.color.white));
                        Toast.makeText(CourseInfoActivity.this, mesg, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MSG_SUCCESS_TWO:
                    mesg = (String)msg.obj;
                    if(!TextUtils.isEmpty(mesg)){
                        if(courseNode!=null)
                            courseNode.setSignInTutor(true);
                        assistantSignBtn.setBackgroundResource(R.drawable.frame_bg_s_red);
                        assistantSignBtn.setTextColor(getResources().getColor(R.color.white));
                        Toast.makeText(CourseInfoActivity.this, mesg, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MSG_FAILURE:
                    mesg = (String)msg.obj;
                    if(!TextUtils.isEmpty(mesg)){
                        Toast.makeText(CourseInfoActivity.this, mesg, Toast.LENGTH_SHORT).show();
                    }
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

        if(node.isSignInTeacher()){
            teacherSignBtn.setBackgroundResource(R.drawable.frame_bg_s_red);
            teacherSignBtn.setTextColor(getResources().getColor(R.color.white));
        }

        if(node.isSignInTutor()){
            assistantSignBtn.setBackgroundResource(R.drawable.frame_bg_s_red);
            assistantSignBtn.setTextColor(getResources().getColor(R.color.white));
        }

        if(standardMode!=null)
            standardMode.setDatas(node.getStudentNodes());
        if(signMode!=null)
            signMode.setDatas(node.getStudentNodes());
    }
}
