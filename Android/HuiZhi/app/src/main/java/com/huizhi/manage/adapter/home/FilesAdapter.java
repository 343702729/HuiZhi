package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.FileNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/19.
 */

public class FilesAdapter extends BaseAdapter{
    private List<FileNode> nodes;
    private Context context;
    private LayoutInflater layoutInflater;

    public FilesAdapter(Context context, List<FileNode> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateDates(List<FileNode> nodes){
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
        FileNode node = nodes.get(i);
        ViewItem viewItem;
        if(view==null){
            viewItem = new ViewItem();
            view = layoutInflater.inflate(R.layout.adapter_files, null);
            viewItem.imgV = (ImageView)view.findViewById(R.id.icon_iv);
            viewItem.titleTV = view.findViewById(R.id.file_title_tv);
            viewItem.createTimeTV = view.findViewById(R.id.file_createtime_tv);
            view.setTag(viewItem);
        }else{
            viewItem = (ViewItem)view.getTag();
        }
        if(node.getFileType() == 1){
            viewItem.imgV.setBackgroundResource(R.mipmap.icon_file_video);
        }else if(node.getFileType() == 2){
            viewItem.imgV.setBackgroundResource(R.mipmap.icon_file_pic);
        }else if(node.getFileType() == 3){
            viewItem.imgV.setBackgroundResource(R.mipmap.icon_file_word);
        }
        viewItem.titleTV.setText(node.getFileName());
        viewItem.createTimeTV.setText(node.getStrCreateTime());
        return view;
    }

    private class ViewItem{
        ImageView imgV;
        TextView titleTV;
        TextView createTimeTV;
    }
}
