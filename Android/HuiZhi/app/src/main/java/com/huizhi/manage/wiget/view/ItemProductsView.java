package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.activity.base.HtmlWebActivity;
import com.huizhi.manage.activity.home.HomeNewsInfoActivity;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.URLHtmlData;
import com.huizhi.manage.node.HomeInfoNode;
import com.huizhi.manage.node.TeacherTrainingNode;

public class ItemProductsView extends LinearLayout {
    private Context context;
    private HomeInfoNode.ObjNew item1;
    private HomeInfoNode.ObjNew item2;
    private boolean flag = false;

    public ItemProductsView(Context context, HomeInfoNode.ObjNew item1, HomeInfoNode.ObjNew item2){
        super(context);
        this.context = context;
        this.item1 = item1;
        this.item2 = item2;
        initViews();
    }

    public ItemProductsView(Context context, HomeInfoNode.ObjNew item1, HomeInfoNode.ObjNew item2, boolean flag){
        super(context);
        this.context = context;
        this.item1 = item1;
        this.item2 = item2;
        this.flag = flag;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_course, this);

        ImageView item1IV = findViewById(R.id.item1_iv);
        ImageView item2IV = findViewById(R.id.item2_iv);

		TextView title1TV = findViewById(R.id.title1_tv);
		TextView title2TV = findViewById(R.id.title2_tv);

		View bottomV = findViewById(R.id.bottom_v);

        if(item1!=null){
			Glide.with(context).load(item1.getThumbImgUrl()).into(item1IV);
			title1TV.setText(item1.getNewsTitle());
            item1IV.setOnClickListener(itemClick);
		}

		if(item2!=null){
			Glide.with(context).load(item2.getThumbImgUrl()).into(item2IV);
			title2TV.setText(item2.getNewsTitle());
			item2IV.setOnClickListener(itemClick);
		}

		if(flag)
            bottomV.setVisibility(View.GONE);
    }

    private OnClickListener itemClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, HtmlWebActivity.class);
            switch (view.getId()){
                case R.id.item1_iv:
                    intent.setClass(context, HomeNewsInfoActivity.class);
                    intent.putExtra("Id", item1.getNewsId());
                    context.startActivity(intent);
                    break;
                case R.id.item2_iv:
                    intent.setClass(context, HomeNewsInfoActivity.class);
                    intent.putExtra("Id", item2.getNewsId());
                    context.startActivity(intent);
                    break;
            }
        }
    };
}
