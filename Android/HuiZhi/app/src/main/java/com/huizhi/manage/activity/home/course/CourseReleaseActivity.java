package com.huizhi.manage.activity.home.course;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.CoursePictureAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.node.PictureNode;
import com.huizhi.manage.node.StudentNode;
import com.huizhi.manage.request.home.HomeCourseGetRequest;
import com.huizhi.manage.request.home.HomeCoursePostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class CourseReleaseActivity extends Activity {
    private String lessonNum;
    private String stuNum;
    private MyGridView gridView;
    private TextView signSTV;
    private Button signBtn;
    private CoursePictureAdapter pictureAdapter;
    private StudentNode studentNode;
    private List<PictureNode> picNodes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_release);
        initDatas();
        initViews();
        getDatas();
    }

    private void initDatas(){
        lessonNum = getIntent().getStringExtra("LessonNum");
        stuNum = getIntent().getStringExtra("StuNum");
    }

    private void initViews() {
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        signSTV = findViewById(R.id.sign_status_tv);
        signBtn = findViewById(R.id.sign_btn);


        gridView = findViewById(R.id.gridview);
        pictureAdapter = new CoursePictureAdapter(this, picNodes);
        gridView.setAdapter(pictureAdapter);

        LinearLayout submitLL = findViewById(R.id.submit_ll);
        submitLL.setOnClickListener(submitLLClick);

        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(saveBtnClick);
    }

    private void getDatas(){
        HomeCourseGetRequest getRequest = new HomeCourseGetRequest();
        getRequest.getCourseStuInfo(lessonNum, stuNum, handler);
    }

    private View.OnClickListener submitLLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(picNodes.size()>=4){
                Toast.makeText(CourseReleaseActivity.this, "最多上传4张", Toast.LENGTH_SHORT).show();
                return;
            }
            picNodes.add(new PictureNode());
            pictureAdapter.updateViewsData(picNodes);
        }
    };

    private View.OnClickListener saveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("HuiZhi", "The pic size is:" + picNodes.size());
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    studentNode = (StudentNode)msg.obj;
                    setViewsData(studentNode);
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    signSTV.setText("已签到");
                    signSTV.setTextColor(getResources().getColor(R.color.app_green));
                    signBtn.setVisibility(View.GONE);
                    if(studentNode!=null) {
                        studentNode.setStuStatus(1);
                        studentNode.setStrStuStatus("已签到");
                    }
                    break;
            }
        }
    };

    private void setViewsData(StudentNode node){
        if(node==null)
            return;
        TextView nameTV = findViewById(R.id.name_tv);
        nameTV.setText(node.getStuName());
        signBtn.setOnClickListener(new SignBtnClick(node.getStuNum()));
        signSTV.setText(node.getStrStuStatus());
        if(node.getStuStatus()==-1){
            signSTV.setTextColor(getResources().getColor(R.color.red));
        }else if(node.getStuStatus()==0){
            signSTV.setTextColor(getResources().getColor(R.color.dark_gray));
            signBtn.setVisibility(View.VISIBLE);
        }else if(node.getStuStatus()==1){
            signSTV.setTextColor(getResources().getColor(R.color.app_green));
        }
    }

    private class SignBtnClick implements View.OnClickListener{
        private String stuNum;

        public SignBtnClick(String stuNum){
            this.stuNum = stuNum;
        }

        @Override
        public void onClick(View view) {
            HomeCoursePostRequest postRequest = new HomeCoursePostRequest();
            postRequest.postStudentsSignInfo(lessonNum, stuNum, handler);
        }
    };
}
