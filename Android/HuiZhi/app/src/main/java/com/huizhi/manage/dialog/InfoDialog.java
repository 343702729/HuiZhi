package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;

/**
 * Created by CL on 2018/3/10.
 */

public class InfoDialog {
    private Context context;
    private PopupWindow popupWindow;
    private View contentView;
    private TextView titleTV, contentTV;

    public InfoDialog(Context context){
        this.context = context;
        initViews();
    }

    private void initViews(){
        contentView = View.inflate(context, R.layout.dialog_info, null);
        titleTV = contentView.findViewById(R.id.info_title_tv);
        contentTV = contentView.findViewById(R.id.info_content_tv);
        Button sureBtn = contentView.findViewById(R.id.sure_btn);
        sureBtn.setOnClickListener(itemBtnClick);

        if(popupWindow==null){
            popupWindow = new PopupWindow(context);
            popupWindow.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
    }

    public void showView(View parentV, String title, String info){
        titleTV.setText(title);
        contentTV.setText(info);
        popupWindow.setContentView(contentView);
        popupWindow.showAtLocation(parentV, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }

    private View.OnClickListener itemBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
        }
    };
}
