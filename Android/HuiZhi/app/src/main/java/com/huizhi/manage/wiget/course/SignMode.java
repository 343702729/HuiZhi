package com.huizhi.manage.wiget.course;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.SignModeAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.dialog.SignDialog;
import com.huizhi.manage.node.StudentNode;

import java.util.ArrayList;
import java.util.List;

public class SignMode extends LinearLayout {
    private Activity context;
    private String lessonNum;
    private ListView listView;
    private SignModeAdapter signModeAdapter;
    private List<StudentNode> studentNodes;
    private BaseInfoUpdate infoUpdate;
    private CheckBox allCheck;

    public SignMode(Activity context, String lessonNum, BaseInfoUpdate infoUpdate){
        super(context);
        this.context = context;
        this.lessonNum = lessonNum;
        this.infoUpdate = infoUpdate;
        initViews();
    }

    public void setDatas(List<StudentNode> studentNodes){
        this.studentNodes = studentNodes;
        signModeAdapter.updateViewsData(studentNodes);
        if(allCheck!=null)
            allCheck.setChecked(false);
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_course_mode_sign, this);
        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(submitBtnClick);

        listView = findViewById(R.id.listview);
        signModeAdapter = new SignModeAdapter(context, null);
        listView.setAdapter(signModeAdapter);

        allCheck = findViewById(R.id.all_check);
        allCheck.setOnCheckedChangeListener(checkedChangeListener);
    }

    private OnClickListener submitBtnClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(signModeAdapter==null)
                return;
            List<StudentNode> nodes = signModeAdapter.getSignNodes();
            SignDialog signDialog = new SignDialog(context, lessonNum, studentNodes, nodes,infoUpdate);
            signDialog.showView(view);
        }
    };

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean flage) {
            if(signModeAdapter!=null)
                signModeAdapter.checkAll(flage);
        }
    };
}
