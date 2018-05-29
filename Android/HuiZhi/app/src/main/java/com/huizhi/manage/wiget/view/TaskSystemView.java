package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.TaskCategoryNode;
import com.huizhi.manage.node.TaskSystemNode;

import java.util.List;

/**
 * Created by CL on 2018/1/8.
 */

public class TaskSystemView extends LinearLayout {
    private Context context;

    public TaskSystemView(Context context){
        super(context);
        this.context = context;
        initViews();
    }

    public TaskSystemView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_task_system, this);
    }

    public void setDates(TaskCategoryNode categoryNode){
        if(categoryNode==null)
            return;
        TextView titleTV = findViewById(R.id.item_title_tv);
        titleTV.setText(categoryNode.getCategoryName());
        List<TaskSystemNode> taskNodes = categoryNode.getTaskSystemNodes();
        LinearLayout itemsLL = findViewById(R.id.items_ll);
        if(taskNodes!=null){
            for(TaskSystemNode node:taskNodes){
                LineTaskItemView lineTaskItem = new LineTaskItemView(context);
                lineTaskItem.setDates(node);
                itemsLL.addView(lineTaskItem);
            }
        }
    }

}
