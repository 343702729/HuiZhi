package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.StudentNode;

import java.util.ArrayList;
import java.util.List;

public class SignModeAdapter extends BaseAdapter {
    private Context context;
    private List<StudentNode> nodes;
    private List<StudentNode> signNodes = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public SignModeAdapter(Context context, List<StudentNode> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateViewsData(List<StudentNode> nodes){
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        notifyDataSetChanged();
    }

    public List<StudentNode> getSignNodes(){
        return  signNodes;
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
            signNodes.clear();
            for (StudentNode node:nodes){
                if(node.getStuStatus()!=1)
                    signNodes.add(node);
            }
        }else {
            //全不选
            signNodes.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        StudentNode node = nodes.get(i);
        ViewItem viewItem;
        if(view==null){
            viewItem = new ViewItem();
            view = layoutInflater.inflate(R.layout.adapter_course_sign, null);
            viewItem.itemLL = view.findViewById(R.id.item_ll);
            viewItem.signIV = view.findViewById(R.id.sign_cb);
            viewItem.namtTV = view.findViewById(R.id.name_tv);
            viewItem.signSTV = view.findViewById(R.id.sign_status_tv);
            view.setTag(viewItem);
        }else{
            viewItem = (ViewItem)view.getTag();
        }
        if(signNodes.contains(node)){
            viewItem.signIV.setBackgroundResource(R.mipmap.user_check_fc);
        }else {
            viewItem.signIV.setBackgroundResource(R.mipmap.user_check_bg);
        }
        viewItem.itemLL.setOnClickListener(new ItemLineClick(node, viewItem.signIV));
        viewItem.namtTV.setText(node.getStuName());
        if(node.getStuStatus()==-1){
            viewItem.signSTV.setTextColor(context.getResources().getColor(R.color.red));
        }else if(node.getStuStatus()==0){
            viewItem.signSTV.setTextColor(context.getResources().getColor(R.color.dark_gray));
        }else if(node.getStuStatus()==1){
            viewItem.signSTV.setTextColor(context.getResources().getColor(R.color.app_green));
            viewItem.signIV.setBackgroundResource(R.mipmap.user_check_fc);
        }
        viewItem.signSTV.setText(node.getStrStuStatus());
        return view;
    }

    private class ViewItem{
        LinearLayout itemLL;
        ImageView signIV;
        TextView namtTV;
        TextView signSTV;
    }

    private class ItemLineClick implements View.OnClickListener {
        private StudentNode studentNode;
        private ImageView imageView;

        public ItemLineClick(StudentNode studentNode, ImageView imageView){
            this.studentNode = studentNode;
            this.imageView = imageView;
        }

        @Override
        public void onClick(View view) {
            if(studentNode.getStuStatus()==1)
                return;
            if(signNodes.contains(studentNode)){
                signNodes.remove(studentNode);
                imageView.setBackgroundResource(R.mipmap.user_check_bg);
            }else {
                signNodes.add(studentNode);
                imageView.setBackgroundResource(R.mipmap.user_check_fc);
            }
        }
    };
}
