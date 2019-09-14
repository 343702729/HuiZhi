package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.huizhi.manage.R;

public class ItemMessageView extends LinearLayout {
    private Context context;
    private int type;

    public ItemMessageView(Context context, int type){
        super(context);
        this.context = context;
        this.type = type;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_message, this);
    }
}
