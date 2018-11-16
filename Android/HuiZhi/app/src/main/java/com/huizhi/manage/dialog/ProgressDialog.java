package com.huizhi.manage.dialog;

import android.app.Activity;

import com.huizhi.manage.util.DipPxUtil;
import com.huizhi.manage.wiget.progress.KProgressHUD;

public class ProgressDialog {
    private Activity activity;
    private KProgressHUD progressHUD;
    private String content = "加载中...";

    public ProgressDialog(Activity activity){
        this.activity = activity;
        initViews();
    }

    public ProgressDialog(Activity activity, String content){
        this.activity = activity;
        this.content = content;
        initViews();
    }

    private void initViews(){
        progressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(content);
        progressHUD.setSize(DipPxUtil.dip2px(activity, 34), DipPxUtil.dip2px(activity, 34));
    }

    public void showView(){
        if(progressHUD!=null)
            progressHUD.show();
    }

    public void closeView(){
        if(progressHUD!=null)
            progressHUD.dismiss();
    }
}
