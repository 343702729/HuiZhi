package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.base.HtmlWebActivity;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.URLHtmlData;
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
        TextView titleTV = findViewById(R.id.task_title_tv);
        if(node.getExecuteType()==0){
            titleTV.setText("新任务");
        }else {
            titleTV.setText("已完成");
            titleTV.setTextColor(context.getResources().getColor(R.color.light_green_s));
        }
        TextView taskNTV = findViewById(R.id.task_name_tv);
        taskNTV.setText(node.getTaskName());

        FrameLayout taskFL = findViewById(R.id.task_fl);
        taskFL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HtmlWebActivity.class);
                intent.putExtra("Title", node.getTaskName());
                intent.putExtra("Url", URLHtmlData.getOperateTaskUrl(UserInfo.getInstance().getUser().getTeacherId(), node.getTaskId()));
                context.startActivity(intent);
            }
        });
    }
}