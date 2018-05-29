package com.huizhi.manage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.communicate.CommunicateListActivity;
import com.huizhi.manage.activity.communicate.CommunicateUsersActivity;
import com.huizhi.manage.adapter.common.PageViewAdapter;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by CL on 2018/2/5.
 * 通讯列表
 */

public class CommunicateListFragment extends Fragment{
    private View messageLayout;
    private Activity activity;
    private LocalActivityManager localActivityManager = null;
    private List<View> listViews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_communicate_list, container, false);
        activity = getActivity();
        localActivityManager = new LocalActivityManager(activity, true);
        localActivityManager.dispatchCreate(savedInstanceState);
        initViews();
        return messageLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
//        RongIM.getInstance().
        CommunicateListActivity communicateListActivity = (CommunicateListActivity)localActivityManager.getActivity("CommunicateListActivity");
        communicateListActivity.onResume();
    }

    private void initViews(){
        Button usersBtn = messageLayout.findViewById(R.id.communicate_users_btn);
        usersBtn.setOnClickListener(usersBtnClick);

        ViewPager viewPager = messageLayout.findViewById(R.id.viewpager);
        Intent listActivity = new Intent(activity, CommunicateListActivity.class);
        View listView = getView("CommunicateListActivity", listActivity);
        listViews.add(listView);
        viewPager.setAdapter(new PageViewAdapter(listViews));
        viewPager.setCurrentItem(0);
    }

    private View getView(String id, Intent intent) {
        return localActivityManager.startActivity(id, intent).getDecorView();
    }

    private View.OnClickListener usersBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(activity, CommunicateUsersActivity.class);
            startActivity(intent);
        }
    };

}
