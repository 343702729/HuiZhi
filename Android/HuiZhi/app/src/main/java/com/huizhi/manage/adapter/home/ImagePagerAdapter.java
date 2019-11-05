/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.huizhi.manage.adapter.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.huizhi.manage.activity.base.HtmlWebActivity;
import com.huizhi.manage.activity.home.HomeNewsInfoActivity;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.http.URLHtmlData;
import com.huizhi.manage.node.BannerNode;
import com.huizhi.manage.util.TLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * @Description: 图片适配器
 */ 
public class ImagePagerAdapter extends BaseAdapter {

	private Context context;
	private List<String> imageIdList;
	private List<String> linkUrlArray;
	private List<String> urlTitlesList;
	private int size;
	private boolean isInfiniteLoop;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private AsyncBitmapLoader asyncBitmapLoader;
	private List<BannerNode> bannerNodes;
	private int bannerType = 0;		//1:运营

	public ImagePagerAdapter(Context context, List<String> imageIdList,
                             List<String> urllist, List<String> urlTitlesList, List<BannerNode> bannerNodes) {
		this.context = context;
		this.imageIdList = imageIdList;
		if (imageIdList != null) {
			this.size = imageIdList.size();
		}
		this.linkUrlArray = urllist;
		this.urlTitlesList = urlTitlesList;
		this.bannerNodes = bannerNodes;
		isInfiniteLoop = false;
		asyncBitmapLoader=new AsyncBitmapLoader();
		// 初始化imageLoader 否则会报错
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

	}

	public ImagePagerAdapter(Context context, List<String> imageIdList,
							 List<String> urllist, List<String> urlTitlesList, List<BannerNode> bannerNodes, int bannerType) {
		this.context = context;
		this.imageIdList = imageIdList;
		if (imageIdList != null) {
			this.size = imageIdList.size();
		}
		this.linkUrlArray = urllist;
		this.urlTitlesList = urlTitlesList;
		this.bannerNodes = bannerNodes;
		isInfiniteLoop = false;
		asyncBitmapLoader=new AsyncBitmapLoader();
		// 初始化imageLoader 否则会报错
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		this.bannerType = bannerType;
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = holder.imageView = new ImageView(context);
			holder.imageView
			.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
			holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		String picUrl = (String) this.imageIdList.get(getPosition(position));
		Bitmap bitmap = null;
		try {
			bitmap=asyncBitmapLoader.loadBitmap(holder.imageView, URLData.getUrlFile(picUrl), new AsyncBitmapLoader.ImageCallBack() {

				@Override
				public void imageLoad(ImageView imageView, Bitmap bitmap) {
					// TODO Auto-generated method stub
					imageView.setImageBitmap(bitmap);
				}
			});
		}catch (Exception e){

		}

		if(bitmap == null){  
//			holder.imageView.setImageResource(R.drawable.ic_launcher);
		}else{  
			holder.imageView.setImageBitmap(bitmap);  
		}  

//		imageLoader.displayImage(
//				(String) this.imageIdList.get(getPosition(position)),
//				holder.imageView, options);

		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final BannerNode node = bannerNodes.get(getPosition(position));
				TLog.log("The banner position:" + position);
				String url = linkUrlArray.get(ImagePagerAdapter.this
						.getPosition(position));
//				String title = urlTitlesList.get(ImagePagerAdapter.this
//						.getPosition(position));
				if(node!=null) {
					if(bannerType==1){	//运营
						if (node.getType()==2){
							Intent intent = new Intent(context, HtmlWebActivity.class);
							intent.putExtra("Title", "运营");
							intent.putExtra("Url", URLHtmlData.getOperateSchemeUrl(UserInfo.getInstance().getUser().getTeacherId(), node.getKenowledgeId()));
							context.startActivity(intent);
						}else {
							Intent intent = new Intent(context, HtmlWebActivity.class);
							intent.putExtra("Title", "运营");
							intent.putExtra("Url", URLHtmlData.getOperateNewDetail(UserInfo.getInstance().getUser().getTeacherId(), node.getNewsId()));
							context.startActivity(intent);
						}
					}else {
						if(node.getType()==1){
							Intent intent = new Intent();
							intent.setClass(context, HomeNewsInfoActivity.class);
							intent.putExtra("Id", node.getNewsId());
							context.startActivity(intent);
						}else if (node.getType()==2){
							Intent intent = new Intent(context, HtmlWebActivity.class);
							intent.putExtra("Title", "运营");
							intent.putExtra("Url", URLHtmlData.getOperateNewDetail(UserInfo.getInstance().getUser().getTeacherId(), node.getNewsId()));
							context.startActivity(intent);
						}else if(node.getType()==3){
							Intent intent = new Intent(context, HtmlWebActivity.class);
							intent.putExtra("Title", "教研");
							intent.putExtra("Url", URLHtmlData.getTrainingDetailUrl(UserInfo.getInstance().getUser().getTeacherId(), node.getNewsId()));
							context.startActivity(intent);
						}
					}


				}
//				if(!TextUtils.isEmpty(title))
//					Toast.makeText(context, title, Toast.LENGTH_LONG).show();

			}
		});

		return view;
	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

}
