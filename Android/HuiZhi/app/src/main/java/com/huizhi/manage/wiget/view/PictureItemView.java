package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.common.PictureShowActivity;
import com.huizhi.manage.activity.task.TaskUnFinishedActivity;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.TaskAccessory;
import com.huizhi.manage.util.PictureUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by CL on 2018/1/7.
 */

public class PictureItemView extends LinearLayout {
    private Context context;
    private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
    private BaseInfoUpdate infoUpdate;

    public PictureItemView(Context context){
        super(context);
        this.context = context;
        initViews();
    }

    public PictureItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_picture, this);
    }

    public void setDatas(TaskAccessory node, List<TaskAccessory> picList){
        ImageView picIV = findViewById(R.id.picture_iv);
        picIV.setOnClickListener(new PicItemClick(node.getFileUrl(), picList));
        if(node.isLocal()){
            Log.i("HuiZhi", "The pic url:" + node.getFileUrl());
            picIV.setImageBitmap(PictureUtil.getimage(node.getFileLocalUrl()));
        }else {
            String picUrl;
            if(node.getFileUrl().startsWith("http"))
                picUrl = node.getFileUrl();
            else
                picUrl = AsyncFileUpload.getInstance().getDomain() + "/" + node.getFileUrl();
            try {
                asyncBitmapLoader.showPicByVolleyRequest(context, picUrl, picIV);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setDatas(TaskAccessory node, List<TaskAccessory> picList, BaseInfoUpdate infoUpdate){
        this.infoUpdate = infoUpdate;
        ImageView picIV = findViewById(R.id.picture_iv);
        picIV.setOnClickListener(new PicItemClick(node.getFileUrl(), picList));
        Button deleteBtn = findViewById(R.id.delete_pic_btn);
        deleteBtn.setVisibility(VISIBLE);
        deleteBtn.setOnClickListener(new ItemDeleteClick(node, picList));

        if(node.isLocal()){
            Log.i("HuiZhi", "The pic url:" + node.getFileUrl());
            picIV.setImageBitmap(PictureUtil.getimage(node.getFileLocalUrl()));
        }else {
            String picUrl;
            if(node.getFileUrl().startsWith("http"))
                picUrl = node.getFileUrl();
            else
                picUrl = AsyncFileUpload.getInstance().getDomain() + "/" + node.getFileUrl();
            try {
                asyncBitmapLoader.showPicByVolleyRequest(context, picUrl, picIV);
//                Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(picIV, picUrl, new AsyncBitmapLoader.ImageCallBack() {
//                    @Override
//                    public void imageLoad(ImageView imageView, Bitmap bitmap) {
//                        imageView.setImageBitmap(bitmap);
//                    }
//                });
//                if(bitmap!=null){
//                    picIV.setImageBitmap(bitmap);
//                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private class PicItemClick implements OnClickListener{
        private List<TaskAccessory> picList;
        private String picUrl;

        public PicItemClick(String picUrl, List<TaskAccessory> picList){
            this.picUrl = picUrl;
            this.picList = picList;
        }

        @Override
        public void onClick(View view) {
            Log.i("Task", "The pic size:" + picList.size());
            if(TextUtils.isEmpty(picUrl))
                return;
            int index = -1;
            for(int i=0; i<picList.size(); i++){
                if(picUrl.contains(picList.get(i).getFileUrl())) {
                    index = i;
                    break;
                }
            }
            Intent intent = new Intent();
            intent.setClass(context, PictureShowActivity.class);
            intent.putExtra("Index", index);
            intent.putExtra("Pictures", (Serializable) picList);
            context.startActivity(intent);
        }
    }

    private class ItemDeleteClick implements OnClickListener{
        private TaskAccessory node;
        private List<TaskAccessory> picList;

        ItemDeleteClick(TaskAccessory node, List<TaskAccessory> picList){
            this.node = node;
            this.picList = picList;
        }
        @Override
        public void onClick(View view) {

            picList.remove(node);
            infoUpdate.update(null);
        }
    }
}
