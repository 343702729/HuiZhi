package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.material.MaterialFilePictureInfoActivity;
import com.huizhi.manage.activity.material.MaterialFileVideoInfoActivity;
import com.huizhi.manage.activity.material.MaterialFileWordInfoActivity;
import com.huizhi.manage.activity.material.MaterialFilesActivity;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.node.FileNode;
import com.huizhi.manage.node.FolderNode;

/**
 * Created by CL on 2017/12/18.
 */

public class LineItemView extends LinearLayout{
    private Context context;
    private String name;

    public LineItemView(Context context){
        super(context);
        this.context = context;
        initViews();
    }

    public LineItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.adapter_line, this);
    }

    public void setFolderDatas(FolderNode node, BaseInfoUpdate infoUpdate){
        LinearLayout itemLL = (LinearLayout)findViewById(R.id.line_ll);
        itemLL.setOnClickListener(new ItemFolderClick(node, infoUpdate));
        TextView textV = (TextView)findViewById(R.id.line_name_tv);
        textV.setText(node.getFolderName());
    }

    public void setFileDates(FileNode node){
        LinearLayout itemLL = (LinearLayout)findViewById(R.id.line_ll);
        itemLL.setOnClickListener(new ItemFileClick(node));
        ImageView iconIV = findViewById(R.id.line_icon_iv);
        if(node.getFileType() == 1){
            iconIV.setBackgroundResource(R.mipmap.icon_file_video);
        }else if(node.getFileType() == 2){
            iconIV.setBackgroundResource(R.mipmap.icon_file_pic);
        }else if(node.getFileType() == 3){
            iconIV.setBackgroundResource(R.mipmap.icon_file_word);
        }
        TextView textV = (TextView)findViewById(R.id.line_name_tv);
        textV.setText(node.getFileName());
    }

    private class ItemFolderClick implements OnClickListener {
        private FolderNode node;
        private BaseInfoUpdate infoUpdate;

        ItemFolderClick(FolderNode node, BaseInfoUpdate infoUpdate){
            this.node = node;
            this.infoUpdate = infoUpdate;
        }

        @Override
        public void onClick(View view) {
            if(node.getSubFoldNum()==0){
                Intent intent = new Intent();
                intent.setClass(context, MaterialFilesActivity.class);
                intent.putExtra("Title", node.getFolderName());
                intent.putExtra("Favorite", false);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Item", node);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }else {
                if(infoUpdate!=null)
                    infoUpdate.update(node.getFolderId());
            }

        }
    };

    private class ItemFileClick implements OnClickListener{
        private FileNode node;

        ItemFileClick(FileNode node){
            this.node = node;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            if(node.getFileType()==1)
                intent.setClass(context, MaterialFileVideoInfoActivity.class);
            else if(node.getFileType()==2)
                intent.setClass(context, MaterialFilePictureInfoActivity.class);
            else if(node.getFileType()==3)
                intent.setClass(context, MaterialFileWordInfoActivity.class);
            intent.putExtra("FileId", node.getFileId());
            Bundle bundle = new Bundle();
            bundle.putSerializable("Item", node);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }
}
