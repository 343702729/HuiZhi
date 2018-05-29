package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.task.TaskUnFinishedActivity;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.util.PictureUtil;

/**
 * Created by CL on 2018/1/16.
 */

public class UserItemView extends LinearLayout {
    private Context context;
    private UserNode userNode;
    private boolean isSelect = false;
    private ImageView selIV;
    private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();

    public UserItemView(Context context){
        super(context);
        this.context = context;
        initViews();
    }

    public UserItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.adapter_person_sel, this);
    }

    public void setDate(UserNode userNode, boolean isSelect, OnClickListener clickListener){
        this.userNode = userNode;
        LinearLayout itemLL = findViewById(R.id.person_item_ll);
        itemLL.setOnClickListener(clickListener);
        ImageView picIV = findViewById(R.id.per_pic_iv);
        TextView nameTV = findViewById(R.id.per_name_tv);
        selIV = findViewById(R.id.per_sel_iv);
        nameTV.setText(userNode.getTeacherName());
        try {
//            asyncBitmapLoader.showPicByVolleyRequest(context, URLData.getUrlFile(UserInfo.getInstance().getUser().getHeadImgUrl()), picIV);
            Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(picIV, AsyncFileUpload.getInstance().getFileUrl(userNode.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                    imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            });
            if(bitmap!=null){
                picIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        this.isSelect = isSelect;
        if(isSelect)
            selIV.setVisibility(VISIBLE);
    }

    public void setSelFlage(boolean flage){
        isSelect = flage;
        if(isSelect)
            selIV.setVisibility(VISIBLE);
        else
            selIV.setVisibility(INVISIBLE);
    }
}
