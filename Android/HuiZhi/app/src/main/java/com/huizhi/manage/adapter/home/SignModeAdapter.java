package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.huizhi.manage.R;
import com.huizhi.manage.node.SignNode;
import com.huizhi.manage.node.StandardNode;

import java.util.ArrayList;
import java.util.List;

public class SignModeAdapter extends BaseAdapter {
    private Context context;
    private List<SignNode> nodes;
    private LayoutInflater layoutInflater;

    public SignModeAdapter(Context context, List<SignNode> nodes){
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

    public void checkAll(boolean flage){
        if(flage){
            //全选
        }else {
            //全不选
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SignNode node = nodes.get(i);
        ViewItem viewItem;
        if(view==null){
            viewItem = new ViewItem();
            view = layoutInflater.inflate(R.layout.adapter_course_sign, null);
            view.setTag(viewItem);
        }else{
            viewItem = (ViewItem)view.getTag();
        }
        return view;
    }

    private class ViewItem{

    }
}
