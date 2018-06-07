package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.StudentNode;

import java.util.ArrayList;
import java.util.List;

public class StandardModeAdapter extends BaseAdapter{
    private Context context;
    private List<StudentNode> nodes;
    private LayoutInflater layoutInflater;
    private boolean isSign = false;

    public StandardModeAdapter(Context context, List<StudentNode> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
    }

    public StandardModeAdapter(Context context, List<StudentNode> nodes, boolean isSign){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        this.isSign = isSign;
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateViewsData(List<StudentNode> nodes){
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
        StudentNode node = nodes.get(i);
        ViewItem viewItem;
        if(view==null){
            viewItem = new ViewItem();
            view = layoutInflater.inflate(R.layout.adapter_course_standard, null);
            viewItem.nameTV = view.findViewById(R.id.name_tv);
            viewItem.signSTV = view.findViewById(R.id.sign_status_tv);
            viewItem.publishIV = view.findViewById(R.id.publish_iv);
            view.setTag(viewItem);
        }else{
            viewItem = (ViewItem)view.getTag();
        }
        if(!isSign)
            viewItem.publishIV.setVisibility(View.VISIBLE);
        viewItem.nameTV.setText(node.getStuName());
        if(node.getStuStatus()==-1){
            viewItem.signSTV.setTextColor(context.getResources().getColor(R.color.red));
        }else if(node.getStuStatus()==0){
            viewItem.signSTV.setTextColor(context.getResources().getColor(R.color.dark_gray));
        }else if(node.getStuStatus()==1){
            viewItem.signSTV.setTextColor(context.getResources().getColor(R.color.app_green));
        }
        viewItem.signSTV.setText(node.getStrStuStatus());
        if(node.isPublished()){
            viewItem.publishIV.setImageResource(R.mipmap.icon_publish_do);
        }else {
            viewItem.publishIV.setImageResource(R.mipmap.icon_publish_undo);
        }
        return view;
    }

    private class ViewItem{
        TextView nameTV;
        TextView signSTV;
        ImageView publishIV;
    }
}
