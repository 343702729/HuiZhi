package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.node.HomeOperateNode;

import java.util.List;

public class ItemProgrammeView extends LinearLayout {
    private Context context;
    private HomeOperateNode.ObjKnowledge node;

    public ItemProgrammeView(Context context, HomeOperateNode.ObjKnowledge node){
        super(context);
        this.context = context;
        this.node = node;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_programme, this);
        if(node==null)
            return;
        TextView monthTV = findViewById(R.id.month_tv);
        monthTV.setText(node.getMonth());
        List<HomeOperateNode.KnowledgeItem> items = node.getItems();
        if(items==null)
            return;
        LinearLayout itemsLL = findViewById(R.id.items_ll);
        for (int i=0; i<items.size(); i++){
//            ItemProgrammeItemView
            if(i+1==items.size())
                itemsLL.addView(new ItemProgrammeItemView(context, items.get(i), true));
            else
                itemsLL.addView(new ItemProgrammeItemView(context, items.get(i), false));
        }
    }
}
