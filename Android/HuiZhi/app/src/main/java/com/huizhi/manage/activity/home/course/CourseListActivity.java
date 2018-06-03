package com.huizhi.manage.activity.home.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.CourseAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.node.CourseNode;
import com.huizhi.manage.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends Activity {
    private ListView listView;
    private CourseAdapter courseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_list);
        initViews();
    }

    private void initViews() {
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        listView = findViewById(R.id.listview);
        List<CourseNode> nodes = new ArrayList<>();
        nodes.add(new CourseNode());
        nodes.add(new CourseNode());
        nodes.add(new CourseNode());
        nodes.add(new CourseNode());
        nodes.add(new CourseNode());
        nodes.add(new CourseNode());
        courseAdapter = new CourseAdapter(this, nodes);
        listView.setAdapter(courseAdapter);
        listView.setOnItemClickListener(courseItemClick);
    }

    private AdapterView.OnItemClickListener courseItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            CourseNode node = (CourseNode)courseAdapter.getItem(i);
            Intent intent = new Intent();
            intent.setClass(CourseListActivity.this, CourseInfoActivity.class);
            startActivity(intent);
        }
    };
}
