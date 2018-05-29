package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.task.HomeTaskVerifyActivity;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.request.home.HomeTaskPostRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/14.
 */

public class TaskItemAdapter extends BaseAdapter {
    private Context context;
    private List<TaskNode> nodes;
    private LayoutInflater layoutInflater;
    private Handler handler;

    public TaskItemAdapter(Context context, List<TaskNode> nodes, Handler handler){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
        this.handler = handler;
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

    private void deleteItem(int position){
        nodes.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        TaskNode node = nodes.get(position);
        if(view==null){
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.adapter_task_item, null);
            viewHolder.itemFL = view.findViewById(R.id.item_fl);
            viewHolder.titleTV = view.findViewById(R.id.title_tv);
            viewHolder.descriptionTV = view.findViewById(R.id.description_tv);
            viewHolder.timeTV = view.findViewById(R.id.as_time_tv);
            viewHolder.peopleTV = view.findViewById(R.id.as_people_tv);
            viewHolder.deleteBtn = view.findViewById(R.id.delete_btn);
            viewHolder.limitimeIV = view.findViewById(R.id.limittime_iv);
            viewHolder.joinIV = view.findViewById(R.id.join_iv);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.itemFL.setOnClickListener(new TaskItemClick(position, node));
        viewHolder.titleTV.setText(node.getTaskTitle());
        viewHolder.descriptionTV.setText(node.getTaskDescription());
        viewHolder.timeTV.setText(node.getStrCreateTime());
        viewHolder.deleteBtn.setOnClickListener(new TaskItemClick(position, node));
        viewHolder.peopleTV.setText(node.getProcessingTeacherName());

//        UserNode userNode = UserInfo.getInstance().getUserByTeacherId(node.getProcessingTeacherId());
//        if(userNode!=null){
//            viewHolder.peopleTV.setText(userNode.getTeacherName());
//        }
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
        FrameLayout itemFL;
        TextView titleTV;
        TextView descriptionTV;
        TextView timeTV;
        TextView peopleTV;
        Button deleteBtn;
        ImageView limitimeIV;
        ImageView joinIV;
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

    private class TaskItemClick implements View.OnClickListener{
        private int position;
        private TaskNode node;

        public TaskItemClick(int position, TaskNode node){
            this.position = position;
            this.node = node;
        }
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.item_fl){
                Intent intent = new Intent();
                intent.putExtra("TaskId", node.getTaskId());
                intent.setClass(context, HomeTaskVerifyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Item", node);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }else if(view.getId()==R.id.delete_btn){
                deleteItem(position);
                HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
                postRequest.postAdminTaskDelete(node.getTaskId(), handler);
            }

        }
    }
}
