package com.huizhi.manage.adapter.topic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.node.CourseWareCategoryNode;
import com.huizhi.manage.node.CourseWareTypeNode;

import java.util.ArrayList;
import java.util.List;

public class TopicItemGVAdapter extends BaseAdapter {
    private Context context;
    private List<CourseWareCategoryNode> nodes = new ArrayList<>();

    public TopicItemGVAdapter(Context context, List<CourseWareCategoryNode> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
    }

    public void updateList(List<CourseWareCategoryNode> nodes){
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
        CourseWareCategoryNode node = nodes.get(i);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_topic_gv, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.item_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 这里只是模拟，实际开发可能需要加载网络图片，可以使用ImageLoader这样的图片加载框架来异步加载图片
        Glide.with(context).load(node.getFullCategoryCover()).into(viewHolder.imageView);
        return convertView;
    }
    class ViewHolder {
        ImageView imageView;
    }
}
