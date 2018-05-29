package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;

/**
 * Created by CL on 2018/2/6.
 */

public class JudgeDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;
    private TextView titleTV;

    public JudgeDialog(Context context, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    private void initViews(){
        contentView = View.inflate(context, R.layout.dialog_judge, null);
        titleTV = contentView.findViewById(R.id.judge_title_tv);
        Button cancelBtn = contentView.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(itemBtnClick);
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

    public void showView(View parentV, String title){
        titleTV.setText(title);
        popupWindow.setContentView(contentView);
        popupWindow.showAtLocation(parentV, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }

    private View.OnClickListener itemBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
            if(view.getId()==R.id.cancel_btn){

            }else if(view.getId()==R.id.sure_btn){
                if(infoUpdate!=null)
                    infoUpdate.update(true);
            }

        }
    };

}
