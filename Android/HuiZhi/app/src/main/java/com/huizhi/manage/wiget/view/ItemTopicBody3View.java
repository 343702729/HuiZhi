package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.activity.base.HtmlWebActivity;
import com.huizhi.manage.activity.home.course.CoursewareActivity;
import com.huizhi.manage.adapter.topic.TopicItemLVAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.URLHtmlData;
import com.huizhi.manage.node.CourseWareTypeNode;
import com.huizhi.manage.request.topic.TopicRequest;
import com.huizhi.manage.util.TLog;

import java.util.ArrayList;
import java.util.List;

public class ItemTopicBody3View extends LinearLayout {
    private Context context;
    private String category;
    private TextView titleTV;
    private List<CourseWareTypeNode.ObjTypeItem> items = new ArrayList<>();
    private TopicItemLVAdapter adapter;
    private BaseInfoUpdate infoUpdate;
    private CourseWareTypeNode courseNode;
    private LinearLayout itemsLL;
//    private View headV;

    public ItemTopicBody3View(Context context, String category, BaseInfoUpdate infoUpdate){
        super(context);
        this.context = context;
        this.category = category;
        this.infoUpdate = infoUpdate;
        initViews();
        getDatas();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_topic_body3, this);
        TextView backBtn = findViewById(R.id.level3_back_btn);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                TLog.log("Come into topic body 3");
                infoUpdate.update(true);
            }
        });
        itemsLL = findViewById(R.id.items_ll);
//        headV= View.inflate(context, R.layout.item_topic_lv_title, null);
        titleTV = findViewById(R.id.title_tv);
//        ListView listView = findViewById(R.id.listview);
//        listView.addHeaderView(headV);
//        adapter = new TopicItemLVAdapter(context, items);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                CourseWareTypeNode.ObjTypeItem item = (CourseWareTypeNode.ObjTypeItem)adapter.getItem(i-1);
//                TLog.log("Come into listview item click" + i );
//                Intent intent = new Intent(context, CoursewareActivity.class);
//                intent.putExtra("Title", courseNode.getObjCategory().getCategoryName());
//                intent.putExtra("Url", URLHtmlData.getCourseUrl(UserInfo.getInstance().getUser().getTeacherId(), category, item.getCode() + ""));
//
//                context.startActivity(intent);
//            }
//        });
    }

    private void getDatas(){
        TopicRequest request = new TopicRequest();
        request.getCourseWareType(category, handler);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS_ONE:
                    courseNode = (CourseWareTypeNode)msg.obj;
//                    TLog.log("The node item size:" + node.getObjTypeList().size());
                    setViewsData(courseNode);
                    break;
            }
        }
    };

    private void setViewsData(CourseWareTypeNode node){
        itemsLL.removeAllViews();
        if(node==null)
            return;
        titleTV.setText(node.getObjCategory().getCategoryName());
        for (final CourseWareTypeNode.ObjTypeItem item:node.getObjTypeList()){
            ImageView imageView = new ImageView(context);
            itemsLL.addView(imageView);
            Glide.with(context).load(item.getCoursewareUrl()).into(imageView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CoursewareActivity.class);
                    intent.putExtra("Title", courseNode.getObjCategory().getCategoryName());
                    intent.putExtra("Url", URLHtmlData.getCourseUrl(UserInfo.getInstance().getUser().getTeacherId(), category, item.getCode() + ""));

                    context.startActivity(intent);
                }
            });
        }
//        adapter.updateList(node.getObjTypeList());
    }
}
