package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.HomeNewsInfoActivity;
import com.huizhi.manage.activity.material.MaterialFilesActivity;
import com.huizhi.manage.activity.task.TaskUnFinishedActivity;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.NewNode;

/**
 * Created by CL on 2018/1/2.
 */

public class LineNewView extends LinearLayout {
    private Context context;
    private String name;
    private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
    private LinearLayout item1_LL, item2_LL;

    public LineNewView(Context context){
        super(context);
        this.context = context;
        initViews();
    }

    public LineNewView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_line_new, this);
        item1_LL = (LinearLayout)findViewById(R.id.new_item1_ll);
        item2_LL = (LinearLayout)findViewById(R.id.new_item2_ll);
    }

    public void setDates(NewNode node1, NewNode node2){
        if(node1!=null){
            item1_LL.setOnClickListener(new ItemClickListener(node1));
            ImageView item1IV = findViewById(R.id.new1_item_iv);
            TextView title1TV = findViewById(R.id.new1_title_tv);
            title1TV.setText(node1.getNewsTitle());
            TextView time1TV = findViewById(R.id.new1_time_tv);
            time1TV.setText(node1.getStrCreateTime1());
            loadImage(item1IV, URLData.getUrlFile(node1.getThumbImgUrl()));
            item1_LL.setVisibility(VISIBLE);
        }

        if(node2!=null){
            item2_LL.setOnClickListener(new ItemClickListener(node2));
            ImageView item2IV = findViewById(R.id.new2_item_iv);
            TextView title2TV = findViewById(R.id.new2_title_tv);
            title2TV.setText(node2.getNewsTitle());
            TextView time2TV = findViewById(R.id.new2_time_tv);
            time2TV.setText(node2.getStrCreateTime1());
            loadImage(item2IV, URLData.getUrlFile(node2.getThumbImgUrl()));
            item2_LL.setVisibility(VISIBLE);
        }
    }

    private void loadImage(ImageView picIV, String picUrl){
        try {
            asyncBitmapLoader.showPicByVolleyRequest(context, picUrl, picIV);
//            Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(picIV, picUrl, new AsyncBitmapLoader.ImageCallBack() {
//                @Override
//                public void imageLoad(ImageView imageView, Bitmap bitmap) {
//                    imageView.setImageBitmap(bitmap);
//                }
//            });
//            if(bitmap!=null){
//                picIV.setImageBitmap(bitmap);
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class ItemClickListener implements OnClickListener {
        private NewNode node;

        ItemClickListener(NewNode node){
            this.node = node;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(context, HomeNewsInfoActivity.class);
            intent.putExtra("Id", node.getNewsId());
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("Item", node);
//            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    };
}
