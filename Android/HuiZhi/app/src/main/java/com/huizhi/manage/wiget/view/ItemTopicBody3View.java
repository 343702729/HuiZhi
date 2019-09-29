package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.topic.TopicItemLVAdapter;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.node.CourseWareTypeNode;
import com.huizhi.manage.request.topic.TopicRequest;
import com.huizhi.manage.util.TLog;

import java.util.ArrayList;
import java.util.List;

public class ItemTopicBody3View extends LinearLayout {
    private Context context;
    private TextView titleTV;
    private List<CourseWareTypeNode.ObjTypeItem> items = new ArrayList<>();
    private TopicItemLVAdapter adapter;

    public ItemTopicBody3View(Context context){
        super(context);
        this.context = context;
        initViews();
        getDatas();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_topic_body3, this);
        View headV= View.inflate(context, R.layout.item_topic_lv_title, null);
        titleTV = headV.findViewById(R.id.title_tv);
        ListView listView = findViewById(R.id.listview);
        listView.addHeaderView(headV);
        adapter = new TopicItemLVAdapter(context, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CourseWareTypeNode.ObjTypeItem item = (CourseWareTypeNode.ObjTypeItem)adapter.getItem(i-1);
                TLog.log("Come into listview item click" + i + "  name:" + item.getName());


            }
        });
    }

    private void getDatas(){
        TopicRequest request = new TopicRequest();
        request.getCourseWareType("fef7a99eeab94cee9023f0668f7b4fa2", handler);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    CourseWareTypeNode node = (CourseWareTypeNode)msg.obj;
                    TLog.log("The node item size:" + node.getObjTypeList().size());
                    setViewsData(node);
                    break;
            }
        }
    };

    private void setViewsData(CourseWareTypeNode node){
        if(node==null)
            return;
        titleTV.setText(node.getObjCategory().getCategoryName());
        adapter.updateList(node.getObjTypeList());
    }
}
