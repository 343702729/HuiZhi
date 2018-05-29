package com.huizhi.manage.activity.material;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.FilesAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.FolderNode;
import com.huizhi.manage.node.FileNode;
import com.huizhi.manage.request.common.FileGetRequest;
import com.huizhi.manage.util.AppUtil;

import java.util.List;

/**
 * Created by CL on 2017/12/18.
 * 文件列表
 */

public class MaterialFilesActivity extends Activity{
    private String title;
    private ListView listView;
    private FilesAdapter filesAdapter;
    private boolean isFavorite = false;
    private FolderNode fileMenuNode;
    private TextView sizeTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_files);
        getIntentData();
        initViews();
        getDates();
    }

    private void getIntentData(){
        title = getIntent().getStringExtra("Title");
        isFavorite = getIntent().getBooleanExtra("Favorite", false);
        if(!isFavorite)
            fileMenuNode = (FolderNode)getIntent().getSerializableExtra("Item");
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        TextView titleV = (TextView)findViewById(R.id.files_title_tv);
        if(isFavorite)
            titleV.setText("资料收藏");
        else
            titleV.setText(title);
        sizeTV = findViewById(R.id.file_size_tv);
        listView = (ListView)findViewById(R.id.listview);
        filesAdapter = new FilesAdapter(this, null);
        listView.setAdapter(filesAdapter);
        listView.setOnItemClickListener(fileItemClick);
    }

    private void getDates(){
        FileGetRequest getRequest = new FileGetRequest();
        if(isFavorite)
            getRequest.getFavoriteFiles(UserInfo.getInstance().getUser().getTeacherId(),handler);
        else
            getRequest.getFileList(fileMenuNode.getFolderId(), handler);
    }

    private AdapterView.OnItemClickListener fileItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            FileNode node = (FileNode)listView.getItemAtPosition(position);
            System.out.println("The file type:" + node.getFileType());
            Intent intent = new Intent();
            if(node.getFileType()==1)
                intent.setClass(MaterialFilesActivity.this, MaterialFileVideoInfoActivity.class);
            else if(node.getFileType()==2)
                intent.setClass(MaterialFilesActivity.this, MaterialFilePictureInfoActivity.class);
            else if(node.getFileType()==3)
                intent.setClass(MaterialFilesActivity.this, MaterialFileWordInfoActivity.class);
            Log.i("HuiZhi", "The 1 file id:" + node.getFileId());
            intent.putExtra("FileId", node.getFileId());
            Bundle bundle = new Bundle();
            bundle.putSerializable("Item", node);
            intent.putExtras(bundle);
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
                    List<FileNode> nodes = (List<FileNode>)msg.obj;
                    setDates(nodes);
                    break;
            }
        }
    };

    private void setDates(List<FileNode> nodes){
        sizeTV.setText(String.valueOf(nodes.size()));
        filesAdapter.updateDates(nodes);
    }
}
