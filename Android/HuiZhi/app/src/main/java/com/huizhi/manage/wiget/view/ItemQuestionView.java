package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.node.HomeOperateNode;

public class ItemQuestionView extends LinearLayout {
    private Context context;
    private HomeOperateNode.ObjAsk node;

    public ItemQuestionView(Context context, HomeOperateNode.ObjAsk node){
        super(context);
        this.context = context;
        this.node = node;
        initViews();
    }

    public void initViews() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_question, this);
        TextView questionTV = findViewById(R.id.question_tv);
        questionTV.setText(node.getQuestion());
    }
}