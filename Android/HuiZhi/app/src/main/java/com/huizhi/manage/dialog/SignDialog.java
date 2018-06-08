package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.StandardModeAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.node.StudentNode;
import com.huizhi.manage.request.home.HomeCoursePostRequest;
import com.huizhi.manage.util.AppUtil;

import java.util.List;

public class SignDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;
    private String lessonNum;
    private List<StudentNode> allStus;
    private List<StudentNode> signStus;
    private ListView listView;

    public SignDialog(Context context, String lessonNum, List<StudentNode> allStus, List<StudentNode> signStus, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.lessonNum = lessonNum;
        this.allStus = allStus;
        this.signStus = signStus;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    public void initViews(){
        contentView = View.inflate(context, R.layout.dialog_course_sign, null);
        TextView ydCountTV = contentView.findViewById(R.id.yd_count_tv);
        TextView sdCountTV = contentView.findViewById(R.id.sd_count_tv);
        int count = 0;
        for(StudentNode node:allStus){
            if(node.getStuStatus()==1)
                count++;
        }
        if(allStus!=null)
            ydCountTV.setText(String.valueOf(allStus.size()));
        if(signStus!=null)
            sdCountTV.setText(String.valueOf(count));

        listView = contentView.findViewById(R.id.listview);
        StandardModeAdapter adapter = new StandardModeAdapter(context, signStus, true);
        listView.setAdapter(adapter);

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

    public void showView(View parentV){
        popupWindow.setContentView(contentView);
        popupWindow.showAtLocation(parentV, Gravity.BOTTOM, 0, 0);
        popupWindow.update();
    }

    private View.OnClickListener itemBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.cancel_btn){
                popupWindow.dismiss();
            }else if(view.getId()==R.id.sure_btn){
//                popupWindow.dismiss();
                if(signStus==null||signStus.size()==0)
                    return;
                String stuNums = "";
                for(StudentNode node:signStus){
                    stuNums = stuNums + node.getStuNum() + ",";
                }
                HomeCoursePostRequest postRequest = new HomeCoursePostRequest();
                postRequest.postStudentsSignInfo(lessonNum, stuNums, handler);
            }
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS_ONE:
                    popupWindow.dismiss();
                    if(infoUpdate!=null)
                        infoUpdate.update(true);
                    break;
                case Constants.MSG_FAILURE:
                    String mesg = (String)msg.obj;
                    Toast.makeText(context, mesg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
