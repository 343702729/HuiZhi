package com.huizhi.manage.wiget.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.base.HtmlWebActivity;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.URLHtmlData;
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
        FrameLayout itemFL = findViewById(R.id.item_fl);
        itemFL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HtmlWebActivity.class);
                intent.putExtra("Title", "提问详情");
                intent.putExtra("Url", URLHtmlData.getOperateAskDetailUrl(UserInfo.getInstance().getUser().getTeacherId(), node.getAskId()));
                context.startActivity(intent);
            }
        });
    }
}