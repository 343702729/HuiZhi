package com.huizhi.manage.activity.home.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.util.AppUtil;

/**
 * Created by CL on 2017/12/16.
 * 创建任务
 */

public class HomeTaskAllotActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_allot);
        initViews();
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));
        //日常任务模板
        LinearLayout rcLL = (LinearLayout)findViewById(R.id.task_rc);
        rcLL.setOnClickListener(itemLLClick);
        //系统任务
        LinearLayout xtLL = (LinearLayout)findViewById(R.id.task_xt);
        xtLL.setOnClickListener(itemLLClick);
        //自定义任务
        LinearLayout zdyLL = (LinearLayout)findViewById(R.id.task_zdy);
        zdyLL.setOnClickListener(itemLLClick);
    }

    private View.OnClickListener itemLLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.task_rc:
                    intent = new Intent();
                    intent.setClass(HomeTaskAllotActivity.this, HomeTaskMouldsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.task_xt:
                    intent = new Intent();
                    intent.setClass(HomeTaskAllotActivity.this, HomeTaskSystemMenuActivity.class);
                    startActivity(intent);
                    break;
                case R.id.task_zdy:
                    intent = new Intent();
                    intent.setClass(HomeTaskAllotActivity.this, HomeTaskCustomFoundActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
