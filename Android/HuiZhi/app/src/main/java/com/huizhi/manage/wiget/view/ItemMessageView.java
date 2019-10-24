package com.huizhi.manage.wiget.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.HomeMessageInfoActivity;
import com.huizhi.manage.activity.home.HomeNewsInfoActivity;
import com.huizhi.manage.activity.task.TaskDetailActivity;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.node.MessageListNode;

public class ItemMessageView extends LinearLayout {
    private Activity context;
    private MessageListNode.MessageItemNode node;

    public ItemMessageView(Activity context, MessageListNode.MessageItemNode node){
        super(context);
        this.context = context;
        this.node = node;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_message, this);
        if(node==null)
            return;
        LinearLayout itemLL = findViewById(R.id.item_ll);
        TextView messageTV = findViewById(R.id.message_tv);
        messageTV.setText(node.getMessage());
        TextView timeTV = findViewById(R.id.time_tv);
        timeTV.setText(node.getStrCreateTime());
        ImageView tagIV = findViewById(R.id.tag_iv);
        TextView typeTV = findViewById(R.id.type_tv);
        ImageView statusIV = findViewById(R.id.msg_status_iv);
        if(node.getFunctionType()==1) {
            typeTV.setText("公告");
            Glide.with(context).load(R.mipmap.icon_msg_gg).into(tagIV);
            itemLL.setOnClickListener(new MsgItemClick(1));
        }else if(node.getFunctionType()==2) {
            typeTV.setText("新闻");
            Glide.with(context).load(R.mipmap.icon_msg_xw).into(tagIV);
            itemLL.setOnClickListener(new MsgItemClick(2));
        }else if(node.getFunctionType()==5) {
            typeTV.setText("家校互联");
            Glide.with(context).load(R.mipmap.icon_msg_jx).into(tagIV);
            itemLL.setOnClickListener(new MsgItemClick(5));
        }else if(node.getFunctionType()==3) {
            typeTV.setText("任务");
            Glide.with(context).load(R.mipmap.icon_msg_rw).into(tagIV);
            itemLL.setOnClickListener(new MsgItemClick(3));
        }
        if(!node.isRead())
            statusIV.setVisibility(View.VISIBLE);
        else
            statusIV.setVisibility(View.GONE);
    }

    private class MsgItemClick implements OnClickListener{
        private int type;

        public MsgItemClick(int type){
            this.type = type;
        }
        @Override
        public void onClick(View view) {
            if(type==2){
                Intent intent = new Intent(context, HomeNewsInfoActivity.class);
                intent.putExtra("Id", node.getBizId());
                context.startActivityForResult(intent, Constants.REQUEST_CODE);
//                context.startActivity(intent);
            }else if(type==3){
                Intent intent = new Intent(context, TaskDetailActivity.class);
                intent.putExtra("TaskId", node.getBizId());
                context.startActivityForResult(intent, Constants.REQUEST_CODE);
//                context.startActivity(intent);
            }else if(type==1){
                Intent intent = new Intent(context, HomeMessageInfoActivity.class);
                intent.putExtra("Id", node.getBizId());
                context.startActivityForResult(intent, Constants.REQUEST_CODE);
//                context.startActivity(intent);
            }
        }
    }
}
