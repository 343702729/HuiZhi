package com.huizhi.manage.wiget;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.main.MainApplication;
import com.huizhi.manage.util.TLog;
import com.huizhi.manage.wiget.util.FileManager;


/**
 * 带进度条的webview
 *
 * @author Jarry
 */
public class ProgressWebView extends WebView {
    private Context context;
    private Activity activity;
    private ProgressBar progressBar;
    private ProgressWebView webView;

    /** 视频全屏参数 */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;

    public ProgressWebView(Context context) {
        super(context);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ProgressWebView getWebView() {
        return webView;
    }

    public void setWebView(ProgressWebView webView) {
        this.webView = webView;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @SuppressWarnings("deprecation")
    private void init(final Context context) {
        this.context = context;

        progressBar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 3, 0, 0);
        progressBar.setLayoutParams(lp);
        this.addView(progressBar);
        setWebChromeClient(new MyWebChromeClient());
        WebSettings webSettings = getSettings();
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 屏幕自适应
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);

        webSettings.setJavaScriptEnabled(true);
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,
                                                    final String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    /**
     * 图片选择回调
     */
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    public static final int SELECT_PIC_BY_TACK_PHOTO = 100;

    class MyWebChromeClient extends WebChromeClient {
        // 配置权限（同样在WebChromeClient中实现）
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                if (progressBar.getVisibility() != View.VISIBLE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);

        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            showOptions();
        }

        // For Android > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            showOptions();
        }

        // For Android > 5.0支持多张上传
        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> uploadMsg,
                                         FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = uploadMsg;
            showOptions();
            return true;
        }

        /*** 视频播放相关的方法 **/

        @Override
        public View getVideoLoadingProgressView() {
            TLog.log("Come into video getVideoLoadingProgressView");
            FrameLayout frameLayout = new FrameLayout(activity);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return frameLayout;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            TLog.log("Come into video onShowCustomView");
            if(webView!=null)
                showCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            TLog.log("Come into video onHideCustomView");
            if(webView!=null)
                hideCustomView();
        }
    }

    private Uri fileUri;
    public static final int TYPE_REQUEST_PERMISSION = 3;
    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_GALLERY = 2;

    /**
     * 包含拍照和相册选择
     */
    public void showOptions() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setOnCancelListener(new ReOnCancelListener());
        alertDialog.setTitle("选择");
        alertDialog.setItems(R.array.options,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (ContextCompat.checkSelfPermission(context,
                                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                // 申请WRITE_EXTERNAL_STORAGE权限
                                ActivityCompat
                                        .requestPermissions(
                                                (Activity) context,
                                                new String[]{Manifest.permission.CAMERA},
                                                TYPE_REQUEST_PERMISSION);
                            } else {
                                toCamera();

                            }
                        } else {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
                            ((Activity) context).startActivityForResult(i,
                                    TYPE_GALLERY);
                        }
                    }
                });
        alertDialog.show();
    }

    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(FileManager.getImgFile(MainApplication.getInstance().getApplicationContext()));
    }

    private class ReOnCancelListener implements
            DialogInterface.OnCancelListener {

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }
            if (mUploadCallbackAboveL != null) {
                mUploadCallbackAboveL.onReceiveValue(null);
                mUploadCallbackAboveL = null;
            }
        }
    }

    // 请求拍照
    public void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android的相机
        // 创建一个文件保存图片
        fileUri = getOutputMediaFileUri();
        Log.d("MainActivity", "fileUri=" + fileUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        ((Activity) context).startActivityForResult(intent, TYPE_CAMERA);
    }

    /**
     * 回调到网页
     *
     * @param isCamera
     * @param uri
     */
    public void onActivityCallBack(boolean isCamera, Uri uri) {
        if (isCamera) {
            uri = fileUri;
        }

        if (mUploadCallbackAboveL != null) {
            Uri[] uris = new Uri[]{uri};
            mUploadCallbackAboveL.onReceiveValue(uris);
            mUploadCallbackAboveL = null;
        } else if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(uri);
            mUploadMessage = null;
        } else {
            Toast.makeText(context, "无法获取数据", Toast.LENGTH_LONG).show();
        }
    }

    /** 视频播放全屏 **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        activity.getWindow().getDecorView();
        FrameLayout decor = (FrameLayout) activity.getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(context);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) activity.getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);
        // 设置竖屏
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 取消全屏
        final WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 设置全屏
     *
     * @param visible
     */
    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /** 全屏容器界面 */
    static class FullscreenHolder extends FrameLayout {
        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }
        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

}
