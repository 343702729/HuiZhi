package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.util.DipPxUtil;

public class CourseFilterDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;
    private String status;
    private Button statusAllBtn, statusWqdBtn, statusYqdBtn, statusQjBtn;

    public CourseFilterDialog(Context context, String status, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.infoUpdate = infoUpdate;
        this.status = status;
        initViews();
    }

    private void initViews(){
        contentView = View.inflate(context, R.layout.dialog_course_filter, null);

        statusAllBtn = contentView.findViewById(R.id.status_all_btn);
        statusWqdBtn = contentView.findViewById(R.id.status_wqd_btn);
        statusYqdBtn = contentView.findViewById(R.id.status_yqd_btn);
        statusQjBtn = contentView.findViewById(R.id.status_qj_btn);
        statusAllBtn.setOnClickListener(statusItemClick);
        statusWqdBtn.setOnClickListener(statusItemClick);
        statusYqdBtn.setOnClickListener(statusItemClick);
        statusQjBtn.setOnClickListener(statusItemClick);
        if("0".equals(status)){
            setStatusItem(0);
        }else if("1".equals(status)){
            setStatusItem(1);
        }else if("2".equals(status)){
            setStatusItem(2);
        }else if("3".equals(status)){
            setStatusItem(3);
        }

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

    private View.OnClickListener statusItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.status_all_btn:
                    if("0".equals(status))
                        return;
                    status = "0";
                    setStatusItem(0);
                    break;
                case R.id.status_wqd_btn:
                    if("1".equals(status))
                        return;
                    status = "1";
                    setStatusItem(1);
                    break;
                case R.id.status_yqd_btn:
                    if("2".equals(status))
                        return;
                    status = "2";
                    setStatusItem(2);
                    break;
                case R.id.status_qj_btn:
                    if("3".equals(status))
                        return;
                    status = "3";
                    setStatusItem(3);
                    break;
            }
            if(infoUpdate!=null)
                infoUpdate.update(status);
            popupWindow.dismiss();
        }
    };

    private void setStatusItem(int type){
        statusAllBtn.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        statusAllBtn.setTextColor(context.getResources().getColor(R.color.gray));
        statusWqdBtn.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        statusWqdBtn.setTextColor(context.getResources().getColor(R.color.gray));
        statusYqdBtn.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        statusYqdBtn.setTextColor(context.getResources().getColor(R.color.gray));
        statusQjBtn.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        statusQjBtn.setTextColor(context.getResources().getColor(R.color.gray));
        if(0==type){
            statusAllBtn.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusAllBtn.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(1==type){
            statusWqdBtn.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusWqdBtn.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(2==type){
            statusYqdBtn.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusYqdBtn.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(3==type){
            statusQjBtn.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusQjBtn.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }
    }

}
