package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.CourseNode;
import com.huizhi.manage.node.TaskMouldNode;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends BaseAdapter {
    private Context context;
    private List<CourseNode> nodes;
    private LayoutInflater layoutInflater;

    public CourseAdapter(Context context, List<CourseNode> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateViews(List<CourseNode> nodes){
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
        CourseNode node = nodes.get(i);
        ViewItem viewItem;
        if(view==null){
            viewItem = new ViewItem();
            view = layoutInflater.inflate(R.layout.adapter_course, null);
            viewItem.nameTV = view.findViewById(R.id.name_tv);
            viewItem.timeTV = view.findViewById(R.id.time_tv);
            viewItem.allStuTV = view.findViewById(R.id.all_stu_count_tv);
            viewItem.signStuTV = view.findViewById(R.id.sign_count_tv);
            viewItem.leveStuTV = view.findViewById(R.id.leave_stu_count_tv);
            viewItem.workStuTV = view.findViewById(R.id.work_count_tv);
            viewItem.commentTV = view.findViewById(R.id.comment_count_tv);
            viewItem.completeRateTV = view.findViewById(R.id.complete_rate_tv);
            view.setTag(viewItem);
        }else{
            viewItem = (ViewItem)view.getTag();
        }
        viewItem.nameTV.setText(node.getLessonName());
        viewItem.timeTV.setText(node.getLessonTime());
        viewItem.allStuTV.setText(String.valueOf(node.getAllStuCount()));
        viewItem.signStuTV.setText(String.valueOf(node.getSignedCount()));
        viewItem.leveStuTV.setText(String.valueOf(node.getLeaveStuCount()));
        viewItem.workStuTV.setText(String.valueOf(node.getPublishWorkCount()));
        viewItem.commentTV.setText(String.valueOf(node.getCommentedCount()));
        viewItem.completeRateTV.setText(node.getCompletionRate());
        return view;
    }

    private class ViewItem{
        TextView nameTV;
        TextView timeTV;
        TextView allStuTV;
        TextView signStuTV;
        TextView leveStuTV;
        TextView workStuTV;
        TextView commentTV;
        TextView completeRateTV;
    }
}
