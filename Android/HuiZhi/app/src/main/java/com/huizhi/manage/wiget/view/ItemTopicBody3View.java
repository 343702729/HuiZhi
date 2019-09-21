package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.topic.TopicItemLVAdapter;
import com.huizhi.manage.util.TLog;

import java.util.ArrayList;
import java.util.List;

public class ItemTopicBody3View extends LinearLayout {
    private Context context;

    public ItemTopicBody3View(Context context){
        super(context);
        this.context = context;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_topic_body3, this);
        View headV= View.inflate(context, R.layout.item_topic_lv_title, null);
        ListView listView = findViewById(R.id.listview);
        listView.addHeaderView(headV);
        List<String> items = new ArrayList<>();
        items.add("1");
        items.add("1");
        items.add("1");
        items.add("1");
        items.add("1");
        items.add("1");
        items.add("1");
        items.add("1");
        items.add("1");
        TopicItemLVAdapter adapter = new TopicItemLVAdapter(context, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TLog.log("Come into listview item click" + i);
            }
        });
    }
}
