package com.huizhi.manage.activity.home.course;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.CoursePictureAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.node.PictureNode;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class CourseReleaseActivity extends Activity {
    private MyGridView gridView;
    private CoursePictureAdapter pictureAdapter;
    List<PictureNode> nodes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_release);

        initViews();
    }

    private void initViews() {
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        gridView = findViewById(R.id.gridview);
        pictureAdapter = new CoursePictureAdapter(this, nodes);
        gridView.setAdapter(pictureAdapter);

        LinearLayout submitLL = findViewById(R.id.submit_ll);
        submitLL.setOnClickListener(submitLLClick);

        Button saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(saveBtnClick);
    }

    private View.OnClickListener submitLLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(nodes.size()>=4){
                Toast.makeText(CourseReleaseActivity.this, "最多上传4张", Toast.LENGTH_SHORT).show();
                return;
            }
            nodes.add(new PictureNode());
            pictureAdapter.updateViewsData(nodes);
        }
    };

    private View.OnClickListener saveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("HuiZhi", "The pic size is:" + nodes.size());
        }
    };
}
