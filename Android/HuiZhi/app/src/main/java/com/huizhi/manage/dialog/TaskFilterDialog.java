package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.util.DipPxUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/27.
 */

public class TaskFilterDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;
    private Button filterBtn;
    private ImageView filterIV;
    private boolean isShow = false;
    private boolean isAdmin = false;
    private Button priorityBtn1, priorityBtn2, priorityBtn3,priorityBtn4;
    private Button statusBtn1, statusBtn2, statusBtn3, statusBtn4, statusBtn5, statusBtn6;
    private Button limitBtn0, limitBtn1, limitBtn2;
    private Button createtimeBtn1, createtimeBtn2, createtimeBtn3, createtimeBtn4;
    private int priority = 0, status = 0;
    private String isLimit = "", createtime = "";
    private List<Button> userBtns = new ArrayList<>();
    private String userSel = "";

    public TaskFilterDialog(Context context, Button filterBtn, ImageView filterIV, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.filterBtn = filterBtn;
        this.filterIV = filterIV;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    public TaskFilterDialog(Context context, Button filterBtn, ImageView filterIV, boolean isAdmin, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.filterBtn = filterBtn;
        this.filterIV = filterIV;
        this.infoUpdate = infoUpdate;
        this.isAdmin = isAdmin;
        initViews();
    }

    private void initViews(){
        contentView = View.inflate(context, R.layout.dialog_task_filter, null);
        if(isAdmin){
            LinearLayout operateLL = contentView.findViewById(R.id.operate_ll);
            operateLL.setVisibility(View.VISIBLE);
            LinearLayout usersLL = contentView.findViewById(R.id.users_ll);
            setOperatePersons(usersLL);
        }

        priorityBtn1 = contentView.findViewById(R.id.priority_1_btn);
        priorityBtn1.setOnClickListener(priorityItemClick);
        priorityBtn2 = contentView.findViewById(R.id.priority_2_btn);
        priorityBtn2.setOnClickListener(priorityItemClick);
        priorityBtn3 = contentView.findViewById(R.id.priority_3_btn);
        priorityBtn3.setOnClickListener(priorityItemClick);
        priorityBtn4 = contentView.findViewById(R.id.priority_4_btn);
        priorityBtn4.setOnClickListener(priorityItemClick);

        statusBtn1 = contentView.findViewById(R.id.status_1_btn);
        statusBtn1.setOnClickListener(statusItemClick);
        statusBtn2 = contentView.findViewById(R.id.status_2_btn);
        statusBtn2.setOnClickListener(statusItemClick);
        statusBtn3 = contentView.findViewById(R.id.status_3_btn);
        statusBtn3.setOnClickListener(statusItemClick);
        statusBtn4 = contentView.findViewById(R.id.status_4_btn);
        statusBtn4.setOnClickListener(statusItemClick);
        statusBtn5 = contentView.findViewById(R.id.status_5_btn);
        statusBtn5.setOnClickListener(statusItemClick);
        statusBtn6 = contentView.findViewById(R.id.status_6_btn);
        statusBtn6.setOnClickListener(statusItemClick);

        limitBtn0 = contentView.findViewById(R.id.limit_0_btn);
        limitBtn0.setOnClickListener(limitItemClick);
        limitBtn1 = contentView.findViewById(R.id.limit_1_btn);
        limitBtn1.setOnClickListener(limitItemClick);
        limitBtn2 = contentView.findViewById(R.id.limit_2_btn);
        limitBtn2.setOnClickListener(limitItemClick);

        createtimeBtn1 = contentView.findViewById(R.id.createtime_1_btn);
        createtimeBtn1.setOnClickListener(createtimeItemClick);
        createtimeBtn2 = contentView.findViewById(R.id.createtime_2_btn);
        createtimeBtn2.setOnClickListener(createtimeItemClick);
        createtimeBtn3 = contentView.findViewById(R.id.createtime_3_btn);
        createtimeBtn3.setOnClickListener(createtimeItemClick);
        createtimeBtn4 = contentView.findViewById(R.id.createtime_4_btn);
        createtimeBtn4.setOnClickListener(createtimeItemClick);

        Button resetBtn = contentView.findViewById(R.id.reset_btn);
        resetBtn.setOnClickListener(itemBtnClick);
        Button sureBtn = contentView.findViewById(R.id.sure_btn);
        sureBtn.setOnClickListener(itemBtnClick);

        if(popupWindow==null){
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            popupWindow = new PopupWindow(context);
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
            filterBtn.setTextColor(context.getResources().getColor(R.color.light_blue_d));
            filterIV.setBackgroundResource(R.mipmap.icon_sx_2);
        }else{
            filterBtn.setTextColor(context.getResources().getColor(R.color.text_light_black));
            filterIV.setBackgroundResource(R.mipmap.icon_sx_1);
        }
    }

    private PopupWindow.OnDismissListener dialogDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            isShow = false;
//            priority, status, isLimit, createtime, userSel
            if(priority==0 && status==0 && TextUtils.isEmpty(isLimit) && TextUtils.isEmpty(createtime) && TextUtils.isEmpty(userSel)){
                setParentViewsSel(isShow);
            }
        }
    };

    private View.OnClickListener itemBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.sure_btn){
                if(infoUpdate!=null){
                    Object[] result = {priority, status, isLimit, createtime, userSel};
                    infoUpdate.update(result);
                    closeView();
                }
            }else if(view.getId()==R.id.reset_btn){
                setPriorityItem(1);
                setStatusItem(1);
                setLimitItem(0);
                setCreateTimeItem(1);
                resetOperatePersn();
            }
        }
    };

    private View.OnClickListener priorityItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.priority_1_btn){
                setPriorityItem(1);
            }else if(view.getId()==R.id.priority_2_btn){
                setPriorityItem(2);
            }else if(view.getId()==R.id.priority_3_btn){
                setPriorityItem(3);
            }else if(view.getId()==R.id.priority_4_btn){
                setPriorityItem(4);
            }
        }
    };

    private View.OnClickListener statusItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.status_1_btn){
                setStatusItem(1);
            }else if(view.getId()==R.id.status_2_btn){
                setStatusItem(2);
            }else if(view.getId()==R.id.status_3_btn){
                setStatusItem(3);
            }else if(view.getId()==R.id.status_4_btn){
                setStatusItem(4);
            }else if(view.getId()==R.id.status_5_btn){
                setStatusItem(5);
            }else if(view.getId()==R.id.status_6_btn){
                setStatusItem(6);
            }
        }
    };

    private View.OnClickListener limitItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.limit_0_btn){
                setLimitItem(0);
            }else if(view.getId()==R.id.limit_1_btn){
                setLimitItem(1);
            }else if(view.getId()==R.id.limit_2_btn){
                setLimitItem(2);
            }
        }
    };

    private View.OnClickListener createtimeItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.createtime_1_btn){
                setCreateTimeItem(1);
            }else if(view.getId()==R.id.createtime_2_btn){
                setCreateTimeItem(2);
            }else if(view.getId()==R.id.createtime_3_btn){
                setCreateTimeItem(3);
            }else if(view.getId()==R.id.createtime_4_btn){
                setCreateTimeItem(4);
            }
        }
    };

    private void setPriorityItem(int type){
        priorityBtn1.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        priorityBtn1.setTextColor(context.getResources().getColor(R.color.gray));
        priorityBtn2.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        priorityBtn2.setTextColor(context.getResources().getColor(R.color.gray));
        priorityBtn3.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        priorityBtn3.setTextColor(context.getResources().getColor(R.color.gray));
        priorityBtn4.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        priorityBtn4.setTextColor(context.getResources().getColor(R.color.gray));

        if(1==type){
            priority = 0;
            priorityBtn1.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            priorityBtn1.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(2==type){
            priority = 1;
            priorityBtn2.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            priorityBtn2.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(3==type){
            priority = 2;
            priorityBtn3.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            priorityBtn3.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(4==type){
            priority = 3;
            priorityBtn4.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            priorityBtn4.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }
    }

    private void setStatusItem(int type){
        statusBtn1.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        statusBtn1.setTextColor(context.getResources().getColor(R.color.gray));
        statusBtn2.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        statusBtn2.setTextColor(context.getResources().getColor(R.color.gray));
        statusBtn3.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        statusBtn3.setTextColor(context.getResources().getColor(R.color.gray));
        statusBtn4.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        statusBtn4.setTextColor(context.getResources().getColor(R.color.gray));
        statusBtn5.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        statusBtn5.setTextColor(context.getResources().getColor(R.color.gray));
        statusBtn6.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        statusBtn6.setTextColor(context.getResources().getColor(R.color.gray));

        if(1==type){
            status = 0;
            statusBtn1.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusBtn1.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(2==type){
            status = 1;
            statusBtn2.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusBtn2.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(3==type){
            status = 2;
            statusBtn3.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusBtn3.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(4==type){
            status = 3;
            statusBtn4.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusBtn4.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(5==type){
            status = 4;
            statusBtn5.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusBtn5.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(6==type){
            status = 5;
            statusBtn6.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            statusBtn6.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }
    }

    private void setLimitItem(int type){
        limitBtn0.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        limitBtn0.setTextColor(context.getResources().getColor(R.color.gray));
        limitBtn1.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        limitBtn1.setTextColor(context.getResources().getColor(R.color.gray));
        limitBtn2.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        limitBtn2.setTextColor(context.getResources().getColor(R.color.gray));

        if(0==type){
            isLimit = "";
            limitBtn0.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            limitBtn0.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(1==type){
            isLimit = "0";
            limitBtn1.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            limitBtn1.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(2==type){
            isLimit = "1";
            limitBtn2.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            limitBtn2.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }
    }

    private void setCreateTimeItem(int type){
        createtimeBtn1.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        createtimeBtn1.setTextColor(context.getResources().getColor(R.color.gray));
        createtimeBtn2.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        createtimeBtn2.setTextColor(context.getResources().getColor(R.color.gray));
        createtimeBtn3.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        createtimeBtn3.setTextColor(context.getResources().getColor(R.color.gray));
        createtimeBtn4.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        createtimeBtn4.setTextColor(context.getResources().getColor(R.color.gray));

        if(1==type){
            createtime = "";
            createtimeBtn1.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            createtimeBtn1.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(2==type){
            createtime = "1";
            createtimeBtn2.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            createtimeBtn2.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(3==type){
            createtime = "2";
            createtimeBtn3.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            createtimeBtn3.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }else if(4==type){
            createtime = "3";
            createtimeBtn4.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            createtimeBtn4.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }
    }

    private void setOperatePersons(LinearLayout parentLL){
        List<UserNode> users = UserInfo.getInstance().getTeamUsers();
        if(users==null)
            return;
        parentLL.addView(createUserFirstView());
        for(UserNode user:users){
            parentLL.addView(createUserItemView(user));
        }
    }

    private View createUserFirstView(){
        Button button = new Button(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DipPxUtil.dip2px(context, 70), DipPxUtil.dip2px(context, 35));
        params.setMargins(0,0, DipPxUtil.dip2px(context, 10), 0);
        button.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
        button.setText("全部");
        button.setTextSize(14);
        button.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        button.setLayoutParams(params);
        UserNode user = new UserNode();
        button.setOnClickListener(new UserItemClick(user));
        userBtns.add(button);
        return button;
    }

    private View createUserItemView(UserNode user){
        Button button = new Button(context);
        button.setTag(user.getTeacherId());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DipPxUtil.dip2px(context, 70), DipPxUtil.dip2px(context, 35));
        params.setMargins(0,0, DipPxUtil.dip2px(context, 10), 0);
        button.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
        button.setText(user.getTeacherName());
        button.setTextSize(12);
        button.setTextColor(context.getResources().getColor(R.color.gray));
        button.setLayoutParams(params);
        button.setOnClickListener(new UserItemClick(user));
        userBtns.add(button);
        return button;
    }

    private class UserItemClick implements View.OnClickListener{
        private UserNode user;

        UserItemClick(UserNode user){
            this.user = user;
        }

        @Override
        public void onClick(View view) {
            userSel = user.getTeacherId();
            Button itemV = (Button)view;
            for (Button item:userBtns){
                item.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
                item.setTextColor(context.getResources().getColor(R.color.gray));
            }
            itemV.setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
            itemV.setTextColor(context.getResources().getColor(R.color.light_blue_d));
        }
    }

    private void resetOperatePersn(){
        if(!isAdmin)
            return;
        userSel = "";
        for (Button item:userBtns){
            item.setBackground(context.getResources().getDrawable(R.drawable.frame_gray));
            item.setTextColor(context.getResources().getColor(R.color.gray));
        }
        userBtns.get(0).setBackground(context.getResources().getDrawable(R.drawable.frame_blue));
        userBtns.get(0).setTextColor(context.getResources().getColor(R.color.light_blue_d));
    }
}
