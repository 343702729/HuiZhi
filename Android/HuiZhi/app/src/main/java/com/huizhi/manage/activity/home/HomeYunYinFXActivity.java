package com.huizhi.manage.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.util.AppUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CL on 2018/06/23
 * 校区运营分享
 */

public class HomeYunYinFXActivity extends Activity {
    private WebView webView;
    private String url = "";
    private TextView closeBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_yunyin_fx);
        initDates();
        initViews();
    }

    private void initDates(){
        try{
            url = URLData.getUrlHomeYunyin();
        }catch (Exception e){

        }
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
//        backBtn.setOnClickListener(new BackCliclListener(this));
        backBtn.setOnClickListener(backBtnClick);
        closeBtn = findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(new BackCliclListener(this));

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
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                // H5中包含下载链接的话让外部浏览器去处理
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        //加载页面
        Log.i("HuiZhi", "WebInfo load web url:" + url);
        Map<String, String > map = new HashMap<String, String>() ;
        map.put( "LogonUserId" , UserInfo.getInstance().getUser().getTeacherId()) ;
        webView.loadUrl(url, map);
    }


    @Override
    public boolean
    onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();//返回上个页面
            closeBtn.setVisibility(View.VISIBLE);
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
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

}
