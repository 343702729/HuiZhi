package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.UserInfo;

/**
 * Created by CL on 2018/1/12.
 */

public class PictureSelDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;

    public PictureSelDialog(Context context, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    private void initViews(){
        contentView = View.inflate(context, R.layout.dialog_picture_sel, null);
        Button takeBtn = contentView.findViewById(R.id.pic_take_btn);
        takeBtn.setOnClickListener(itemClickListener);
        Button albumBtn = contentView.findViewById(R.id.pic_album_btn);
        albumBtn.setOnClickListener(itemClickListener);
        Button cancelBtn = contentView.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(itemClickListener);
        if(popupWindow==null){
            popupWindow = new PopupWindow(context);
            popupWindow.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
    }

    public void showView(View parentV){
        popupWindow.setContentView(contentView);
        popupWindow.showAtLocation(parentV, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }

    private View.OnClickListener itemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.pic_take_btn:
                    infoUpdate.update(1);
                    break;
                case R.id.pic_album_btn:
                    infoUpdate.update(2);
                    break;
                case R.id.cancel_btn:
//                    closeView();
                    break;
                default:
                    break;
            }
            closeView();
        }
    };

    public void closeView(){
        if(popupWindow!=null)
            popupWindow.dismiss();
    }
}
