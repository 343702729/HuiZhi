package com.huizhi.manage.activity.home.course;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.ProgressWebView;

import java.util.HashMap;
import java.util.Map;

/**
 * 课件库
 */
public class CoursewareActivity extends Activity{
    private ProgressWebView webView;
    private String url = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_course_ware);
        initDates();
        initViews();
    }

    private void initDates(){
        url = "http://app.huizhiart.com/courseware/category/" + UserInfo.getInstance().getUser().getTeacherNum();

    }



    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        initWebView();
    }

    private void initWebView(){
        webView =  findViewById(R.id.webview);
        Map<String, String > map = new HashMap<String, String>() ;
        map.put( "LogonUserId" , UserInfo.getInstance().getUser().getTeacherId()) ;
        webView.loadUrl(url, map);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();// 返回上一页面
                return true;
            } else {
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ProgressWebView.TYPE_CAMERA) { // 相册选择
                webView.onActivityCallBack(true, null);
            } else if (requestCode == ProgressWebView.TYPE_GALLERY) {// 相册选择
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        webView.onActivityCallBack(false, uri);
                    } else {
                        Toast.makeText(this, "获取数据为空", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    // 权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ProgressWebView.TYPE_REQUEST_PERMISSION) {
            webView.toCamera();// 到相机
        }
    }
}
