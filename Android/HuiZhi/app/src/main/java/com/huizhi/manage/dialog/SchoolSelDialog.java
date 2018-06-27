package com.huizhi.manage.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.SchoolSelAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.SchoolNode;
import com.huizhi.manage.util.DipPxUtil;

public class SchoolSelDialog {
    private Context context;
    private BaseInfoUpdate infoUpdate;
    private PopupWindow popupWindow;
    private View contentView;
    private SchoolSelAdapter schoolSelAdapter;
    private String schoolId;
    private int marginHeight = 115;

    public SchoolSelDialog(Context context, String schoolId, int height, BaseInfoUpdate infoUpdate){
        this.context = context;
        this.schoolId = schoolId;
        this.marginHeight = height;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    private void initViews(){
        contentView = View.inflate(context, R.layout.dialog_school_sel, null);
        ListView listView = contentView.findViewById(R.id.listview);
        schoolSelAdapter = new SchoolSelAdapter(context, schoolId, UserInfo.getInstance().getUser().getSchools());
        listView.setAdapter(schoolSelAdapter);
        listView.setOnItemClickListener(schoolItemClick);

        if(popupWindow==null){
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int height = wm.getDefaultDisplay().getHeight();
            popupWindow = new PopupWindow(context);
            popupWindow.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(height - DipPxUtil.dip2px(context, marginHeight));
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setContentView(contentView);
//            popupWindow.setOnDismissListener(dialogDismissListener);

        }
    }

    public void showView(View parentV){
        popupWindow.showAtLocation(parentV, Gravity.TOP, 0, DipPxUtil.dip2px(context, marginHeight));
        popupWindow.update();
    }

    private AdapterView.OnItemClickListener schoolItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SchoolNode node = (SchoolNode)schoolSelAdapter.getItem(i);
            Log.i("HuiZhi", "The school item select:" + node.getSchoolName());
            if(node.getSchoolId().equals(schoolId)) {
                popupWindow.dismiss();
                return;
            }
            if(infoUpdate!=null) {
                infoUpdate.update(node);
                popupWindow.dismiss();
            }
        }
    };
}
