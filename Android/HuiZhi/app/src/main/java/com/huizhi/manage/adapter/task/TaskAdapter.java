package com.huizhi.manage.adapter.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.task.HomeTaskAllocationActivity;
import com.huizhi.manage.activity.home.task.HomeTaskVerifyActivity;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/14.
 */

public class TaskAdapter extends BaseAdapter {
    private Context context;
    private List<TaskNode> nodes;
    private LayoutInflater layoutInflater;

    public TaskAdapter(Context context, List<TaskNode> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateAdapter(List<TaskNode> nodes){
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return nodes.size();
    }

    @Override
    public Object getItem(int position) {
        return nodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        TaskNode node = nodes.get(position);
        if(view==null){
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.adapter_task, null);
            viewHolder.titleTV = view.findViewById(R.id.title_tv);
            viewHolder.descriptionTV = view.findViewById(R.id.description_tv);
            viewHolder.timeTV = view.findViewById(R.id.as_time_tv);
            viewHolder.peopleTV = view.findViewById(R.id.as_people_tv);
            viewHolder.limitimeIV = view.findViewById(R.id.limittime_iv);
            viewHolder.joinIV = view.findViewById(R.id.join_iv);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.titleTV.setText(node.getTaskTitle());
        viewHolder.descriptionTV.setText(node.getTaskDescription());
        viewHolder.timeTV.setText(node.getStrCreateTime());
        UserNode userNode = UserInfo.getInstance().getUserByTeacherId(node.getCreateTeacherId());
        if(userNode!=null){
            viewHolder.peopleTV.setText(userNode.getTeacherName());
        }
        viewHolder.limitimeIV.setVisibility(View.GONE);
        viewHolder.joinIV.setVisibility(View.GONE);
        if(node.isTimeLimit()){
            viewHolder.limitimeIV.setVisibility(View.VISIBLE);
        }
        if(node.getIsJoin()){
            viewHolder.joinIV.setVisibility(View.VISIBLE);
        }
        setTaskStatus(node.getTaskStatus(), view);
        setTaskPriority(node.getPriority(), view);
        return view;
    }

    class ViewHolder {
        public TextView titleTV;
        public TextView descriptionTV;
        public TextView timeTV;
        public TextView peopleTV;
        public ImageView limitimeIV;
        public ImageView joinIV;
    }

    private void setTaskPriority(int priority, View view){
        TextView lowTV = view.findViewById(R.id.level_l_tv);
        TextView middleTV = view.findViewById(R.id.level_m_tv);
        TextView highTV = view.findViewById(R.id.level_h_tv);
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

    private void setTaskStatus(int status, View view){
        TextView pendingTV = view.findViewById(R.id.task_pending_tv);
        TextView auditTV = view.findViewById(R.id.task_audit_tv);
        TextView completeTV = view.findViewById(R.id.task_complete_tv);
        TextView refusedTV = view.findViewById(R.id.task_refused_tv);
        TextView closedTV = view.findViewById(R.id.task_close_tv);
        pendingTV.setVisibility(View.GONE);
        auditTV.setVisibility(View.GONE);
        completeTV.setVisibility(View.GONE);
        refusedTV.setVisibility(View.GONE);
        closedTV.setVisibility(View.GONE);
        if(status==1){
            pendingTV.setVisibility(View.VISIBLE);
        }else if(status==2){
            auditTV.setVisibility(View.VISIBLE);
        }else if(status==3){
            completeTV.setVisibility(View.VISIBLE);
        }else if(status==4){
            refusedTV.setVisibility(View.VISIBLE);
        }else if(status==5){
            closedTV.setVisibility(View.VISIBLE);
        }
    }

}
