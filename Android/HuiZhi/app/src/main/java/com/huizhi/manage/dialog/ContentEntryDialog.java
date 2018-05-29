package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;

/**
 * Created by CL on 2018/1/10.
 */

public class ContentEntryDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;
    private EditText contentET;
    private boolean isNotNull = false;
    private String reason;

    public ContentEntryDialog(Context context, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    public ContentEntryDialog(Context context, boolean isNotNull, String reason, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.isNotNull = isNotNull;
        this.reason = reason;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    public void initViews(){
        contentView = View.inflate(context, R.layout.dialog_content_entry, null);
        contentET = contentView.findViewById(R.id.dialog_content_et);
        ImageButton cancelBtn = contentView.findViewById(R.id.back_btn);
        cancelBtn.setOnClickListener(itemBtnClick);
        Button sureBtn = contentView.findViewById(R.id.dialog_sure_btn);
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
        TextView titleTV = contentView.findViewById(R.id.title_tv);
        titleTV.setText(title);
        popupWindow.setContentView(contentView);
        popupWindow.showAtLocation(parentV, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }

    public void showView(View parentV, String content, String title){
        TextView titleTV = contentView.findViewById(R.id.title_tv);
        titleTV.setText(title);
        contentET.setText(content);
        popupWindow.setContentView(contentView);
        popupWindow.showAtLocation(parentV, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }

    private View.OnClickListener itemBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.back_btn:
                    hideKeyBoard();
                    popupWindow.dismiss();
                    break;
                case R.id.dialog_sure_btn:
                    hideKeyBoard();
                    String content = contentET.getText().toString();
                    if(!isNotNull|| !TextUtils.isEmpty(content)){
                        popupWindow.dismiss();

                        infoUpdate.update(content);
                    }else {
                        Toast.makeText(context, reason, Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };

    private void hideKeyBoard(){
        Log.i("HuiZhi", "Come into hide key board");
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(contentET.getWindowToken(), 0);
//        // 获取软键盘的显示状态
//        boolean isOpen=imm.isActive();
//        if(isOpen){
//            // 如果软键盘已经显示，则隐藏，反之则显示
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }
}
