package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;

public class ItemProgrammeView extends LinearLayout {
    private Context context;

    public ItemProgrammeView(Context context){
        super(context);
        this.context = context;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_programme, this);
    }
}
