package com.huizhi.manage.activity.home.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.CourseAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
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

        progressBar1 = findViewById(R.id.progress_bar1);
        progressBar2 = findViewById(R.id.progress_bar2);

        listView = findViewById(R.id.listview);
        courseAdapter = new CourseAdapter(this, null);
        listView.setAdapter(courseAdapter);
        listView.setOnItemClickListener(courseItemClick);
    }

    private void getDatas(){
        HomeCourseGetRequest getRequest = new HomeCourseGetRequest();
        //UserInfo.getInstance().getUser().getTeacherName()
        getRequest.getCourseList("朱虹", handler);
    }

    private AdapterView.OnItemClickListener courseItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            CourseNode node = (CourseNode)courseAdapter.getItem(i);
            Intent intent = new Intent();
            intent.setClass(CourseListActivity.this, CourseInfoActivity.class);
            intent.putExtra("LessonNum", "037670");
            startActivity(intent);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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

        TextView lessonTV = findViewById(R.id.lesson_tv);
        if(infoNode.getTotLessonNum()!=0)
            lessonTV.setText(infoNode.getDoneLessonNum()*100/infoNode.getTotLessonNum() + "%");

        TextView commentTV = findViewById(R.id.comment_tv);
        if(infoNode.getCommentedNum()!=0)
            commentTV.setText(infoNode.getToBeCommentNum()*100/infoNode.getCommentedNum() + "%");
    }
}
