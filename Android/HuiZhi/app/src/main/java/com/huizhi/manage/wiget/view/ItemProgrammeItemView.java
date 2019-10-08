package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.HomeOperateNode;

public class ItemProgrammeItemView extends LinearLayout {
    private Context context;
    private HomeOperateNode.KnowledgeItem item;
    private boolean isLast;

    public ItemProgrammeItemView(Context context, HomeOperateNode.KnowledgeItem item, boolean isLast){
        super(context);
        this.context = context;
        this.item = item;
        this.isLast = isLast;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_programme_item, this);
        View itemV = findViewById(R.id.item_v);
        TextView titleTV = findViewById(R.id.item_title_tv);
        titleTV.setText(item.getTitle());
        if(isLast)
            itemV.setVisibility(View.GONE);
    }
}
