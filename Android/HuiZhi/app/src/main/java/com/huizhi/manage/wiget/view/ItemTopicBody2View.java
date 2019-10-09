package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.base.HtmlWebActivity;
import com.huizhi.manage.adapter.home.ViewPagerAdapter;
import com.huizhi.manage.adapter.topic.TopicItemGVAdapter;
import com.huizhi.manage.adapter.topic.TopicItemLVAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.fragment.home.ItemTopicFragment;
import com.huizhi.manage.http.URLHtmlData;
import com.huizhi.manage.node.CourseWareCategoryNode;
import com.huizhi.manage.node.CourseWareTypeNode;
import com.huizhi.manage.request.topic.TopicRequest;
import com.huizhi.manage.util.TLog;

import java.util.ArrayList;
import java.util.List;

public class ItemTopicBody2View extends LinearLayout {
    private Context context;
    private String category;
    private TopicItemGVAdapter topicItemGVAdapter;
    private LinearLayout level3LL;
    private GridView gridView;

    private TextView titleTV;
    private List<CourseWareTypeNode.ObjTypeItem> items = new ArrayList<>();
    private TopicItemLVAdapter adapter;

    public ItemTopicBody2View(Context context, String category){
        super(context);
        this.context = context;
        this.category = category;
        initViews();
        getDatas();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_topic_body2, this);
        level3LL = findViewById(R.id.level3_ll);
        gridView = findViewById(R.id.gridview);
        topicItemGVAdapter = new TopicItemGVAdapter(context, null);
        gridView.setAdapter(topicItemGVAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CourseWareCategoryNode node = (CourseWareCategoryNode)topicItemGVAdapter.getItem(i);
                TLog.log("Come into gridview item click:" + i);
                if(node==null) {

                    return;
                }
                if(node.getTypeCount()==0){
                    //直接架载网页
                    Intent intent = new Intent(context, HtmlWebActivity.class);
                    intent.putExtra("Title", node.getCategoryName());
                    intent.putExtra("Url", URLHtmlData.getCourseUrl(UserInfo.getInstance().getUser().getTeacherId(), node.getCategoryId(), "1"));

                    context.startActivity(intent);
                    TLog.log("Come into load web html");
                    return;
                }
                level3LL.removeAllViews();
                level3LL.addView(new ItemTopicBody3View(context, node.getCategoryId(), level3InfoUpdate));
                gridView.setVisibility(View.GONE);
                level3LL.setVerticalGravity(View.VISIBLE);
            }
        });
    }

    private void getDatas(){
        TopicRequest request = new TopicRequest();
        request.getCourseCategory(category, handler);
    }

    public void resetView(){
        getDatas();
        level3LL.removeAllViews();
        level3LL.setVerticalGravity(View.GONE);
        gridView.setVisibility(View.VISIBLE);
    }

    private BaseInfoUpdate level3InfoUpdate = new BaseInfoUpdate(){
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            boolean flag = (boolean)object;
            if(!flag)
                return;
            level3LL.removeAllViews();
            level3LL.setVerticalGravity(View.GONE);
            gridView.setVisibility(View.VISIBLE);

        }
    };

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
