package com.huizhi.manage.activity.material;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.huizhi.manage.activity.common.PictureUsualShowActivity;
import com.huizhi.manage.adapter.common.PageViewAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.node.FileNode;
import com.huizhi.manage.request.common.FileGetRequest;
import com.huizhi.manage.request.common.FilePostRequest;
import com.huizhi.manage.util.AppUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/27.
 * 图片文件浏览
 */

public class MaterialFilePictureInfoActivity extends Activity {
    private FileNode fileNode;
    private String fileId;
    private List<View> pageViews;
    private TextView currentTV;
    private int index = 0;
    private TextView descriptionTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_file_pic_info);

        initDates();
        initViews();
        getDates();
    }

    private void initDates(){
        fileId = getIntent().getStringExtra("FileId");
        Log.i("HuiZhi", "The file id:" + fileId);
//        fileNode = (FileNode)getIntent().getSerializableExtra("Item");
//        System.out.println("The file item type：" + fileNode.getFileType());
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));
    }

    private void getDates(){
        FileGetRequest getRequest = new FileGetRequest();
        getRequest.getFileInfo(fileId, UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    private void initViewpager(){
        List<FileNode> nodes = fileNode.getFileInfos();
        if(nodes==null)
            return;
        currentTV = findViewById(R.id.current_tv);
        TextView totalTV = findViewById(R.id.total_tv);
        totalTV.setText(String.valueOf(nodes.size()));
        ViewPager viewPager = findViewById(R.id.viewpager);
        pageViews = new ArrayList<>();
        addPicViews(pageViews, fileNode.getFileInfos());
        PageViewAdapter picPViewAdapter = new PageViewAdapter(pageViews);
        viewPager.setAdapter(picPViewAdapter);
        viewPager.setOnPageChangeListener(new PicChangeListener());
//        viewPager.setCurrentItem(0);
    }

    private void addPicViews(List<View> pageviews, List<FileNode> nodes){
        if(nodes==null)
            return;
        ImageView iv = null;
        View view = null;
        LayoutInflater inflater = getLayoutInflater();
        AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
        int index = 0;
        List<String> picsUrl = new ArrayList<>();
        for(FileNode node:nodes){
            picsUrl.add(node.getFilePath());
            view = inflater.inflate(R.layout.item_material_small_pic, null);
            view.setOnClickListener(new PicItemClick(index, picsUrl));
            iv = view.findViewById(R.id.picture_iv);
            index++;
            try{
                String imgUrl = AsyncFileUpload.getInstance().getFileUrl(node.getFilePath());
                Log.i("HuiZhi", "The material file pic img url:" + imgUrl);
                asyncBitmapLoader.showPicByVolleyRequest(MaterialFilePictureInfoActivity.this, imgUrl, iv);//URLData.getUrlFile(node.getFilePath())
//                Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(iv, imgUrl, new AsyncBitmapLoader.ImageCallBack() {
//                    @Override
//                    public void imageLoad(ImageView imageView, Bitmap bitmap) {
//                        imageView.setImageBitmap(bitmap);
//                    }
//                });
//                if(bitmap!=null){
//                    iv.setImageBitmap(bitmap);
//                }
            }catch (Exception e){

            }
            pageviews.add(view);
        }
    }

    private class PicItemClick implements View.OnClickListener {
        private List<String> picsUrl;
        private int index;

        PicItemClick(int index, List<String> picsUrl){
            this.picsUrl = picsUrl;
            this.index = index;
        }
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(MaterialFilePictureInfoActivity.this, PictureUsualShowActivity.class);
            intent.putExtra("Index", index);
            intent.putExtra("Type", 1);
            intent.putExtra("Pictures", (Serializable) picsUrl);
            startActivity(intent);
        }
    };

    private View.OnClickListener favoriteBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FilePostRequest postRequest = new FilePostRequest();
            Log.i("HuiZhi", "Come into favorite:" + fileNode.isFavorite());
            if(fileNode.isFavorite()){
                postRequest.postFileUnFavrite(UserInfo.getInstance().getUser().getTeacherId(), fileNode.getFileId(), handler);
            }else {
                postRequest.postFileFavorite(UserInfo.getInstance().getUser().getTeacherId(), fileNode.getFileId(), handler);
            }

        }
    };

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
            currentTV.setText(String.valueOf(index + 1));
            if(fileNode!=null){
                List<FileNode> fileNodes = fileNode.getFileInfos();
                if(fileNodes!=null&&fileNodes.size()>index)
                    descriptionTV.setText(fileNodes.get(index).getDescription());
            }

        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        setFavoriteBg(true);
                        Toast.makeText(MaterialFilePictureInfoActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                    break;
                case Constants.MSG_FAILURE:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(MaterialFilePictureInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj!=null) {
                        fileNode = (FileNode)msg.obj;
                        setViewDate(fileNode);
                    }
                    break;
                case Constants.MSG_SUCCESS_TWO:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        setFavoriteBg(false);
                        Toast.makeText(MaterialFilePictureInfoActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void setViewDate(FileNode node){
        TextView titleTV = findViewById(R.id.info_title_tv);
        TextView timeTV = findViewById(R.id.info_time_tv);
        ImageButton favoriteIB = findViewById(R.id.favorite_ib);
        descriptionTV = findViewById(R.id.description_tv);

        if(node!=null){
            titleTV.setText(node.getFileName());
            timeTV.setText(node.getStrCreateTime());
            favoriteIB.setOnClickListener(favoriteBtnClick);
            List<FileNode> fileNodes = node.getFileInfos();
            if(fileNodes!=null&&fileNodes.size()>0)
                descriptionTV.setText(fileNodes.get(0).getDescription());
            setFavoriteBg(node.isFavorite());
        }
        initViewpager();
    }

    private void setFavoriteBg(boolean flage){
        ImageButton favoriteIB = findViewById(R.id.favorite_ib);
        fileNode.setFavorite(flage);
        if(flage)
            favoriteIB.setBackgroundResource(R.mipmap.icon_favorite_fc);
        else
            favoriteIB.setBackgroundResource(R.mipmap.icon_favorite);
    }
}
