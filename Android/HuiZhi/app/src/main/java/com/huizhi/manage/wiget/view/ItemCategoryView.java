package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;

public class ItemCategoryView extends LinearLayout {
    private Context context;
    private String title;

    public ItemCategoryView(Context context, String title){
        super(context);
        this.context = context;
        this.title = title;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_category, this);

        TextView titleTV = findViewById(R.id.title_tv);
        titleTV.setText(title);

        ImageView item1IV = findViewById(R.id.item1_iv);
        ImageView item2IV = findViewById(R.id.item2_iv);

        Glide.with(context).load(R.mipmap.icon_pic_bg).into(item1IV);
        Glide.with(context).load(R.mipmap.icon_pic_bg).into(item2IV);
    }
}
