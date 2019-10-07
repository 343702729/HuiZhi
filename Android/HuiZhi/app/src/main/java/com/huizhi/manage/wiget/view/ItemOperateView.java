package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.node.HomeOperateNode;

public class ItemOperateView extends LinearLayout {
    private Context context;
    private HomeOperateNode.ObjNew item1, item2;

    public ItemOperateView(Context context, HomeOperateNode.ObjNew item1, HomeOperateNode.ObjNew item2){
        super(context);
        this.context = context;
        this.item1 = item1;
        this.item2 = item2;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_course, this);

        ImageView item1IV = findViewById(R.id.item1_iv);
        ImageView item2IV = findViewById(R.id.item2_iv);

        TextView title1TV = findViewById(R.id.title1_tv);
        TextView title2TV = findViewById(R.id.title2_tv);
        if(item1!=null){
            Glide.with(context).load(item1.getThumbImgUrl()).into(item1IV);
            title1TV.setText(item1.getNewsTitle());
        }

        if(item2!=null){
            Glide.with(context).load(item2.getThumbImgUrl()).into(item2IV);
            title2TV.setText(item2.getNewsTitle());
        }

    }
}
