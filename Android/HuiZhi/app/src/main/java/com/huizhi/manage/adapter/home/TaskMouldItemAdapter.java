package com.huizhi.manage.adapter.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.task.HomeTaskMouldSubFoundActivity;
import com.huizhi.manage.activity.home.task.HomeTaskMouldSubInfoActivity;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.wiget.view.MouldTaskItemView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by CL on 2018/3/7.
 */

public class TaskMouldItemAdapter extends BaseAdapter {
    private Activity context;
    private List<TaskMouldNode> nodes;
    private LayoutInflater layoutInflater;
    private Handler handler;
    private BaseInfoUpdate infoUpdate;

    public TaskMouldItemAdapter(Activity context, List<TaskMouldNode> nodes, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.infoUpdate = infoUpdate;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return nodes.size();
    }

    @Override
    public Object getItem(int i) {
        return nodes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private void deleteItem(int position){
        nodes.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewItem viewItem;
        TaskMouldNode mouldNode = nodes.get(position);
        if(view==null){
            viewItem = new ViewItem();
            view = layoutInflater.inflate(R.layout.item_task_mould_task, null);
            viewItem.titleTV = view.findViewById(R.id.title_tv);
            viewItem.descriptionTV = view.findViewById(R.id.description_tv);
            viewItem.createTimeTV = view.findViewById(R.id.as_time_tv);
            viewItem.assignPerTV = view.findViewById(R.id.as_people_tv);
            viewItem.statusFL = view.findViewById(R.id.status_fl);
            viewItem.deleteBtn = view.findViewById(R.id.delete_btn);
            viewItem.itemFL = view.findViewById(R.id.task_item_fl);
            view.setTag(viewItem);
        }else{
            viewItem = (ViewItem)view.getTag();
        }
        viewItem.titleTV.setText(mouldNode.getTaskTitle());
        viewItem.descriptionTV.setText(mouldNode.getTaskDescription());
        viewItem.createTimeTV.setText(mouldNode.getStrCreateTime());
        setTaskPriority(mouldNode.getPriority(), view);
        UserNode user = UserInfo.getInstance().getUserByTeacherId(mouldNode.getExecuteTeacherId());
        if(user!=null)
            viewItem.assignPerTV.setText(user.getTeacherName());
        viewItem.statusFL.setVisibility(GONE);
        viewItem.deleteBtn.setOnClickListener(new ItemBtnClick(position, mouldNode));
        viewItem.itemFL.setOnClickListener(new TaskItemClick(mouldNode));
        return view;
    }

    private class ViewItem {
        TextView titleTV;
        TextView descriptionTV;
        TextView createTimeTV;
        TextView assignPerTV;
        FrameLayout statusFL;
        Button deleteBtn;
        FrameLayout itemFL;
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

    private class TaskItemClick implements View.OnClickListener {
        private TaskMouldNode node;

        public TaskItemClick(TaskMouldNode node){
            this.node = node;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(context, HomeTaskMouldSubFoundActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Item", node);
            intent.putExtras(bundle);
            context.startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    }

    private class ItemBtnClick implements View.OnClickListener{
        private int position;
        private TaskMouldNode node;

        ItemBtnClick(int position, TaskMouldNode node){
            this.position = position;
            this.node = node;
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.delete_btn){
//                deleteItem(position);
                Log.i("HuiZhi", "The delete 1 node title:" + node.getTaskTitle());
                if(infoUpdate!=null)
                    infoUpdate.update(node);
            }

        }
    }
}
