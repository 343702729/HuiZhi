package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.huizhi.manage.R;
import com.huizhi.manage.activity.common.PictureUsualShowActivity;
import com.huizhi.manage.base.BitmapCache;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.node.PictureNode;
import com.huizhi.manage.util.PictureUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CoursePictureAdapter extends BaseAdapter {
    private Context context;
    private List<PictureNode> nodes;
    private LayoutInflater layoutInflater;
    private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
    private List<String> picsUrl;
    private ImageLoader mImageLoader;

    public CoursePictureAdapter(Context context, List<PictureNode> nodes){
        this.context = context;
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        getAllPicUrl(nodes);
        RequestQueue queue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(queue, new BitmapCache());
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateViewsData(List<PictureNode> nodes){
        if(nodes==null)
            this.nodes = new ArrayList<>();
        else
            this.nodes = nodes;
        getAllPicUrl(nodes);
        notifyDataSetChanged();
    }

    private void getAllPicUrl(List<PictureNode> nodes){
        if(nodes==null)
            return;
        picsUrl = new ArrayList<>();
        for (PictureNode node:nodes){
            picsUrl.add(AsyncFileUpload.getInstance().getFileUrl(node.getUrl()));
        }
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
            viewItem.pictureIV = view.findViewById(R.id.picture_iv);
            viewItem.deleteIV = view.findViewById(R.id.delete_iv);
            viewItem.pictureIV.setTag(node.getUrl());
            view.setTag(viewItem);
        }else{
            viewItem = (ViewItem)view.getTag();
        }
        if(node.isServer()){
            viewItem.pictureIV.setImageUrl(AsyncFileUpload.getInstance().getFileUrl(node.getThumbImageUrl190()), mImageLoader);
            try{

//                asyncBitmapLoader.showPicByVolleyRequest(context, node.getThumbImageUrl190(), viewItem.pictureIV);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            viewItem.pictureIV.setImageBitmap(PictureUtil.getimage(node.getPath()));
        }
        viewItem.pictureIV.setOnClickListener(new ItemShowListener(node, i));
        viewItem.deleteIV.setOnClickListener(new DeleteItemListener(node));
        return view;
    }

    private class ViewItem{
        NetworkImageView pictureIV;
        ImageView deleteIV;
    }

    private class ItemShowListener implements View.OnClickListener {
        private PictureNode node;
        private int index;

        public ItemShowListener(PictureNode node, int index){
            this.node = node;
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(context, PictureUsualShowActivity.class);
            intent.putExtra("Index", index);
            intent.putExtra("Pictures", (Serializable) picsUrl);
            context.startActivity(intent);
        }
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
