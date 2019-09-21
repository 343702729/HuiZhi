package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.huizhi.manage.R;
import com.huizhi.manage.wiget.ProgressWebView;

/**
 * 加载web
 */
public class ItemTopicBody1View extends LinearLayout {
    private Context context;
    private ProgressWebView webView;

    public ItemTopicBody1View(Context context){
        super(context);
        this.context = context;

        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_topic_body1, this);
        webView = findViewById(R.id.webview);
        webView.loadUrl("https://www.baidu.com/");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();// 返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
