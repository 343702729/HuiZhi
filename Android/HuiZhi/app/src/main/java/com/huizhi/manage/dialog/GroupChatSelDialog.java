package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.communicate.CommunicateGroupSelAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.UserNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/2/8.
 */

public class GroupChatSelDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;
    private CommunicateGroupSelAdapter selAdapter;
    private List<UserNode> talkUsers, userNodes;

    public GroupChatSelDialog(Context context, List<UserNode> talkUsers, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.infoUpdate = infoUpdate;
        this.talkUsers = talkUsers;
        initViews();
    }

    public void initViews(){
        contentView = View.inflate(context, R.layout.dialog_group_chat_sel, null);
        ImageButton backBtn = contentView.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(backBtnClick);
        Button sureBtn = contentView.findViewById(R.id.sure_btn);
        sureBtn.setOnClickListener(sureBtnClick);
        initListView();
        if(popupWindow==null){
            popupWindow = new PopupWindow(context);
            popupWindow.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
    }

    private void initListView(){
        if(talkUsers==null)
            return;
        userNodes = new ArrayList<>();
        for (UserNode user:talkUsers){
            if(user.getType()!=1){
                userNodes.add(user);
            }
        }
        ListView listView = contentView.findViewById(R.id.listview);
        selAdapter = new CommunicateGroupSelAdapter(context, userNodes);//UserInfo.getInstance().getTeamUsers()
        listView.setAdapter(selAdapter);
    }

    public void showView(View parentV){
        popupWindow.setContentView(contentView);
        popupWindow.setAnimationStyle(R.style.persn_sel);
        popupWindow.showAtLocation(parentV, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }

    private View.OnClickListener backBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
        }
    };

    private View.OnClickListener sureBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            List<String> perSels =  selAdapter.getPersonSelList();
            popupWindow.dismiss();
            if(infoUpdate!=null)
                infoUpdate.update(perSels);

        }
    };
}
