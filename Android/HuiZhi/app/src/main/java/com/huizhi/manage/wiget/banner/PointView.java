package com.huizhi.manage.wiget.banner;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.util.DipPxUtil;

import java.util.ArrayList;
import java.util.List;


public class PointView {
	private Context context;
	private static final int ItemMargin = 10;
	private List<ImageView> ivs = new ArrayList<ImageView>();
	private int type = 0;

	public PointView(Context context){
		this.context = context;
	}

	public PointView(Context context, int type){
		this.context = context;
		this.type = type;
	}

	public void addViews(ViewGroup group, int size){
		ImageView iv = null;
		for(int i=0; i<size; i++){
			iv = new ImageView(context);
			if(i==0) {
				if(type==0)
					iv.setBackgroundResource(R.mipmap.point_fc);
				else
					iv.setBackgroundResource(R.mipmap.point_1_fc);
			}else {
				if(type==0)
					iv.setBackgroundResource(R.mipmap.point_bg);
				else
					iv.setBackgroundResource(R.mipmap.point_1_bg);
			}
			if(i!=0){
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(DipPxUtil.dip2px(context, ItemMargin), 0, 0, 0);
				iv.setLayoutParams(lp);
			}

			ivs.add(iv);
			group.addView(iv);

		}
	}

	public void setPointSelect(int count){
		if(count>ivs.size())
			return;
		for(int i=0; i<ivs.size(); i++){
			if (count==i){
				if(type==0)
					ivs.get(i).setBackgroundResource(R.mipmap.point_fc);
				else
					ivs.get(i).setBackgroundResource(R.mipmap.point_1_fc);
			}else{
				if(type==0)
					ivs.get(i).setBackgroundResource(R.mipmap.point_bg);
				else
					ivs.get(i).setBackgroundResource(R.mipmap.point_1_bg);
			}
		}
	}
}
