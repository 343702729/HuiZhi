package com.huizhi.manage.wiget.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.material.MaterialFileWordInfoActivity;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.AsyncWordLoader;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.TaskAccessory;
import com.huizhi.manage.util.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Created by CL on 2018/1/7.
 */

public class FileItemView extends LinearLayout {
    private Activity context;
    private BaseInfoUpdate infoUpdate;
    private boolean isInfo = false;

    public FileItemView(Activity context){
        super(context);
        this.context = context;
        initViews();
    }

    public FileItemView(Activity context, boolean isInfo){
        super(context);
        this.context = context;
        this.isInfo = isInfo;
        initViews();
    }

    public FileItemView(Activity context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_file, this);
    }

    public void setDatas(TaskAccessory node, List<TaskAccessory> fileList, BaseInfoUpdate infoUpdate){
            this.infoUpdate = infoUpdate;
        TextView nameTV = findViewById(R.id.file_title_tv);
        nameTV.setText(node.getFileName());
        ImageButton deleteIB = findViewById(R.id.delete_ib);
        deleteIB.setOnClickListener(new DeleteItemClick(node, fileList));
        if(!isInfo){
            deleteIB.setVisibility(VISIBLE);
        }else {
            deleteIB.setVisibility(INVISIBLE);
            LinearLayout itemLL = findViewById(R.id.file_item_ll);
            itemLL.setOnClickListener(new ItemLLClick(node));
        }

//        TextView sizeTV = findViewById(R.id.file_size_tv);
//        sizeTV.setText(size);
    }

    private class DeleteItemClick implements OnClickListener {
        private TaskAccessory node;
        private List<TaskAccessory> fileList;

        DeleteItemClick(TaskAccessory node, List<TaskAccessory> fileList){
            this.node = node;
            this.fileList = fileList;
        }

        @Override
        public void onClick(View view) {
            fileList.remove(node);
            if(infoUpdate!=null)
                infoUpdate.update(null);
        }
    }

    private class ItemLLClick implements OnClickListener {
        private TaskAccessory node;
        private AsyncWordLoader asyncWordLoader = new AsyncWordLoader();

        public ItemLLClick(TaskAccessory node){
            this.node = node;
        }

        @Override
        public void onClick(View view) {
            Log.i("HuiZhi", "Come into file item click");
            if(node==null)
                return;
            Log.i("HuiZhi", "The file url:" + node.getFileUrl());
            String filepath = asyncWordLoader.loadWord(context, AsyncFileUpload.getInstance().getFileUrl(node.getFileUrl()), readInfoUpdate);
            openFile(filepath);
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

        private void openFile(String filepath){
            if(!TextUtils.isEmpty(filepath)){
                File docFile = new File(filepath);
                Log.i("HuiZhi", "Come into pad open");
                try {
                    FileUtil.openFile(context, docFile);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    };
}
