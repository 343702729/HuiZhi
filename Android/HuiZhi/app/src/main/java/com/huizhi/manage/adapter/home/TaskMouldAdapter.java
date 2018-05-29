package com.huizhi.manage.adapter.home;

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
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.task.HomeTaskMouldFoundActivity;
import com.huizhi.manage.activity.home.task.HomeTaskMouldInfoActivity;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.request.home.HomeTaskPostRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/19.
 */

public class TaskMouldAdapter extends BaseAdapter {
    private Context context;
    private List<TaskMouldNode> nodes;
    private LayoutInflater layoutInflater;
    private Handler handler;
//    private BaseInfoUpdate infoUpdate;

    public TaskMouldAdapter(Context context, List<TaskMouldNode> nodes, Handler handler){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateViews(List<TaskMouldNode> nodes){
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
    public Object getItem(int i) {
        return nodes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private void moveItem(int position, int type){
        //0:up  1:down
        if(type==0){
            if(position==0){
                notifyDataSetChanged();
                return;
            }
            TaskMouldNode a = nodes.get(position-1);
            nodes.set(position-1, nodes.get(position));
            nodes.set(position, a);
            notifyDataSetChanged();
        }else{
            if((position+1)==nodes.size()){
                notifyDataSetChanged();
                return;
            }
            TaskMouldNode a = nodes.get(position+1);
            nodes.set(position+1, nodes.get(position));
            nodes.set(position, a);
            notifyDataSetChanged();
        }
        String ids = "";
        for (TaskMouldNode node:nodes){
            ids = ids + node.getCategoryId() + ",";
        }
        moveItemPosition(ids);
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
            view = layoutInflater.inflate(R.layout.adapter_user_task_mould, null);
            viewItem.itemFL = view.findViewById(R.id.item_fl);
            viewItem.nameTV = view.findViewById(R.id.name_tv);
            viewItem.deleteBtn = view.findViewById(R.id.delete_btn);
            viewItem.moveupBtn = view.findViewById(R.id.move_up_btn);
            viewItem.movedownBtn = view.findViewById(R.id.move_down_btn);
            view.setTag(viewItem);
        }else{
            viewItem = (ViewItem)view.getTag();
        }
        viewItem.itemFL.setOnClickListener(new ItemNodeClick(position, mouldNode));
        viewItem.nameTV.setText(mouldNode.getCategoryName());
        viewItem.deleteBtn.setOnClickListener(new ItemBtnClick(position, mouldNode));
        viewItem.moveupBtn.setOnClickListener(new ItemBtnClick(position, mouldNode));
        viewItem.movedownBtn.setOnClickListener(new ItemBtnClick(position, mouldNode));
        return view;
    }

    private class ViewItem {
        FrameLayout itemFL;
        TextView nameTV;
        Button deleteBtn;
        Button moveupBtn;
        Button movedownBtn;
    }

    private class ItemNodeClick implements View.OnClickListener {
        private int position;
        private TaskMouldNode node;

        ItemNodeClick(int position, TaskMouldNode node){
            this.position = position;
            this.node = node;
        }

        @Override
        public void onClick(View view) {
            Log.i("Task", "Come into itemVG click:" + position);
            Intent intent = new Intent();
            intent.setClass(context, HomeTaskMouldInfoActivity.class);
            intent.putExtra("Id", node.getCategoryId());
//            intent.putExtra("Edit", true);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("Item", node);
//            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    };

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
                deleteItemPost(node.getCategoryId());
                deleteItem(position);
            }else if(view.getId()==R.id.move_up_btn){
                moveItem(position, 0);
            }else if(view.getId()==R.id.move_down_btn){
                moveItem(position, 1);
            }

        }
    }

    private void deleteItemPost(String categoryid){
        HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
        postRequest.postDeleteMould(categoryid, handler);
    }

    private void moveItemPosition(String categoryids){
        HomeTaskPostRequest postRequest = new HomeTaskPostRequest();
        postRequest.postMoveMouldPosition(categoryids, handler);
    }
}
