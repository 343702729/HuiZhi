package com.huizhi.manage.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.material.MaterialFilesActivity;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.FileMenuNode;
import com.huizhi.manage.node.FileNode;
import com.huizhi.manage.node.FolderNode;
import com.huizhi.manage.request.common.FileGetRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.view.LineItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/18.
 * 资料中心
 */

public class HomeFileMenuActivity extends Activity {
    private Button lastBtn;
    private EditText searchET;
    private List<String> parentIds = new ArrayList<>();
    private boolean isNext = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_filemenu);

        initViews();
        getDates("");
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(backBtnClick);

        searchET = findViewById(R.id.search_et);
        searchET.setOnKeyListener(searchKeyListener);

        LinearLayout favoriteLL = findViewById(R.id.file_favorite_ll);
        favoriteLL.setOnClickListener(favoriteClickListener);

        lastBtn = findViewById(R.id.last_btn);
        lastBtn.setOnClickListener(lastBtnClick);
    }

    private void getDates(String parentid){
        if(TextUtils.isEmpty(parentid))
            isNext = false;
        else
            isNext = true;
        FileGetRequest getRequest = new FileGetRequest();
//        getRequest.getFileMenu("", handler);
        getRequest.getFileFolderList(parentid, UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    private View.OnClickListener backBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!isNext){
                finish();
                return;
            }
            lastDo();
        }
    };

    private View.OnClickListener lastBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(parentIds==null||parentIds.size()==0){
                lastBtn.setVisibility(View.GONE);
                getDates("");
            }else {
                int size = parentIds.size();
                parentIds.remove(size-1);
                if(size==1){
                    lastBtn.setVisibility(View.GONE);
                    getDates("");
                }else {
                    String parentid = parentIds.get(size-2);
                    getDates(parentid);
                    parentIds.remove(parentid);
                }
            }
        }
    };

    private View.OnKeyListener searchKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                //隐藏键盘
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String str = searchET.getText().toString();
                Log.i("HuiZhi", "The searchkey key:" + str);
                if(TextUtils.isEmpty(str)){
                    getDates("");
                }else {
                    getSearchDates(str);
                }
            }
            return false;
        }
    };

    private void getSearchDates(String name){
        parentIds.clear();
//        lastBtn.setVisibility(View.GONE);
        Log.i("HuiZhi", "come material search:" + name);
        FileGetRequest getRequest = new FileGetRequest();
        getRequest.getFileSearch(UserInfo.getInstance().getUser().getTeacherId(), name, handler);
    }

    private View.OnClickListener favoriteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(HomeFileMenuActivity.this, MaterialFilesActivity.class);
            intent.putExtra("Title", "资料收藏");
            intent.putExtra("Favorite", true);
            startActivity(intent);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj==null)
                        return;
                    FileMenuNode node = (FileMenuNode)msg.obj;
//                    List<FolderNode> nodes = (List<FolderNode>)msg.obj;
                    addFilesLL(node);
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj==null)
                        return;
                    FileMenuNode node1 = (FileMenuNode)msg.obj;
                    addFilesLL(node1);
                    break;
            }
        }
    };

    private void addFilesLL(FileMenuNode node){
        if(node==null)
            return;
        LinearLayout filesLL = (LinearLayout)findViewById(R.id.files_ll);
        filesLL.removeAllViews();
        List<FolderNode> folderList = node.getFolders();
        if(folderList!=null){
            LineItemView lineItemV = null;
            for(FolderNode item:folderList){
                lineItemV = new LineItemView(this);
                lineItemV.setFolderDatas(item, folderInfoUpdate);
                filesLL.addView(lineItemV);
            }
        }
        List<FileNode> fileList = node.getFiles();
        if(fileList!=null){
            LineItemView lineItemV = null;
            for (FileNode item:fileList){
                lineItemV = new LineItemView(this);
                lineItemV.setFileDates(item);
                filesLL.addView(lineItemV);
            }
        }
    }

    private BaseInfoUpdate folderInfoUpdate = new BaseInfoUpdate() {
        @Override
        public void update(Object object) {
            if(object==null)
                return;
            String parentid = (String)object;
            parentIds.add(parentid);

            getDates(parentid);

//            lastBtn.setVisibility(View.VISIBLE);
        }
    };

    private void lastDo(){
        if(parentIds==null||parentIds.size()==0){
            lastBtn.setVisibility(View.GONE);
            getDates("");
        }else {
            int size = parentIds.size();
            parentIds.remove(size-1);
            if(size==1){
                lastBtn.setVisibility(View.GONE);
                getDates("");
            }else {
                String parentid = parentIds.get(size-2);
                getDates(parentid);
                parentIds.remove(parentid);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(!isNext)
                return super.onKeyDown(keyCode, event);
            lastDo();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
