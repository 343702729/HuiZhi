package com.huizhi.manage.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.util.TLog;
import com.huizhi.manage.wiget.ProgressWebView;

import java.util.HashMap;
import java.util.Map;

public class HtmlWebActivity extends Activity {
    private ProgressWebView webView;
    private String url;
    private String title = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_html);
        title = getIntent().getStringExtra("Title");
        url = getIntent().getStringExtra("Url");
        TLog.log("The url is:" + url);
        initViews();
    }

    private void initViews(){
        ImageButton backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(backBtnClick);

        TextView titleTV = findViewById(R.id.title_tv);
        titleTV.setText(title);
        initWebView();
    }

    private void initWebView(){
        webView =  findViewById(R.id.webview);
        Map<String, String > map = new HashMap<String, String>() ;
        map.put( "LogonUserId" , UserInfo.getInstance().getUser().getTeacherId()) ;
        if(!TextUtils.isEmpty(url))
            webView.loadUrl(url, map);
    }

    private View.OnClickListener backBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (webView.canGoBack()) {
                webView.goBack();// 返回上一页面
            } else {
                finish();
            }
        }
    };

}