package com.huizhi.manage.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.common.PageViewAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.base.ThreadPoolDo;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.AsyncWordLoader;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/7.
 * 图片详情
 */

public class PictureUsualShowActivity extends Activity {
    private List<String> picList;
    private int index = 0;
    private List<View> pageViews;
    private TextView indexTV;
    private int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_picture_show);
        getIntentData();
        initViews();
    }

    private void getIntentData(){
        picList = (List<String>)getIntent().getSerializableExtra("Pictures");
        index = getIntent().getIntExtra("Index", 0);
        type = getIntent().getIntExtra("Type", 0);
        Log.i("Task", "The picture size:" + picList.size() + " the index:" + index);
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        TextView downloadTV = findViewById(R.id.download_tv);
        downloadTV.setOnClickListener(downloadClick);
        if(type==1)
            downloadTV.setVisibility(View.VISIBLE);

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
        for(String item:picList){
            view = inflater.inflate(R.layout.adapter_picture_show, null);
            iv = view.findViewById(R.id.pic_iv);
            try {
                asyncBitmapLoader.showFitPicByVolleyRequest(this, AsyncFileUpload.getInstance().getFileUrl(item), iv);
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
            pageviews.add(view);
        }
    }

    private class PicChangeListener implements ViewPager.OnPageChangeListener{

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

    private View.OnClickListener downloadClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String url = picList.get(index);
            final String filename = url.substring(url.lastIndexOf("/") + 1, url.length());
            Log.i("HuiZhi", "The url:" + url + "  filename:" + filename);
            ThreadPoolDo.getInstance().executeThread(new FileUtil.LoadPictureThread(url, filename, Constants.PATH_DOWNLOAD, downloadUpdate));

        }
    };

    private BaseInfoUpdate downloadUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            Log.i("HuiZhi", "Come into download pic success");
            final String picPath = (String) object;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    File file = new File(picPath);
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(file);
                    intent.setData(uri);
                    sendBroadcast(intent);
                    Toast.makeText(PictureUsualShowActivity.this, "图片下载成功", Toast.LENGTH_SHORT).show();
                }
            });

        }
    };
}
