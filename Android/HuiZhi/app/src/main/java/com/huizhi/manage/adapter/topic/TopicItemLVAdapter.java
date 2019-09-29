package com.huizhi.manage.adapter.topic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.CourseWareTypeNode;

import java.util.ArrayList;
import java.util.List;

public class TopicItemLVAdapter extends BaseAdapter {
    private Context context;
    private List<CourseWareTypeNode.ObjTypeItem> nodes;

    public TopicItemLVAdapter(Context context, List<CourseWareTypeNode.ObjTypeItem> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
    }

    public void updateList(List<CourseWareTypeNode.ObjTypeItem> nodes){
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        CourseWareTypeNode.ObjTypeItem node = nodes.get(i);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_topic_lv, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTV = convertView.findViewById(R.id.item_title_tv);
            viewHolder.descTV = convertView.findViewById(R.id.item_desc_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.titleTV.setText(node.getName());
        viewHolder.descTV.setText(node.getDesc());
        return convertView;
    }
    class ViewHolder {
        TextView titleTV;
        TextView descTV;
    }
}
