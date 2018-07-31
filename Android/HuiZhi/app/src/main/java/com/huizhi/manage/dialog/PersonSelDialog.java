package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.wiget.view.UserItemView;

import java.util.List;

/**
 * Created by CL on 2018/1/10.
 */

public class PersonSelDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;
    private String perSelId;
    private boolean isShowAdmin = false;
    private boolean isShowAll = false;
    private boolean isTaskUsers = true;

    public PersonSelDialog(Context context, String personSelId, BaseInfoUpdate infoUpdate){
        this.context = context;
        perSelId = personSelId;
        Log.i("Task", "The person:" + personSelId);
        this.infoUpdate = infoUpdate;
        initViews();
    }

    public PersonSelDialog(Context context, String personSelId, boolean isShowAdmin, BaseInfoUpdate infoUpdate){
        this.context = context;
        perSelId = personSelId;
        Log.i("Task", "The person:" + personSelId);
        this.isShowAdmin = isShowAdmin;
        this.infoUpdate = infoUpdate;
        isTaskUsers = false;
        initViews();
    }

    public PersonSelDialog(Context context, String personSelId, boolean isShowAdmin, boolean isShowAll, BaseInfoUpdate infoUpdate){
        this.context = context;
        perSelId = personSelId;
        Log.i("Task", "The person:" + personSelId);
        this.isShowAdmin = isShowAdmin;
        this.isShowAll = isShowAll;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    public void initViews(){
        contentView = View.inflate(context, R.layout.dialog_person_sel, null);
        ImageButton backBtn = contentView.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(popupWindow!=null)
                    popupWindow.dismiss();
            }
        });
        ImageView checkIV = contentView.findViewById(R.id.per_sel_iv);
        checkIV.setVisibility(View.INVISIBLE);
        if(isShowAll){
            LinearLayout allLL = contentView.findViewById(R.id.person_all_ll);

            allLL.setOnClickListener(allPersonClick);
            if("1".equals(perSelId)){
                checkIV.setVisibility(View.VISIBLE);
            }
            allLL.setVisibility(View.VISIBLE);
        }
//        List<UserNode> users = UserInfo.getInstance().getTeamUsers();
        List<UserNode> users;
        if(!isTaskUsers)
            users = UserInfo.getInstance().getTeamUsers();
        else
            users = UserInfo.getInstance().getTaskUsers();
        addUserView(users);


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

    private void addUserView(List<UserNode> nodes){
        if(nodes==null)
            return;
        LinearLayout adminsLL = contentView.findViewById(R.id.admins_ll);
        LinearLayout usersLL = contentView.findViewById(R.id.users_ll);
        for(UserNode node:nodes){
            if(node.getType()==1)
                continue;
            UserItemView itemView = new UserItemView(context);
            boolean isSel = false;
            if(!TextUtils.isEmpty(perSelId)&&perSelId.equals(node.getTeacherId()))
                isSel = true;
            itemView.setDate(node, isSel, new UserItemClick(node));
            if(node.isAdmin()){
                adminsLL.addView(itemView);
            }else {
                usersLL.addView(itemView);
            }

        }
        if(isShowAdmin){
            LinearLayout usualLL = contentView.findViewById(R.id.users_usual_ll);
            usualLL.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener allPersonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
            perSelId = "1";
            if(infoUpdate!=null)
                infoUpdate.update(perSelId);
        }
    };

    private class UserItemClick implements View.OnClickListener{
        private UserNode usernode;

        public UserItemClick(UserNode usernode){
            this.usernode = usernode;
        }

        @Override
        public void onClick(View view) {
            Log.i("Home", "Come into user item click");
            popupWindow.dismiss();
            if(infoUpdate!=null)
                infoUpdate.update(usernode.getTeacherId());

        }
    };

}
