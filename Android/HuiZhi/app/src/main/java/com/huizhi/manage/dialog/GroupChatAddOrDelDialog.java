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
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.communicate.CommunicateGroupAddOrDelAdapter;
import com.huizhi.manage.adapter.communicate.CommunicateGroupSelAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.request.main.MainRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/2/8.
 */

public class GroupChatAddOrDelDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;
    private TextView titleTV;
    private CommunicateGroupAddOrDelAdapter selAdapter;
    private List<String> ids;
    private boolean isAdd = true; //true:add false:delete

    public GroupChatAddOrDelDialog(Context context, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    public void initViews(){
        contentView = View.inflate(context, R.layout.dialog_group_chat_sel, null);
        ImageButton backBtn = contentView.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(backBtnClick);
        titleTV = contentView.findViewById(R.id.title_tv);
        Button sureBtn = contentView.findViewById(R.id.sure_btn);
        sureBtn.setOnClickListener(sureBtnClick);

        if(popupWindow==null){
            popupWindow = new PopupWindow(context);
            popupWindow.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }
    }

    public void showView(View parentV, List<String> ids, boolean isAdd){
        this.ids = ids;
        this.isAdd = isAdd;
        initListView();
        popupWindow.setContentView(contentView);
        popupWindow.setAnimationStyle(R.style.persn_sel);
        popupWindow.showAtLocation(parentV, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }

    private void initListView(){
        if(ids==null)
            return;
        List<UserNode> nodes = new ArrayList<>();
        for(String id:ids){
            UserNode userNode = UserInfo.getInstance().getUserByTeacherId(id);
            if(userNode!=null)
                nodes.add(userNode);
        }
        ListView listView = contentView.findViewById(R.id.listview);
        if(isAdd) {
            titleTV.setText("选择联系人");
            selAdapter = new CommunicateGroupAddOrDelAdapter(context, UserInfo.getInstance().getTeamUsers(), ids);
        }else {
            titleTV.setText("删除联系人");
            for(UserNode node:nodes){
                if(node==null)
                    continue;
                if(node.getTeacherId().equals(UserInfo.getInstance().getUser().getTeacherId()))
                    nodes.remove(node);
                break;
            }
            selAdapter = new CommunicateGroupAddOrDelAdapter(context, nodes, null);
        }
        listView.setAdapter(selAdapter);
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
