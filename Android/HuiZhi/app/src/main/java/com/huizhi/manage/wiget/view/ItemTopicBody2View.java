package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.topic.TopicItemGVAdapter;
import com.huizhi.manage.util.TLog;

import java.util.ArrayList;
import java.util.List;

public class ItemTopicBody2View extends LinearLayout {
    private Context context;

    public ItemTopicBody2View(Context context){
        super(context);
        this.context = context;

        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_topic_body2, this);
        GridView gridView = findViewById(R.id.gridview);
        List<String> items = new ArrayList<>();
        items.add("1");
        items.add("2");
        items.add("3");
        items.add("1");
        items.add("1");
        items.add("1");
        items.add("1");
        items.add("1");
        items.add("1");
        items.add("1");
        items.add("1");
        TopicItemGVAdapter topicItemGVAdapter = new TopicItemGVAdapter(context, items);
        gridView.setAdapter(topicItemGVAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TLog.log("Come into gridview item click:" + i);
            }
        });
    }
}
