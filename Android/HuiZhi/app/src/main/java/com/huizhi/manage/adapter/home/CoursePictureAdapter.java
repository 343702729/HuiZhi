package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.PictureNode;

import java.util.ArrayList;
import java.util.List;

public class CoursePictureAdapter extends BaseAdapter {
    private Context context;
    private List<PictureNode> nodes;
    private LayoutInflater layoutInflater;

    public CoursePictureAdapter(Context context, List<PictureNode> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateViewsData(List<PictureNode> nodes){
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
        ViewItem viewItem;
        PictureNode node = nodes.get(i);
        if(view==null) {
            viewItem = new ViewItem();
            view = layoutInflater.inflate(R.layout.adapter_course_picture, null);
            viewItem.deleteIV = view.findViewById(R.id.delete_iv);
            view.setTag(viewItem);
        }else{
            viewItem = (ViewItem)view.getTag();
        }
        viewItem.deleteIV.setOnClickListener(new DeleteItemListener(node));
        return view;
    }

    private class ViewItem{
        ImageView deleteIV;
    }

    private class DeleteItemListener implements View.OnClickListener {
        private PictureNode node;

        public DeleteItemListener(PictureNode node){
            this.node = node;
        }

        @Override
        public void onClick(View view) {
            nodes.remove(node);
            notifyDataSetChanged();
        }
    }
}
