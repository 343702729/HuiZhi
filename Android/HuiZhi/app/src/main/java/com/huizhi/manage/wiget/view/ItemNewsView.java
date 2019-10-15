package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.node.HomeInfoNode;

public class ItemNewsView extends LinearLayout {
    private Context context;
    private boolean flag;
    private HomeInfoNode.ObjNew item;

    public ItemNewsView(Context context, HomeInfoNode.ObjNew item, boolean flag){
        super(context);
        this.context = context;
        this.item = item;
        this.flag = flag;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_news, this);
        ImageView item1IV = findViewById(R.id.item_iv);
        View lineV = findViewById(R.id.item_line_v);
        TextView contentTV = findViewById(R.id.content_tv);
        TextView timeTV = findViewById(R.id.time_tv);

        if(item==null)
            return;

        if(flag)
            lineV.setVisibility(VISIBLE);
        contentTV.setText(item.getNewsTitle());
        timeTV.setText(item.getStrCreateTime1());
        Glide.with(context).load(item.getThumbImgUrl()).into(item1IV);
    }
}
