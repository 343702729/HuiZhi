package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.task.HomeTaskSystemFoundActivity;
import com.huizhi.manage.node.TaskSystemNode;

/**
 * Created by CL on 2018/1/8.
 */

public class LineTaskItemView extends LinearLayout {
    private Context context;

    public LineTaskItemView(Context context){
        super(context);
        this.context = context;
        initViews();
    }

    public LineTaskItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_task_line, this);
    }

    public void setDates(TaskSystemNode taskNode){
        if(taskNode==null)
            return;
        TextView titleTV = findViewById(R.id.line_name_tv);
        titleTV.setText(taskNode.getTaskTitle());
        LinearLayout itemLL = findViewById(R.id.line_ll);
        itemLL.setOnClickListener(new ItemLineClick(taskNode));
    }

    private class ItemLineClick implements OnClickListener{
        private TaskSystemNode node;

        public ItemLineClick(TaskSystemNode node){
            this.node = node;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(context, HomeTaskSystemFoundActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Item", node);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
}
