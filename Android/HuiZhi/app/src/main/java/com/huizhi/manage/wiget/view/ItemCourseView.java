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
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.URLHtmlData;
import com.huizhi.manage.node.TeacherTrainingNode;

public class ItemCourseView extends LinearLayout {
    private Context context;
    private TeacherTrainingNode.ObjTeachingTrainingItem item1;
    private TeacherTrainingNode.ObjTeachingTrainingItem item2;
    private boolean flag = false;

    public ItemCourseView(Context context, TeacherTrainingNode.ObjTeachingTrainingItem item1, TeacherTrainingNode.ObjTeachingTrainingItem item2){
        super(context);
        this.context = context;
        this.item1 = item1;
        this.item2 = item2;
        initViews();
    }

    public ItemCourseView(Context context, TeacherTrainingNode.ObjTeachingTrainingItem item1, TeacherTrainingNode.ObjTeachingTrainingItem item2, boolean flag){
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
			Glide.with(context).load(item1.getCoverImg()).into(item1IV);
			title1TV.setText(item1.getTitle());
            item1IV.setOnClickListener(itemClick);
		}

		if(item2!=null){
			Glide.with(context).load(item2.getCoverImg()).into(item2IV);
			title2TV.setText(item2.getTitle());
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
                    intent.putExtra("Title", item1.getTitle());
                    intent.putExtra("Url", URLHtmlData.getTrainingDetailUrl(UserInfo.getInstance().getUser().getTeacherId(), item1.getTrainingId()));
                    context.startActivity(intent);
                    break;
                case R.id.item2_iv:
                    intent.putExtra("Title", item2.getTitle());
                    intent.putExtra("Url", URLHtmlData.getTrainingDetailUrl(UserInfo.getInstance().getUser().getTeacherId(), item2.getTrainingId()));
                    context.startActivity(intent);
                    break;
            }
        }
    };
}
