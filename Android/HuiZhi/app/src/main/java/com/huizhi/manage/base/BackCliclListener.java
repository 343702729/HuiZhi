package com.huizhi.manage.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by CL on 2017/12/26.
 */

public class BackCliclListener implements View.OnClickListener {
    private Activity activity;

    public BackCliclListener(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        activity.finish();
    }
}
