package com.huizhi.manage.wiget.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.task.HomeTaskCustomSubFoundActivity;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;

/**
 * Created by CL on 2018/1/11.
 */

public class SubTaskItemView extends LinearLayout {
    private Activity activity;
    private TaskNode taskNode;
    private String parentId;

    public SubTaskItemView(Activity activity){
        super(activity);
        this.activity = activity;
        initViews();
    }

    public SubTaskItemView(Activity activity, AttributeSet attrs){
        super(activity, attrs);
        this.activity = activity;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_task_sub, this);
    }

    public void setDates(TaskNode taskNode, String parentId){
        this.taskNode = taskNode;
        this.parentId = parentId;
        TextView titleTV = findViewById(R.id.sub_task_title_tv);
        titleTV.setText(taskNode.getTaskTitle());
        TextView personTV = findViewById(R.id.sub_task_person_tv);
        UserNode user = UserInfo.getInstance().getUserByTeacherId(taskNode.getProcessingTeacherId());
        personTV.setText(user.getTeacherName());
        LinearLayout taskLL = findViewById(R.id.sub_task_ll);
        taskLL.setOnClickListener(subTaskItemClick);
    }

    private OnClickListener subTaskItemClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(activity, HomeTaskCustomSubFoundActivity.class);
            intent.putExtra("ParentId", parentId);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Item", taskNode);
            intent.putExtras(bundle);
            activity.startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    };

}
