package com.huizhi.manage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.material.MaterialFilesActivity;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.FileMenuNode;
import com.huizhi.manage.node.FileNode;
import com.huizhi.manage.node.FolderNode;
import com.huizhi.manage.request.common.FileGetRequest;
import com.huizhi.manage.wiget.view.LineItemView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by CL on 2017/12/13.
 * 资料中心
 */

public class MaterialFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private EditText searchET;
    private Button lastBtn;
    private List<String> parentIds = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_material, container, false);
        activity = this.getActivity();
        initViews();
        getDates("");
        return messageLayout;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initViews(){
        LinearLayout favoriteLL = messageLayout.findViewById(R.id.file_favorite_ll);
        favoriteLL.setOnClickListener(favoriteClickListener);

        searchET = messageLayout.findViewById(R.id.search_et);
        searchET.setOnKeyListener(searchKeyListener);

        lastBtn = messageLayout.findViewById(R.id.last_btn);
        lastBtn.setOnClickListener(lastBtnClick);
    }

    private void getDates(String parentid){
        FileGetRequest getRequest = new FileGetRequest();
//        getRequest.getFileMenu("", handler);
        getRequest.getFileFolderList(parentid, UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

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

    private View.OnClickListener favoriteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(activity, MaterialFilesActivity.class);
            intent.putExtra("Title", "资料收藏");
            intent.putExtra("Favorite", true);
            startActivity(intent);
        }
    };

    private View.OnKeyListener searchKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                //隐藏键盘
                ((InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String str = searchET.getText().toString();
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
        lastBtn.setVisibility(View.GONE);
        Log.i("HuiZhi", "come material search:" + name);
        FileGetRequest getRequest = new FileGetRequest();
        getRequest.getFileSearch(UserInfo.getInstance().getUser().getTeacherId(), name, handler);
    }

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
        LinearLayout filesLL = (LinearLayout)messageLayout.findViewById(R.id.files_ll);
        filesLL.removeAllViews();
        List<FolderNode> folderList = node.getFolders();
        if(folderList!=null){
            LineItemView lineItemV = null;
            for(FolderNode item:folderList){
                lineItemV = new LineItemView(activity);
                lineItemV.setFolderDatas(item, folderInfoUpdate);
                filesLL.addView(lineItemV);
            }
        }
        List<FileNode> fileList = node.getFiles();
        if(fileList!=null){
            LineItemView lineItemV = null;
            for (FileNode item:fileList){
                lineItemV = new LineItemView(activity);
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
            lastBtn.setVisibility(View.VISIBLE);
        }
    };
}
