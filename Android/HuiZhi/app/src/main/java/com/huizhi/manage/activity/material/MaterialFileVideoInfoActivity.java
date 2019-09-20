package com.huizhi.manage.activity.material;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.ThreadPoolDo;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.FileNode;
import com.huizhi.manage.request.common.FileGetRequest;
import com.huizhi.manage.request.common.FilePostRequest;
import com.huizhi.manage.util.AppUtil;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.rong.imageloader.utils.L;

/**
 * Created by CL on 2017/12/27.
 * 视频文件浏览
 */

public class MaterialFileVideoInfoActivity extends AppCompatActivity {
    private FileNode fileNode;
    private String fileId;
    private JCVideoPlayerStandard videoPlayer;
    private Activity activity;
    private HideToolBarThread hideToolBarThread;
    private boolean flage = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_file_video_info);
        getSupportActionBar().hide();
        initDates();
        initViews();
        getDates();
        hideToolBarThread = new HideToolBarThread();
        ThreadPoolDo.getInstance().executeThread(hideToolBarThread);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("HuiZhi", "Video on Resume");
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            Log.i("HuiZhi", "Come into onBackPressed");
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        flage = false;
        hideToolBarThread.interrupt();
        if(videoPlayer!=null)
            videoPlayer.release();
    }

    private void initDates(){
        fileId = getIntent().getStringExtra("FileId");
//        fileNode = (FileNode)getIntent().getSerializableExtra("Item");
//        System.out.println("The file item type：" + fileNode.getFileType());
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));
    }

    private void getDates(){
        FileGetRequest getRequest = new FileGetRequest();
        getRequest.getFileInfo(fileId, UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    class HideToolBarThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (flage){
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessageDelayed(msg,100);
                try {
                    Thread.sleep(100);
                }catch (Exception e){

                }
//                Log.i("HuiZhi", "Come into hide");
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getSupportActionBar().hide();
        return super.onTouchEvent(event);

    }

    private View.OnClickListener favoriteBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            FilePostRequest postRequest = new FilePostRequest();
//            postRequest.postFileFavorite(UserInfo.getInstance().getUser().getTeacherId(), fileNode.getFileId(), handler);
            FilePostRequest postRequest = new FilePostRequest();
            if(fileNode.isFavorite()){
                postRequest.postFileUnFavrite(UserInfo.getInstance().getUser().getTeacherId(), fileNode.getFileId(), handler);
            }else {
                postRequest.postFileFavorite(UserInfo.getInstance().getUser().getTeacherId(), fileNode.getFileId(), handler);
            }
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        setFavoriteBg(true);
                        Toast.makeText(MaterialFileVideoInfoActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
//                        Toast.makeText(MaterialFileVideoInfoActivity.this, message, Toast.LENGTH_LONG).show();
//                        finish();
                    }
                    break;
                case Constants.MSG_FAILURE:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(MaterialFileVideoInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj!=null) {
                        fileNode = (FileNode)msg.obj;
                        setViewDate();
                    }
                    break;
                case Constants.MSG_SUCCESS_TWO:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        setFavoriteBg(false);
                        Toast.makeText(MaterialFileVideoInfoActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    getSupportActionBar().hide();
                    break;
            }
        }
    };

    class MyUserActionStandard implements JCUserActionStandard {

        @Override
        public void onEvent(int type, String url, int screen, Object... objects) {
            getSupportActionBar().hide();
            switch (type) {
                case JCUserAction.ON_CLICK_START_ICON:
                    Log.i("USER_EVENT", "ON_CLICK_START_ICON" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_START_ERROR:
                    Log.i("USER_EVENT", "ON_CLICK_START_ERROR" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_START_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_CLICK_START_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_PAUSE:
                    Log.i("USER_EVENT", "ON_CLICK_PAUSE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_CLICK_RESUME:
                    Log.i("USER_EVENT", "ON_CLICK_RESUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_AUTO_COMPLETE:
                    Log.i("USER_EVENT", "ON_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_ENTER_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_QUIT_FULLSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_ENTER_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_ENTER_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_QUIT_TINYSCREEN:
                    Log.i("USER_EVENT", "ON_QUIT_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_VOLUME:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_VOLUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserAction.ON_TOUCH_SCREEN_SEEK_POSITION:
                    Log.i("USER_EVENT", "ON_TOUCH_SCREEN_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;

                case JCUserActionStandard.ON_CLICK_START_THUMB:
                    Log.i("USER_EVENT", "ON_CLICK_START_THUMB" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCUserActionStandard.ON_CLICK_BLANK:
                    Log.i("USER_EVENT", "ON_CLICK_BLANK" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                default:
                    Log.i("USER_EVENT", "unknow");
                    break;
            }
            getSupportActionBar().hide();
        }
    }

    private void setViewDate(){
        TextView titleTV = findViewById(R.id.info_title_tv);
        TextView timeTV = findViewById(R.id.info_time_tv);
        ImageButton favoriteIB = findViewById(R.id.favorite_ib);
        TextView descriptionTV = findViewById(R.id.description_tv);

        LinearLayout bodyLL = findViewById(R.id.body_ll);
        bodyLL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                System.out.println("Come into ll touch");
                getSupportActionBar().hide();
                return false;
            }
        });

        videoPlayer = findViewById(R.id.video_player);
        if(fileNode!=null){
            titleTV.setText(fileNode.getFileName());
            timeTV.setText(fileNode.getStrCreateTime());
            favoriteIB.setOnClickListener(favoriteBtnClick);
            descriptionTV.setText(fileNode.getDescription());
            setFavoriteBg(fileNode.isFavorite());
//            List<FileNode> list = fileNode.getFileList();
            List<FileNode> list = fileNode.getFileInfos();
            if(list!=null&&list.size()>0){
                FileNode node = list.get(0);
                if(!TextUtils.isEmpty(node.getFilePath())) {
                    Log.i("HuiZhi", "The video path:" + node.getFilePath());
                    videoPlayer.setUp(URLData.getUrlFile(node.getFilePath()), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "视频播放");
                    JCVideoPlayer.setJcUserAction(new MyUserActionStandard());

                }
            }

        }
    }

    private void setFavoriteBg(boolean flage){
        ImageButton favoriteIB = findViewById(R.id.favorite_ib);
        fileNode.setFavorite(flage);
        if(flage)
            favoriteIB.setBackgroundResource(R.mipmap.icon_favorite_fc);
        else
            favoriteIB.setBackgroundResource(R.mipmap.icon_favorite);
    }
}
