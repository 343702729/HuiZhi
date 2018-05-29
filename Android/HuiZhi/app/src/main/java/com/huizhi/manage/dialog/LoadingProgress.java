package com.huizhi.manage.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;

/**
 * Created by CL on 2018/2/26.
 */

public class LoadingProgress {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow myPopupW;
    private View contentView;

    public LoadingProgress(Context context, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.infoUpdate =infoUpdate;
        initView();
    }

    private void initView(){
        contentView = View.inflate(context, R.layout.dialog_progress, null);

        if(myPopupW == null) {
            myPopupW = new PopupWindow(context);
            myPopupW.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            myPopupW.setHeight(WindowManager.LayoutParams.MATCH_PARENT);

            myPopupW.setBackgroundDrawable(new BitmapDrawable());
            myPopupW.setFocusable(true);
            myPopupW.setOutsideTouchable(true);
        }
    }

    public void showView(View parentV){
        myPopupW.setContentView(contentView);
        myPopupW.showAtLocation(parentV, Gravity.BOTTOM, 0, 0);
        myPopupW.update();
        ImageView imageView = (ImageView) contentView.findViewById(R.id.loadingImageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
        //		textV = (TextView)contentView.findViewById(R.id.id_tv_loadingmsg);
        //		textV.setText(content);
    }

    public void closeView(){
        if(myPopupW!=null)
            myPopupW.dismiss();
    }
}
