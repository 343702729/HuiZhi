package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.util.PictureUtil;

public class UserProcessorItemView extends LinearLayout {
    private Context context;
    private UserNode userNode;

    public UserProcessorItemView(Context context, UserNode userNode){
        super(context);
        this.context = context;
        this.userNode = userNode;
        initViews();
    }

    public UserProcessorItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_user_processor, this);
        TextView statusTV = findViewById(R.id.status_tv);
        ImageView userIV = findViewById(R.id.user_iv);
        TextView nameTV = findViewById(R.id.name_tv);

        if(userNode!=null){
            if(userNode.getIsDone()==0){
                statusTV.setTextColor(context.getResources().getColor(R.color.light_red));
            }else if(userNode.getIsDone()==1){
                statusTV.setTextColor(context.getResources().getColor(R.color.text_light_blue));
            }
            statusTV.setText(userNode.getStrIsDone());
            nameTV.setText(userNode.getTeacherName());

            try {
                AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
                Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(userIV, userNode.getFullHeadImgUrlThumb(), new AsyncBitmapLoader.ImageCallBack() {
                    @Override
                    public void imageLoad(ImageView imageView, Bitmap bitmap) {
                        imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                    }
                });
                if(bitmap!=null){
                    userIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
