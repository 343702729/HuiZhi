package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;

public class ItemNewsView extends LinearLayout {
    private Context context;
    private boolean flag;

    public ItemNewsView(Context context, boolean flag){
        super(context);
        this.context = context;
        this.flag = flag;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_news, this);
        ImageView item1IV = findViewById(R.id.item1_iv);
        ImageView item2IV = findViewById(R.id.item2_iv);
        ImageView item3IV = findViewById(R.id.item3_iv);
        View lineV = findViewById(R.id.item_line_v);

        if(flag)
            lineV.setVisibility(VISIBLE);

        Glide.with(context).load(R.mipmap.icon_pic_bg).into(item1IV);
        Glide.with(context).load(R.mipmap.icon_pic_bg).into(item2IV);
        Glide.with(context).load(R.mipmap.icon_pic_bg).into(item3IV);
    }
}
