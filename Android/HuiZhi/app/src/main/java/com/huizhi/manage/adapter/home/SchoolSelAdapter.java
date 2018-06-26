package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.SchoolNode;

import java.util.ArrayList;
import java.util.List;

public class SchoolSelAdapter extends BaseAdapter{
    private Context context;
    private List<SchoolNode> nodes;
    private LayoutInflater layoutInflater;
    private String schoolId;

    public SchoolSelAdapter(Context context, String schoolId, List<SchoolNode> nodes){
        this.context = context;
        this.schoolId = schoolId;
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
        SchoolNode node = nodes.get(i);
        ViewItem viewItem;
        if(view==null) {
            viewItem = new ViewItem();
            view = layoutInflater.inflate(R.layout.adapter_school, null);
            viewItem.selIV = view.findViewById(R.id.sel_iv);
            viewItem.nameTV = view.findViewById(R.id.name_tv);
            view.setTag(viewItem);
        }else{
            viewItem = (ViewItem)view.getTag();
        }
        viewItem.nameTV.setText(node.getSchoolName());
        viewItem.selIV.setBackgroundResource(R.mipmap.user_check_bg);
        if(!TextUtils.isEmpty(schoolId)){
            if(schoolId.equals(node.getSchoolId()))
                viewItem.selIV.setBackgroundResource(R.mipmap.user_check_fc);
        }
        return view;
    }

    private class ViewItem{
        ImageView selIV;
        TextView nameTV;
    }
}
