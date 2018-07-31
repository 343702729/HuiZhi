package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.node.TaskNode;

/**
 * Created by CL on 2018/1/12.
 */

public class OperateSelDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;
    private TaskNode taskNode;

    public OperateSelDialog(Context context, BaseInfoUpdate infoUpdate, TaskNode taskNode){
        this.context = context;
        this.infoUpdate = infoUpdate;
        this.taskNode = taskNode;
        initViews();
    }

    private void initViews(){
        contentView = View.inflate(context, R.layout.dialog_operate_sel, null);
        Button okBtn = contentView.findViewById(R.id.verify_ok);
        Button noBtn = contentView.findViewById(R.id.verify_no);
        Button zpBtn = contentView.findViewById(R.id.verify_zp);
        Button deleteBtn = contentView.findViewById(R.id.verify_delete);
        Button editBtn = contentView.findViewById(R.id.verify_edit);
        okBtn.setOnClickListener(itemClickListener);
        noBtn.setOnClickListener(itemClickListener);
        zpBtn.setOnClickListener(itemClickListener);
        deleteBtn.setOnClickListener(itemClickListener);
        editBtn.setOnClickListener(itemClickListener);
        if(taskNode.getTaskStatus()==1){
            okBtn.setVisibility(View.GONE);
            noBtn.setVisibility(View.GONE);
//            zpBtn.setVisibility(View.GONE);
            contentView.findViewById(R.id.view_ok).setVisibility(View.GONE);
            contentView.findViewById(R.id.view_no).setVisibility(View.GONE);
//            contentView.findViewById(R.id.view_zp).setVisibility(View.GONE);
        }
        if(taskNode.getTaskStatus()==1&&taskNode.isForAllTeacher()){
            okBtn.setVisibility(View.VISIBLE);
            contentView.findViewById(R.id.view_ok).setVisibility(View.VISIBLE);
        }
        if(taskNode.getCanChooseApprover()==0){
            zpBtn.setVisibility(View.GONE);
            contentView.findViewById(R.id.view_zp).setVisibility(View.GONE);
        }
        if(taskNode.getTaskStatus()!=1&&taskNode.getCanChooseApprover()==0){
            okBtn.setVisibility(View.VISIBLE);
            contentView.findViewById(R.id.view_ok).setVisibility(View.VISIBLE);
            noBtn.setVisibility(View.GONE);
            contentView.findViewById(R.id.view_no).setVisibility(View.GONE);
        }
        Button cancelBtn = contentView.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(itemClickListener);
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

    private View.OnClickListener itemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            closeView();
            switch (v.getId()) {
                case R.id.verify_ok:
                    infoUpdate.update(1);
                    break;
                case R.id.verify_no:
                    infoUpdate.update(2);
                    break;
                case R.id.verify_delete:
                    infoUpdate.update(3);
                    break;
                case R.id.verify_edit:
                    infoUpdate.update(4);
                    break;
                case R.id.verify_zp:
                    infoUpdate.update(5);
                    break;
                default:
                    break;
            }

        }
    };

    public void closeView(){
        if(popupWindow!=null)
            popupWindow.dismiss();
    }
}
