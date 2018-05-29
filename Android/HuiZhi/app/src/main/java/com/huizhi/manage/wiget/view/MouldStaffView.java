package com.huizhi.manage.wiget.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.node.TaskNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CL on 2018/1/16.
 */

public class MouldStaffView extends LinearLayout {
    private Activity context;
    private boolean isInfo = false;

    public MouldStaffView(Activity context){
        super(context);
        this.context = context;
        initViews();
    }

    public MouldStaffView(Activity context, boolean isInfo){
        super(context);
        this.context = context;
        this.isInfo = isInfo;
        initViews();
    }

    public MouldStaffView(Activity context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_user_task_mould_staff, this);
    }

    public void updateViews(List<TaskMouldNode> taskNodes){
        if(taskNodes==null)
            return;
        Map<String , List<TaskMouldNode>> maps = new LinkedHashMap<>();
        for(TaskMouldNode node:taskNodes){
            String teacherid = node.getExecuteTeacherId();
            List<TaskMouldNode> listT;
            if(maps.containsKey(teacherid)){
                listT = maps.get(teacherid);
                listT.add(node);
                maps.put(teacherid, listT);
            }else {
                listT = new ArrayList<>();
                listT.add(node);
                maps.put(teacherid, listT);
            }
        }

        LinearLayout itemsLL = findViewById(R.id.staffs_ll);
        itemsLL.removeAllViews();
        for(String key:maps.keySet()){
            MouldStaffItemView itemView = new MouldStaffItemView(context, isInfo);
            itemView.setDateViews(key, maps.get(key));
            itemsLL.addView(itemView);
        }
    }
}
