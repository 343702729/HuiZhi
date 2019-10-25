package com.huizhi.manage.activity.home.oa;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.web.BaseWebChromeClient;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.ProgressWebView;

import java.util.HashMap;
import java.util.Map;

public class OAActivity extends Activity{
    private ProgressWebView webView;
    private String url = "";
    private TextView closeBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_oa);
        initDates();
        initViews();
    }

    private void initDates(){
        url = "http://121.43.164.49:8080/tmis/mobile/index?uid=" + UserInfo.getInstance().getUser().getTeacherNum() + "&code=adsfjkj89siu3i4n-i23iiudsfiasu-iuioeuicuiuc-rbquwiocub";

    }



    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(backBtnClick);
        closeBtn = findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new BackCliclListener(this));

        initWebView();
    }

    private void initWebView(){
        webView = (ProgressWebView) findViewById(R.id.webview);
        Map<String, String > map = new HashMap<String, String>() ;
        map.put( "LogonUserId" , UserInfo.getInstance().getUser().getTeacherId()) ;
        webView.loadUrl(url, map);
    }

    private View.OnClickListener backBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (webView.canGoBack()) {
                webView.goBack();// 返回上一页面
                closeBtn.setVisibility(View.VISIBLE);
            } else {
                finish();
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();// 返回上一页面
                closeBtn.setVisibility(View.VISIBLE);
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
