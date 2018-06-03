package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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
            view.setTag(viewItem);
        }else{
            viewItem = (ViewItem)view.getTag();
        }
        return view;
    }

    private class ViewItem{

    }
}
