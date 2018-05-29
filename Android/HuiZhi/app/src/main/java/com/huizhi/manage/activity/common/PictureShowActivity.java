package com.huizhi.manage.activity.common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.common.PageViewAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.TaskAccessory;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.PictureUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/7.
 * 图片详情
 */

public class PictureShowActivity extends Activity {
    private List<TaskAccessory> picList;
    private int index = 0;
    private List<View> pageViews;
    private TextView indexTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_picture_show);
        getIntentData();
        initViews();
    }

    private void getIntentData(){
        picList = (List<TaskAccessory>)getIntent().getSerializableExtra("Pictures");
        index = getIntent().getIntExtra("Index", 0);
        Log.i("Task", "The picture size:" + picList.size() + " the index:" + index);
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        indexTV = (TextView) findViewById(R.id.page_index_tv);
        indexTV.setText(String.valueOf(index + 1));
        ViewPager viewPager = (ViewPager)findViewById(R.id.pic_vp);
        pageViews = new ArrayList<>();
        addPicViews(pageViews);
        Log.i("Task", "page size:" + pageViews.size());
        PageViewAdapter picPViewAdapter = new PageViewAdapter(pageViews);
        viewPager.setAdapter(picPViewAdapter);
        viewPager.setOnPageChangeListener(new PicChangeListener());
        viewPager.setCurrentItem(index);
    }

    private void addPicViews(List<View> pageviews){
        ImageView iv = null;
        View view = null;
        LayoutInflater inflater = getLayoutInflater();
        AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
        for(TaskAccessory item:picList){
            view = inflater.inflate(R.layout.adapter_picture_show, null);
            iv = view.findViewById(R.id.pic_iv);
            if(item.isLocal()){
                Log.i("HuiZhi", "The pic url:" + item.getFileUrl());
                iv.setImageBitmap(PictureUtil.getimage(item.getFileLocalUrl()));
            }else {
                String picUrl;
                if(item.getFileUrl().startsWith("http"))
                    picUrl = item.getFileUrl();
                else
                    picUrl = AsyncFileUpload.getInstance().getDomain() + "/" + item.getFileUrl();
                try {
                    asyncBitmapLoader.showFitPicByVolleyRequest(this, picUrl, iv);
//                    Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(iv, picUrl, new AsyncBitmapLoader.ImageCallBack() {
//                        @Override
//                        public void imageLoad(ImageView imageView, Bitmap bitmap) {
//                            imageView.setImageBitmap(bitmap);
//                        }
//                    });
//                    if(bitmap!=null){
//                        iv.setImageBitmap(bitmap);
//                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            pageviews.add(view);
        }
    }

    private class PicChangeListener implements android.support.v4.view.ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setPointSel(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        private  void setPointSel(int sel){
            index = sel;
            indexTV.setText(String.valueOf(index + 1));
        }
    }
}
