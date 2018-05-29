package com.huizhi.manage.activity.home.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.TaskMouldAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.request.home.HomeTaskGetRequest;
import com.huizhi.manage.request.home.HomeTaskPostRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.wiget.swipe.SwipeListView;

import java.util.ArrayList;
import java.util.List;

import io.rong.imageloader.utils.L;

/**
 * Created by CL on 2017/12/19.
 * 日常任务模板管理
 */

public class HomeTaskMouldsActivity extends Activity {
    private SwipeListView listView;
    private TaskMouldAdapter taskMouldAdapter;
    private View addDateV, dateListV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_moulds);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDates();
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        addDateV = findViewById(R.id.adddate_ll);
        dateListV = findViewById(R.id.datelist_fl);

        Button createMBtn = findViewById(R.id.create_m_btn);
        createMBtn.setOnClickListener(createBtnClick);
        Button createBtn = (Button)findViewById(R.id.create_btn);
        createBtn.setOnClickListener(createBtnClick);

        listView = findViewById(R.id.listview);

        taskMouldAdapter = new TaskMouldAdapter(this, null, handler);
        listView.setAdapter(taskMouldAdapter);
//        listView.setOnItemClickListener(itemClickListener);
    }

    private void getDates(){
        HomeTaskGetRequest getRequest = new HomeTaskGetRequest();
        getRequest.getMouldTasks(UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    private View.OnClickListener createBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(HomeTaskMouldsActivity.this, HomeTaskMouldFoundActivity.class);
            startActivity(intent);
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Log.i("Task", "Come into item click");
            TaskMouldNode node = (TaskMouldNode)taskMouldAdapter.getItem(position);
            Intent intent = new Intent();
            intent.setClass(HomeTaskMouldsActivity.this, HomeTaskMouldFoundActivity.class);
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
                    if(msg.obj==null) {
                        setDateViews(null);
                        return;
                    }
                    List<TaskMouldNode> nodes = (List<TaskMouldNode>)msg.obj;
                    setDateViews(nodes);
                    break;
                case Constants.MSG_SUCCESS_ONE:

                    break;
            }
        }
    };

    private void setDateViews(List<TaskMouldNode> nodes){
        if(nodes==null){
            dateListV.setVisibility(View.GONE);
            addDateV.setVisibility(View.VISIBLE);
        }else{
            addDateV.setVisibility(View.GONE);
            dateListV.setVisibility(View.VISIBLE);
        }
        taskMouldAdapter.updateViews(nodes);
    }

}
