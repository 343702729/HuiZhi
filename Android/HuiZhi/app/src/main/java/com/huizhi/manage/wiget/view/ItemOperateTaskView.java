package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.HomeOperateNode;

public class ItemOperateTaskView extends LinearLayout {
    private Context context;
    private HomeOperateNode.ObjTask node;

    public ItemOperateTaskView(Context context, HomeOperateNode.ObjTask node){
        super(context);
        this.context = context;
        this.node = node;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_operate_task, this);
        TextView taskNTV = findViewById(R.id.task_name_tv);
        taskNTV.setText(node.getTaskName());
    }
}