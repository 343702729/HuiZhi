package com.huizhi.manage.activity.material;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.AsyncWordLoader;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.FileNode;
import com.huizhi.manage.request.common.FileGetRequest;
import com.huizhi.manage.request.common.FilePostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Created by CL on 2017/12/27.
 * 文档文件浏览
 */

public class MaterialFileWordInfoActivity extends Activity {
    private FileNode fileNode;
    private String fileId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_file_word_info);

        initDates();
        initViews();
        getDates();
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

    private View.OnClickListener itemBtnClick = new View.OnClickListener() {
        private AsyncWordLoader asyncWordLoader = new AsyncWordLoader();
        private FileNode node;
        private View view;
        @Override
        public void onClick(View view) {
            this.view = view;
            if(fileNode==null)
                return;
//            List<FileNode> nodes = fileNode.getFileList();
            List<FileNode> nodes = fileNode.getFileInfos();
            if(nodes==null||nodes.size()==0)
                return;
            node = nodes.get(0);
            if(node==null)
                return;
            if(view.getId()==R.id.yl_ll){
                String filepath = asyncWordLoader.loadWord(MaterialFileWordInfoActivity.this, URLData.getUrlFile(node.getFilePath()), readInfoUpdate);
                openFile(filepath);
            }else if(view.getId()==R.id.bc_ll){
                asyncWordLoader.downloadWord(AsyncFileUpload.getInstance().getFileUrl(node.getFilePath()), downloadInfoUpdate);
            }
        }

        private BaseInfoUpdate readInfoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                if(object==null)
                    return;
                String filepath = (String)object;
                Log.i("HuiZhi", "The word path:" + filepath);
                if(!TextUtils.isEmpty(filepath)){
                    openFile(filepath);
                }
            }
        };

        private BaseInfoUpdate downloadInfoUpdate = new BaseInfoUpdate() {
            @Override
            public void update(Object object) {
                Log.i("HuiZhi", "come into download success");
                handler.sendEmptyMessage(1);
            }
        };

        private void openFile(String filepath){
            if(!TextUtils.isEmpty(filepath)){
                File docFile = new File(filepath);
                Log.i("HuiZhi", "Come into pad open:" + filepath);
                try {
                    FileUtil.openFile(MaterialFileWordInfoActivity.this, docFile);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(MaterialFileWordInfoActivity.this, "文件已保存到本地Download文件夹下", Toast.LENGTH_LONG).show();
                    break;
                case Constants.MSG_SUCCESS:
                    if(msg.obj!=null) {
                        setFavoriteBg(true);
                        Toast.makeText(MaterialFileWordInfoActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
//                        String message = (String)msg.obj;
//                        Toast.makeText(MaterialFileWordInfoActivity.this, message, Toast.LENGTH_LONG).show();
//                        finish();
                    }
                    break;
                case Constants.MSG_FAILURE:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        Toast.makeText(MaterialFileWordInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj!=null) {
                        fileNode = (FileNode)msg.obj;
                        setViewDate(fileNode);
                    }
                    break;
                case Constants.MSG_SUCCESS_TWO:
                    if(msg.obj!=null) {
                        String message = (String)msg.obj;
                        setFavoriteBg(false);
                        Toast.makeText(MaterialFileWordInfoActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private void setViewDate(FileNode node){
        TextView titleTV = findViewById(R.id.info_title_tv);
        TextView timeTV = findViewById(R.id.info_time_tv);
        ImageButton favoriteIB = findViewById(R.id.favorite_ib);
        TextView contentTV = findViewById(R.id.file_content_tv);

        if(node!=null){
            titleTV.setText(node.getFileName());
            timeTV.setText(node.getStrCreateTime());
            setFavoriteBg(node.isFavorite());
            favoriteIB.setOnClickListener(favoriteBtnClick);
            contentTV.setText(node.getDescription());
        }

        LinearLayout ylBtn = findViewById(R.id.yl_ll);
        ylBtn.setOnClickListener(itemBtnClick);

        LinearLayout bcBtn =  findViewById(R.id.bc_ll);
        bcBtn.setOnClickListener(itemBtnClick);
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
