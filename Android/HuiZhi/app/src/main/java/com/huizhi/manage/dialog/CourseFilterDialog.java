package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.util.DipPxUtil;

public class CourseFilterDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;

    public CourseFilterDialog(Context context, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    private void initViews(){
        contentView = View.inflate(context, R.layout.dialog_course_filter, null);

        if(popupWindow==null){
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            popupWindow = new PopupWindow(context);
            popupWindow.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(height - DipPxUtil.dip2px(context, 125));
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setContentView(contentView);
            popupWindow.setOnDismissListener(dialogDismissListener);

        }
    }

    public void showView(View parentV){
        popupWindow.showAtLocation(parentV, Gravity.TOP, 0, DipPxUtil.dip2px(context, 125));
        popupWindow.update();
    }

    private PopupWindow.OnDismissListener dialogDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {

        }
    };

}
