package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.topic.TopicItemGVAdapter;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.node.CourseWareCategoryNode;
import com.huizhi.manage.node.CourseWareTypeNode;
import com.huizhi.manage.request.topic.TopicRequest;
import com.huizhi.manage.util.TLog;

import java.util.ArrayList;
import java.util.List;

public class ItemTopicBody2View extends LinearLayout {
    private Context context;
    private TopicItemGVAdapter topicItemGVAdapter;

    public ItemTopicBody2View(Context context){
        super(context);
        this.context = context;

        initViews();
        getDatas();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_topic_body2, this);
        GridView gridView = findViewById(R.id.gridview);
        topicItemGVAdapter = new TopicItemGVAdapter(context, null);
        gridView.setAdapter(topicItemGVAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TLog.log("Come into gridview item click:" + i);
            }
        });
    }

    private void getDatas(){
        TopicRequest request = new TopicRequest();
        request.getCourseCategory("be5d0b7ab9814731a067623a0b7324b1", handler);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    List<CourseWareCategoryNode> nodes = (List<CourseWareCategoryNode>)msg.obj;
                    TLog.log("The nodes size:" + nodes.size());
                    if(nodes!=null)
                        topicItemGVAdapter.updateList(nodes);
//                    setViewsData(node);
                    break;
            }
        }
    };
}
