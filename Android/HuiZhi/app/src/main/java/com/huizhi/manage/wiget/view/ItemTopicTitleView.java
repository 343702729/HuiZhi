package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.ViewPagerAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.node.CourseWareCategoryNode;

public class ItemTopicTitleView extends LinearLayout {
    private Context context;
    private boolean flag;
    private CourseWareCategoryNode categoryNode;
//    private String category;
    private BaseInfoUpdate infoUpdate;
    private int index;
    private LinearLayout bgLL;
    private TextView titleTV;
    private TextView typeTV;
    private ItemTopicBody2View bodyView;

    public ItemTopicTitleView(Context context, boolean flag, CourseWareCategoryNode categoryNode, int index, BaseInfoUpdate infoUpdate){
        super(context);
        this.context = context;
        this.flag = flag;
        this.categoryNode = categoryNode;
        this.index = index;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    public View getBodyView() {
        return bodyView;
    }

    public void resetView(){
        if(bodyView!=null)
            bodyView.resetView();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_topic_title, this);
        bgLL = findViewById(R.id.item_bg_ll);
        bgLL.setOnClickListener(itemClick);
        titleTV = findViewById(R.id.item_title_tv);
        typeTV = findViewById(R.id.item_type_tv);
        setStatus(flag);
        createBodyViews();
    }

    private void createBodyViews(){
        String category = "";
        if(categoryNode!=null)
            category = categoryNode.getCategoryId();
        bodyView = new ItemTopicBody2View(context, category);
//        bodyView = new ItemTopicBody3View(context, "fef7a99eeab94cee9023f0668f7b4fa2");
//        if(index==2)
//            bodyView = new ItemTopicBody2View(context);
//        else if(index==3)
//            bodyView = new ItemTopicBody3View(context);
//        else
//            bodyView = new ItemTopicBody1View(context);
    }

    public void setStatus(boolean flag){
        this.flag = flag;
        if(flag){
            titleTV.setTextColor(context.getResources().getColor(R.color.white));
            typeTV.setTextColor(context.getResources().getColor(R.color.white));
            bgLL.setBackgroundColor(context.getResources().getColor(R.color.blue_light));
        }else {
            titleTV.setTextColor(context.getResources().getColor(R.color.dark_gray_light));
            typeTV.setTextColor(context.getResources().getColor(R.color.dark_gray_light));
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