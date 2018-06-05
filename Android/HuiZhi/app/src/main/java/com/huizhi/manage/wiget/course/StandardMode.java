package com.huizhi.manage.wiget.course;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.course.CourseReleaseActivity;
import com.huizhi.manage.adapter.home.StandardModeAdapter;
import com.huizhi.manage.node.StandardNode;

import java.util.ArrayList;
import java.util.List;

public class StandardMode extends LinearLayout {
    private Activity context;
    private ListView listView;
    private StandardModeAdapter standardModeAdapter;

    public StandardMode(Activity context){
        super(context);
        this.context = context;
        initViews();
    }

    public StandardMode(Activity context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_course_mode_standard, this);
        listView = findViewById(R.id.listview);
        List<StandardNode> nodes = new ArrayList<>();
        nodes.add(new StandardNode());
        nodes.add(new StandardNode());
        nodes.add(new StandardNode());
        nodes.add(new StandardNode());
        nodes.add(new StandardNode());
        nodes.add(new StandardNode());
        nodes.add(new StandardNode());
        nodes.add(new StandardNode());
        nodes.add(new StandardNode());
        nodes.add(new StandardNode());
        standardModeAdapter = new StandardModeAdapter(context, nodes);
        listView.setAdapter(standardModeAdapter);
        listView.setOnItemClickListener(itemClickListener);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            StandardNode node = (StandardNode)standardModeAdapter.getItem(i);
            Intent intent = new Intent();
            intent.setClass(context, CourseReleaseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Node", node);
            intent.putExtras(intent);
            context.startActivity(intent);
        }
    };

}
