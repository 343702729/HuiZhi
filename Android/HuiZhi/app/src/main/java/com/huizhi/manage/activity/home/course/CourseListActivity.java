package com.huizhi.manage.activity.home.course;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
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
        getRequest.getCourseList(UserInfo.getInstance().getUser().getTeacherNum(), handler);
    }

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
