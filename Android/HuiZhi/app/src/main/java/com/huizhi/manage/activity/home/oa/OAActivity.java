package com.huizhi.manage.activity.home.oa;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.util.AppUtil;

import java.util.HashMap;
import java.util.Map;

public class OAActivity extends Activity{
    private WebView webView;
    private String url = "";

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
        backBtn.setOnClickListener(new BackCliclListener(this));

        webView = (WebView)findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }else{
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        //加载页面
        System.out.println("WebInfo load web url:" + url);
        Log.i("HuiZhi", "WebInfo load web url:" + url);
        //UserInfo.getInstance().getUser().getTeacherId()
        Map<String, String > map = new HashMap<String, String>() ;
        map.put( "LogonUserId" , UserInfo.getInstance().getUser().getTeacherId()) ;
        webView.loadUrl(url, map);
    }
}
