package com.huizhi.manage.activity.home.course;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.task.HomeTaskCustomFoundActivity;
import com.huizhi.manage.adapter.home.CourseAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.dialog.LoadingProgress;
import com.huizhi.manage.node.CourseInfoNode;
import com.huizhi.manage.node.CourseNode;
import com.huizhi.manage.request.home.HomeCourseGetRequest;
import com.huizhi.manage.util.AppUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 课程列表
 */
public class CourseListActivity extends Activity {
    private ProgressBar progressBar1, progressBar2;
    private ListView listView;
    private CourseAdapter courseAdapter;
    private LinearLayout loadingLL;
    private ImageView loadingIV;
    private Button day1Btn, day2Btn, day3Btn, day4Btn;
    private String lessonDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_list);
        initViews();
        getDatas();
    }

    private void initViews() {
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        loadingLL = findViewById(R.id.loading_ll);
        loadingIV = findViewById(R.id.loadingImageView);

        progressBar1 = findViewById(R.id.progress_bar1);
        progressBar2 = findViewById(R.id.progress_bar2);

        day1Btn = findViewById(R.id.day1_btn);
        day2Btn = findViewById(R.id.day2_btn);
        day3Btn = findViewById(R.id.day3_btn);
        day4Btn = findViewById(R.id.day4_btn);

        day1Btn.setOnClickListener(dayBtnClick);
        day2Btn.setOnClickListener(dayBtnClick);
        day3Btn.setOnClickListener(dayBtnClick);
        day4Btn.setOnClickListener(dayBtnClick);

        Calendar calendar = Calendar.getInstance();
        String str = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DATE);
        TextView calendarDTV = findViewById(R.id.calendar_day_tv);
        calendarDTV.setText(str);

        LinearLayout calendarLL = findViewById(R.id.calendar_ll);
        calendarLL.setOnClickListener(calendarLLClick);

        listView = findViewById(R.id.listview);
        courseAdapter = new CourseAdapter(this, null);
        listView.setAdapter(courseAdapter);
        listView.setOnItemClickListener(courseItemClick);
    }

    private void getDatas(){
        loadingLL.setVisibility(View.VISIBLE);
        AnimationDrawable animationDrawable = (AnimationDrawable) loadingIV.getBackground();
        animationDrawable.start();
        HomeCourseGetRequest getRequest = new HomeCourseGetRequest();
        getRequest.getCourseList(UserInfo.getInstance().getUser().getTeacherNum(), lessonDate, handler);
    }

    private View.OnClickListener dayBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            initBtns();
            //LessonDate

            if(view.getId()==R.id.day1_btn){
                lessonDate = "";
                day1Btn.setBackgroundResource(R.drawable.frame_course_gray_l_fc);
                day1Btn.setTextColor(getResources().getColor(R.color.white));
            }else if(view.getId()==R.id.day2_btn){
                lessonDate = AppUtil.getDate(0);
                day2Btn.setBackgroundColor(getResources().getColor(R.color.blue_light));
                day2Btn.setTextColor(getResources().getColor(R.color.white));
            }else if(view.getId()==R.id.day3_btn){
                lessonDate = AppUtil.getDate(-1);
                day3Btn.setBackgroundColor(getResources().getColor(R.color.blue_light));
                day3Btn.setTextColor(getResources().getColor(R.color.white));
            }else if(view.getId()==R.id.day4_btn){
                lessonDate = AppUtil.getDate(1);
                day4Btn.setBackgroundResource(R.drawable.frame_course_gray_r_fc);
                day4Btn.setTextColor(getResources().getColor(R.color.white));
            }
            Log.i("HuiZhi", "The lessonDate is:" + lessonDate);
            getDatas();
        }

    };

    private void initBtns(){
        day1Btn.setBackgroundResource(R.drawable.frame_course_gray_l_bg);
        day1Btn.setTextColor(getResources().getColor(R.color.gray));
        day2Btn.setBackgroundColor(getResources().getColor(R.color.white));
        day2Btn.setTextColor(getResources().getColor(R.color.gray));
        day3Btn.setBackgroundColor(getResources().getColor(R.color.white));
        day3Btn.setTextColor(getResources().getColor(R.color.gray));
        day4Btn.setBackgroundResource(R.drawable.frame_course_gray_r_bg);
        day4Btn.setTextColor(getResources().getColor(R.color.gray));
    }

    private View.OnClickListener calendarLLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            final DatePickerDialog picker = new DatePickerDialog(CourseListActivity.this, null, year, month, day);
            picker.setCancelable(true);
            picker.setCanceledOnTouchOutside(true);
            picker.setButton(DialogInterface.BUTTON_POSITIVE, "确认",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initBtns();
                            //确定的逻辑代码
                            DatePicker dp = picker.getDatePicker();
                            int year = dp.getYear();
                            int monthOfYear = dp.getMonth() + 1;
                            int dayOfMonth = dp.getDayOfMonth();

                            String strMonth, strDay;
                            if(monthOfYear<10)
                                strMonth = "0" + monthOfYear;
                            else
                                strMonth = "" + monthOfYear;
                            if(dayOfMonth<10)
                                strDay = "0" + dayOfMonth;
                            else
                                strDay = "" + dayOfMonth;
                            String str;
                            str = year + "-" + strMonth + "-" + strDay;
                            lessonDate = year + strMonth + strDay;
                            Log.i("HuiZhi", "The calendar lessonDate is:" + lessonDate);
                            TextView calendarDTV = findViewById(R.id.calendar_day_tv);
                            calendarDTV.setText(str);
                            getDatas();
                        }
                    });

            picker.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //取消什么也不用做
                        }
                    });
            picker.show();
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        }
    } ;

    private AdapterView.OnItemClickListener courseItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            CourseNode node = (CourseNode)courseAdapter.getItem(i);
            Intent intent = new Intent();
            intent.setClass(CourseListActivity.this, CourseInfoActivity.class);
            intent.putExtra("LessonNum", node.getLessonNum());
            startActivity(intent);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadingLL.setVisibility(View.GONE);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    CourseInfoNode infoNode = (CourseInfoNode)msg.obj;
                    setDataViews(infoNode);
                    break;
            }
        }
    };

    public void setDataViews(CourseInfoNode infoNode){
        if(infoNode==null)
            return;
        courseAdapter.updateViews(infoNode.getLessons());
        progressBar1.setMax(infoNode.getTotLessonNum());
        progressBar1.setProgress(infoNode.getDoneLessonNum());

        progressBar2.setMax(infoNode.getCommentedNum());
        progressBar2.setProgress(infoNode.getToBeCommentNum());

        TextView lessonZTV = findViewById(R.id.lesson_z_tv);
        TextView lessonMTV = findViewById(R.id.lesson_m_tv);
        lessonZTV.setText(infoNode.getDoneLessonNum() + "");
        lessonMTV.setText(infoNode.getTotLessonNum() + "");

        TextView commentZTV = findViewById(R.id.comment_z_tv);
        TextView commentMTV = findViewById(R.id.comment_m_tv);
        commentZTV.setText(infoNode.getToBeCommentNum() + "");
        commentMTV.setText(infoNode.getCommentedNum() + "");

        TextView lessonTV = findViewById(R.id.lesson_tv);
//        if(infoNode.getTotLessonNum()!=0)
        lessonTV.setText(infoNode.getLessonFinishPercent());

        TextView commentTV = findViewById(R.id.comment_tv);
//        if(infoNode.getCommentedNum()!=0)
        commentTV.setText(infoNode.getCommentFinishPercent());
    }
}
