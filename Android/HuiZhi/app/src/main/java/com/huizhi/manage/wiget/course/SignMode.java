package com.huizhi.manage.wiget.course;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.SignModeAdapter;
import com.huizhi.manage.node.SignNode;

import java.util.ArrayList;
import java.util.List;

public class SignMode extends LinearLayout {
    private Activity context;
    private ListView listView;
    private SignModeAdapter signModeAdapter;

    public SignMode(Activity context){
        super(context);
        this.context = context;
        initViews();
    }

    public SignMode(Activity context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public void initViews(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_course_mode_sign, this);
        listView = findViewById(R.id.listview);
        List<SignNode> nodes = new ArrayList<>();
        nodes.add(new SignNode());
        nodes.add(new SignNode());
        nodes.add(new SignNode());
        nodes.add(new SignNode());
        nodes.add(new SignNode());
        nodes.add(new SignNode());
        signModeAdapter = new SignModeAdapter(context, nodes);
        listView.setAdapter(signModeAdapter);

        CheckBox allCheck = findViewById(R.id.all_check);
        allCheck.setOnCheckedChangeListener(checkedChangeListener);
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean flage) {
            if(signModeAdapter!=null)
                signModeAdapter.checkAll(flage);
        }
    };
}
