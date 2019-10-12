package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.node.TeacherTrainingNode;

public class ItemStudyView extends LinearLayout {
    private Context context;
    private TeacherTrainingNode.ObjTrainingItem node;

    public ItemStudyView(Context context, TeacherTrainingNode.ObjTrainingItem node){
        super(context);
        this.context = context;
        this.node = node;
        initViews();
    }

    public void initViews() {
    	if(node==null)
    		return;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_study, this);
		TextView titleTV = findViewById(R.id.train_title_tv);
		titleTV.setText(node.getTitle());
		TextView timeTV = findViewById(R.id.time_tv);
		timeTV.setText(node.getStrCreateTime());
		TextView typeTV = findViewById(R.id.type_tv);
		if(node.getType()==1){
			typeTV.setText("选修");
		}else if(node.getType()==2){
			typeTV.setText("必修");
		}
        ImageView itemIV = findViewById(R.id.item_iv);
        Glide.with(context).load(node.getCoverImg()).into(itemIV);
    }
}
