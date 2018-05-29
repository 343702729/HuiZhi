package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.util.DipPxUtil;

/**
 * Created by CL on 2018/1/27.
 */

public class TaskSortDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private MyPopupWindow popupWindow;
    private View contentView;
    private LinearLayout priorityUpLL, priorityDwLL, createtimeUpLL, createtimeDwLL, completetimeUpLL, completetimeDwLL, limittimeUpLL, limitimeDwLL;
    private Button sortBtn;
    private ImageView sortIV;
    private boolean isShow = false;
    private int priority = -1, createtime = -1, completetime = -1, limittime = -1;

    public TaskSortDialog(Context context, Button sortBtn, ImageView sortIV, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.sortBtn = sortBtn;
        this.sortIV = sortIV;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    private void initViews(){
        contentView = View.inflate(context, R.layout.dialog_task_sort, null);
        priorityUpLL = contentView.findViewById(R.id.time_up_ll);
        priorityUpLL.setOnClickListener(itemLLClick);
        priorityDwLL = contentView.findViewById(R.id.time_down_ll);
        priorityDwLL.setOnClickListener(itemLLClick);
        createtimeUpLL = contentView.findViewById(R.id.status_up_ll);
        createtimeUpLL.setOnClickListener(itemLLClick);
        createtimeDwLL = contentView.findViewById(R.id.status_down_ll);
        createtimeDwLL.setOnClickListener(itemLLClick);
        completetimeUpLL = contentView.findViewById(R.id.level_up_ll);
        completetimeUpLL.setOnClickListener(itemLLClick);
        completetimeDwLL = contentView.findViewById(R.id.level_down_ll);
        completetimeDwLL.setOnClickListener(itemLLClick);
        limittimeUpLL = contentView.findViewById(R.id.limittime_up_ll);
        limittimeUpLL.setOnClickListener(itemLLClick);
        limitimeDwLL = contentView.findViewById(R.id.limittime_down_ll);
        limitimeDwLL.setOnClickListener(itemLLClick);

        Button resetBtn = contentView.findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(itemBtnClick);
        Button sureBtn = contentView.findViewById(R.id.sure_btn);
        sureBtn.setOnClickListener(itemBtnClick);

        if(popupWindow==null){
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            popupWindow = new MyPopupWindow(context);
            popupWindow.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(height - DipPxUtil.dip2px(context, 115));
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setContentView(contentView);
            popupWindow.setOnDismissListener(dialogDismissListener);

        }
    }

    public void showView(View parentV){
        popupWindow.showAtLocation(parentV, Gravity.TOP, 0, DipPxUtil.dip2px(context, 115));
        popupWindow.update();
        isShow = true;
        setParentViewsSel(isShow);
    }

    public void closeView(){
        popupWindow.dismiss();
    }

    public boolean isShow(){
        return isShow;
    }

    private void setParentViewsSel(boolean flage){
        if(flage){
            sortBtn.setTextColor(context.getResources().getColor(R.color.light_blue_d));
            sortIV.setBackgroundResource(R.mipmap.icon_px_2);
        }else{
            sortBtn.setTextColor(context.getResources().getColor(R.color.text_light_black));
            sortIV.setBackgroundResource(R.mipmap.icon_px_1);
        }
    }

    private View.OnClickListener itemBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.sure_btn){
                if(infoUpdate!=null){
                    int[] result = {priority, createtime, completetime, limittime};
                    infoUpdate.update(result);
                    closeView();
                }
            }else if(view.getId()==R.id.reset_btn){
                priority = -1;
                setPriorityUpOrDown(-1);
                createtime = -1;
                setCreatetimeUpOrDown(-1);
                completetime = -1;
                setCompletetimeUpOrDown(-1);
                limittime = -1;
                setLimitCompletetimeUpOrDown(-1);
            }

        }
    };

    private View.OnClickListener itemLLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.time_up_ll){
                priority = 0;
                setPriorityUpOrDown(0);
            }else if(view.getId()==R.id.time_down_ll){
                priority = 1;
                setPriorityUpOrDown(1);
            }else if(view.getId()==R.id.status_up_ll){
                createtime = 0;
                setCreatetimeUpOrDown(0);
            }else if(view.getId()==R.id.status_down_ll){
                createtime = 1;
                setCreatetimeUpOrDown(1);
            }else if(view.getId()==R.id.level_up_ll){
                completetime = 0;
                setCompletetimeUpOrDown(0);
            }else if(view.getId()==R.id.level_down_ll){
                completetime = 1;
                setCompletetimeUpOrDown(1);
            }else if(view.getId()==R.id.limittime_up_ll){
                limittime = 0;
                setLimitCompletetimeUpOrDown(0);
            }else if(view.getId()==R.id.limittime_down_ll){
                limittime = 1;
                setLimitCompletetimeUpOrDown(1);
            }
        }
    };

    private PopupWindow.OnDismissListener dialogDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            isShow = false;
            if(priority == -1 && createtime == -1 && completetime == -1 && limittime == -1){
                setParentViewsSel(isShow);
            }

        }
    };

    private class MyPopupWindow extends PopupWindow{

        MyPopupWindow(Context context){
            super(context);
        }
        @Override
        public void dismiss() {

            super.dismiss();
        }
    }

    private void setPriorityUpOrDown(int type){
        TextView timeUpTV = contentView.findViewById(R.id.time_up_tv);
        ImageView timeUpIV = contentView.findViewById(R.id.time_up_iv);
        TextView timeDwTV = contentView.findViewById(R.id.time_down_tv);
        ImageView timeDwIV = contentView.findViewById(R.id.time_down_iv);
        if(type==0){
            priorityUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            timeUpTV.setTextColor(context.getResources().getColor(R.color.light_blue_d));
            timeUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_fc));

            priorityDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            timeDwTV.setTextColor(context.getResources().getColor(R.color.gray));
            timeDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_bg));
        }else if(type==1){
            priorityUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            timeUpTV.setTextColor(context.getResources().getColor(R.color.gray));
            timeUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_bg));

            priorityDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            timeDwTV.setTextColor(context.getResources().getColor(R.color.light_blue_d));
            timeDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_fc));
        }else if(type==-1){
            priorityUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            timeUpTV.setTextColor(context.getResources().getColor(R.color.gray));
            timeUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_bg));

            priorityDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            timeDwTV.setTextColor(context.getResources().getColor(R.color.gray));
            timeDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_bg));
        }
    }

    private void setCreatetimeUpOrDown(int type){
        TextView statusUpTV = contentView.findViewById(R.id.status_up_tv);
        ImageView statusUpIV = contentView.findViewById(R.id.status_up_iv);
        TextView statusDwTV = contentView.findViewById(R.id.status_down_tv);
        ImageView statusDwIV = contentView.findViewById(R.id.status_down_iv);
        if(type==0){
            createtimeUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusUpTV.setTextColor(context.getResources().getColor(R.color.light_blue_d));
            statusUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_fc));

            createtimeDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            statusDwTV.setTextColor(context.getResources().getColor(R.color.gray));
            statusDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_bg));
        }else if(type==1){
            createtimeUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            statusUpTV.setTextColor(context.getResources().getColor(R.color.gray));
            statusUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_bg));

            createtimeDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusDwTV.setTextColor(context.getResources().getColor(R.color.light_blue_d));
            statusDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_fc));
        }else if(type==-1){
            createtimeUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            statusUpTV.setTextColor(context.getResources().getColor(R.color.gray));
            statusUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_bg));

            createtimeDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            statusDwTV.setTextColor(context.getResources().getColor(R.color.gray));
            statusDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_bg));
        }
    }

    private void setCompletetimeUpOrDown(int type){
        TextView levelUpTV = contentView.findViewById(R.id.level_up_tv);
        ImageView levelUpIV = contentView.findViewById(R.id.level_up_iv);
        TextView levelDwTV = contentView.findViewById(R.id.level_down_tv);
        ImageView levelDwIV = contentView.findViewById(R.id.level_down_iv);
        if(type==0){
            completetimeUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            levelUpTV.setTextColor(context.getResources().getColor(R.color.light_blue_d));
            levelUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_fc));

            completetimeDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            levelDwTV.setTextColor(context.getResources().getColor(R.color.gray));
            levelDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_bg));
        }else if(type==1){
            completetimeUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            levelUpTV.setTextColor(context.getResources().getColor(R.color.gray));
            levelUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_bg));

            completetimeDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            levelDwTV.setTextColor(context.getResources().getColor(R.color.light_blue_d));
            levelDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_fc));
        }else if(type==-1){
            completetimeUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            levelUpTV.setTextColor(context.getResources().getColor(R.color.gray));
            levelUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_bg));

            completetimeDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            levelDwTV.setTextColor(context.getResources().getColor(R.color.gray));
            levelDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_bg));
        }
    }

    private void setLimitCompletetimeUpOrDown(int type){
        TextView limitUpTV = contentView.findViewById(R.id.limittime_up_tv);
        ImageView limitUpIV = contentView.findViewById(R.id.limittime_up_iv);
        TextView limitDwTV = contentView.findViewById(R.id.limittime_down_tv);
        ImageView limitDwIV = contentView.findViewById(R.id.limittime_down_iv);
        if(type==0){
            limittimeUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            limitUpTV.setTextColor(context.getResources().getColor(R.color.light_blue_d));
            limitUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_fc));

            limitimeDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            limitDwTV.setTextColor(context.getResources().getColor(R.color.gray));
            limitDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_bg));
        }else if(type==1){
            limittimeUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            limitUpTV.setTextColor(context.getResources().getColor(R.color.gray));
            limitUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_bg));

            limitimeDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            limitDwTV.setTextColor(context.getResources().getColor(R.color.light_blue_d));
            limitDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_fc));
        }else if(type==-1){
            limittimeUpLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            limitUpTV.setTextColor(context.getResources().getColor(R.color.gray));
            limitUpIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_up_bg));

            limitimeDwLL.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            limitDwTV.setTextColor(context.getResources().getColor(R.color.gray));
            limitDwIV.setBackground(context.getResources().getDrawable(R.mipmap.icon_arrow_down_bg));
        }
    }
}
