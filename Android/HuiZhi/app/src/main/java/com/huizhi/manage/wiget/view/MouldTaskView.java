package com.huizhi.manage.wiget.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.TaskMouldItemAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.util.DipPxUtil;
import com.huizhi.manage.wiget.swipe.SwipeListView;

import java.util.List;

/**
 * Created by CL on 2018/1/16.
 */

public class MouldTaskView extends LinearLayout {
    private Activity context;
    private List<TaskMouldNode> taskNodes;
    private boolean isInfo = false;
    private SwipeListView listView;
    private ScrollView scrollView;
    private TaskMouldItemAdapter taskMouldItemAdapter;
    private BaseInfoUpdate deleteInfoUpdate;

    public MouldTaskView(Activity context){
        super(context);
        this.context = context;
        initViews();
    }

    public MouldTaskView(Activity context, boolean isInfo){
        super(context);
        this.context = context;
        this.isInfo = isInfo;
        initViews();
    }

    public MouldTaskView(Activity context, boolean isInfo, BaseInfoUpdate deleteInfoUpdate){
        super(context);
        this.context = context;
        this.isInfo = isInfo;
        this.deleteInfoUpdate = deleteInfoUpdate;
        initViews();
    }

    public MouldTaskView(Activity context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_user_task_mould_task, this);
        listView = findViewById(R.id.listview);
        scrollView = findViewById(R.id.scrollview);
    }

    public void updateViews(List<TaskMouldNode> taskNodes){
        this.taskNodes = taskNodes;

        if(isInfo){
            LinearLayout tasksLL = findViewById(R.id.tasks_ll);
            tasksLL.removeAllViews();
            MouldTaskItemView itemView = null;
            for(TaskMouldNode node:taskNodes){
                itemView = new MouldTaskItemView(context, isInfo);
                itemView.setDates(node);
                tasksLL.addView(itemView);
                itemView = null;
            }
            scrollView.setVisibility(VISIBLE);
        }else {
            listView.setVisibility(VISIBLE);
            taskMouldItemAdapter = new TaskMouldItemAdapter(context, taskNodes, deleteInfoUpdate);
            listView.setAdapter(taskMouldItemAdapter);
        }

    }
}
