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
        TextView taskNTV = findViewById(R.id.task_name_tv);
        titleTV.setText(node.getTaskName());
        if(node.getExecuteStatus()==1){
            taskNTV.setText("已完成");
        }else {
            taskNTV.setText(node.getStrBlockingDate() + "前执行");
//            titleTV.setTextColor(context.getResources().getColor(R.color.light_green_s));
        }

        LinearLayout taskLL = findViewById(R.id.task_item_ll);
        taskLL.setOnClickListener(new OnClickListener() {
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