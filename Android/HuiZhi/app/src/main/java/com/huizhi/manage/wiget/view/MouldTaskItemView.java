package com.huizhi.manage.wiget.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.task.HomeTaskMouldFoundActivity;
import com.huizhi.manage.activity.home.task.HomeTaskMouldSubFoundActivity;
import com.huizhi.manage.activity.home.task.HomeTaskMouldSubInfoActivity;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;

/**
 * Created by CL on 2018/1/16.
 */

public class MouldTaskItemView extends LinearLayout {
    private Activity context;
    private boolean isInfo;

    public MouldTaskItemView(Activity context){
        super(context);
        this.context = context;
        initViews();
    }

    public MouldTaskItemView(Activity context, boolean isInfo){
        super(context);
        this.context = context;
        this.isInfo = isInfo;
        initViews();
    }

    public MouldTaskItemView(Activity context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_task_mould_task, this);
    }

    public void setDates(TaskMouldNode taskNode){
        TextView titleTV = findViewById(R.id.title_tv);
        titleTV.setText(taskNode.getTaskTitle());
        TextView descriptionTV = findViewById(R.id.description_tv);
        descriptionTV.setText(taskNode.getTaskDescription());
        setTaskPriority(taskNode.getPriority());
        TextView createTimeTV = findViewById(R.id.as_time_tv);
        createTimeTV.setText(taskNode.getStrCreateTime());
        TextView assignPerTV = findViewById(R.id.as_people_tv);
        UserNode user = UserInfo.getInstance().getUserByTeacherId(taskNode.getExecuteTeacherId());
        if(user!=null)
            assignPerTV.setText(user.getTeacherName());
        FrameLayout statusFL = findViewById(R.id.status_fl);
        statusFL.setVisibility(GONE);
        FrameLayout itemFL = findViewById(R.id.task_item_fl);
        itemFL.setOnClickListener(new TaskItemClick(taskNode));

    }

    private void setTaskPriority(int priority){
        TextView lowTV = findViewById(R.id.level_l_tv);
        TextView middleTV = findViewById(R.id.level_m_tv);
        TextView highTV = findViewById(R.id.level_h_tv);
        lowTV.setVisibility(View.GONE);
        middleTV.setVisibility(View.GONE);
        highTV.setVisibility(View.GONE);
        if(priority==1){
            lowTV.setVisibility(View.VISIBLE);
        }else if(priority==2){
            middleTV.setVisibility(View.VISIBLE);
        }else if(priority==3){
            highTV.setVisibility(View.VISIBLE);
        }
    }

    private class TaskItemClick implements OnClickListener {
        private TaskMouldNode node;

        public TaskItemClick(TaskMouldNode node){
            this.node = node;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            if(isInfo)
                intent.setClass(context, HomeTaskMouldSubInfoActivity.class);
            else
                intent.setClass(context, HomeTaskMouldSubFoundActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Item", node);
            intent.putExtras(bundle);
            context.startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    }
}
