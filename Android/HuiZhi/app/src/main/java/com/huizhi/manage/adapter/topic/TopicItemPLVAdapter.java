package com.huizhi.manage.adapter.topic;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.node.CourseWareCategoryNode;

import java.util.ArrayList;
import java.util.List;

public class TopicItemPLVAdapter extends BaseAdapter {
    private Context context;
    private List<CourseWareCategoryNode> nodes = new ArrayList<>();

    public TopicItemPLVAdapter(Context context, List<CourseWareCategoryNode> nodes){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_topic_lv_new, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.titleTV = convertView.findViewById(R.id.title_tv);
            viewHolder.ageTV = convertView.findViewById(R.id.age_tv);
            viewHolder.contentTV = convertView.findViewById(R.id.content_tv);
            viewHolder.enhanceTV = convertView.findViewById(R.id.enhance_tv);
            viewHolder.bgIV = convertView.findViewById(R.id.item_pic_iv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(node.getFullCategoryCover()).into(viewHolder.bgIV);
//        viewHolder.titleTV.setText(node.getCategoryName());
//        if(!TextUtils.isEmpty(node.getTrialAge())) {
//            viewHolder.ageTV.setText(node.getTrialAge());
//            viewHolder.ageTV.setVisibility(View.VISIBLE);
//        }
//        viewHolder.contentTV.setText(node.getStudyDesc());
//        viewHolder.enhanceTV.setText(node.getEnhanceDesc());
        return convertView;
    }

    class ViewHolder {
        TextView titleTV;
        TextView ageTV;
        TextView contentTV;
        TextView enhanceTV;
        ImageView bgIV;
    }
}
