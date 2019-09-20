package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;

public class ItemStudyView extends LinearLayout {
    private Context context;


    public ItemStudyView(Context context){
        super(context);
        this.context = context;

        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_study, this);
        ImageView itemIV = findViewById(R.id.item_iv);
        Glide.with(context).load(R.mipmap.icon_pic_bg).into(itemIV);
    }
}
