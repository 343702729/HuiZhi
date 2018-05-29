package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.MessageNode;
import com.huizhi.manage.node.TaskNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/25.
 */

public class MessageAdapter extends BaseAdapter {
    private Context context;
    private List<MessageNode> nodes;
    private LayoutInflater layoutInflater;

    public MessageAdapter(Context context, List<MessageNode> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateAdapter(List<MessageNode> nodes){
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        MessageNode node = nodes.get(i);
        if(view==null){
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.adapter_message_item, null);
            viewHolder.isreadIV = view.findViewById(R.id.isread_iv);
            viewHolder.titleTV = view.findViewById(R.id.title_tv);
            viewHolder.createtimeTV = view.findViewById(R.id.createtime_tv);
            viewHolder.contentTV = view.findViewById(R.id.content_tv);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.titleTV.setText(node.getTitle());
        viewHolder.createtimeTV.setText(node.getStrCreateTime());
        viewHolder.contentTV.setText(node.getContent());
        if(node.getIsRead()==1){
            viewHolder.isreadIV.setVisibility(View.GONE);
            viewHolder.titleTV.setTextColor(context.getResources().getColor(R.color.gray));
            viewHolder.createtimeTV.setTextColor(context.getResources().getColor(R.color.gray));
            viewHolder.contentTV.setTextColor(context.getResources().getColor(R.color.gray));
        }else {
            viewHolder.isreadIV.setVisibility(View.VISIBLE);
            viewHolder.titleTV.setTextColor(context.getResources().getColor(R.color.light_black));
            viewHolder.createtimeTV.setTextColor(context.getResources().getColor(R.color.dark_gray));
            viewHolder.contentTV.setTextColor(context.getResources().getColor(R.color.dark_gray));
        }
        return view;
    }

    class ViewHolder {
        ImageView isreadIV;
        TextView titleTV;
        TextView createtimeTV;
        TextView contentTV;
    }
}
