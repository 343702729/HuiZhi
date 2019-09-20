package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;

public class ItemTopicTitle extends LinearLayout {
    private Context context;
    private boolean flag;
    private BaseInfoUpdate infoUpdate;
    private int index;
    private LinearLayout bgLL;
    private TextView titleTV;

    public ItemTopicTitle(Context context, boolean flag, int index, BaseInfoUpdate infoUpdate){
        super(context);
        this.context = context;
        this.flag = flag;
        this.index = index;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_topic_title, this);
        bgLL = findViewById(R.id.item_bg_ll);
        bgLL.setOnClickListener(itemClick);
        titleTV = findViewById(R.id.item_title_tv);
        setStatus(flag);
    }

    public void setStatus(boolean flag){
        this.flag = flag;
        if(flag){
            titleTV.setTextColor(context.getResources().getColor(R.color.white));
            bgLL.setBackgroundColor(context.getResources().getColor(R.color.blue_light));
        }else {
            titleTV.setTextColor(context.getResources().getColor(R.color.dark_gray_light));
            bgLL.setBackgroundColor(context.getResources().getColor(R.color.app_bg));
        }
    }

    private OnClickListener itemClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(infoUpdate!=null)
                infoUpdate.update(index);
        }
    };
}