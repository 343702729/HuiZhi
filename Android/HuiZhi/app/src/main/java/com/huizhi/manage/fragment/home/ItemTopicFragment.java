package com.huizhi.manage.fragment.home;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.ViewPagerAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.node.CourseWareCategoryNode;
import com.huizhi.manage.request.topic.TopicRequest;
import com.huizhi.manage.util.TLog;
import com.huizhi.manage.wiget.ViewPager;
import com.huizhi.manage.wiget.view.ItemTopicTitleView;

import java.util.ArrayList;
import java.util.List;

public class ItemTopicFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private LinearLayout titlesLL;
    private ViewPager viewPager;
    private List<ItemTopicTitleView> titleViews = new ArrayList<>();
    private List<View> itemViews = new ArrayList<>();
    private int currentSel = 0;
    public static ViewPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_item_topic, container, false);
        activity = getActivity();
        initViews();
        getDatas();
        return messageLayout;
    }

    private void initViews(){
        titlesLL = messageLayout.findViewById(R.id.item_title_ll);
        viewPager = messageLayout.findViewById(R.id.viewpager);
//        addTitles();
        currentSel = 0;
        ItemTopicTitleView view1 = new ItemTopicTitleView(activity, true, null, 0, new ItemTitlInfoUpdate());
        titleViews.add(view1);
        titlesLL.addView(view1);
        itemViews.add(view1.getBodyView());
        pagerAdapter = new ViewPagerAdapter(itemViews);
        viewPager.setCurrentItem(0, false);
    }

    private void getDatas(){
        TopicRequest request = new TopicRequest();
        request.getCourseCategory("", handler);
    }

    private void addTitles(List<CourseWareCategoryNode> nodes){
        if(nodes==null||nodes.size()==0)
            return;
        int i = 1;
        for (CourseWareCategoryNode node:nodes){
            ItemTopicTitleView view = new ItemTopicTitleView(activity, false, node, i, new ItemTitlInfoUpdate());
            titleViews.add(view);
            titlesLL.addView(view);
            itemViews.add(view.getBodyView());
            i++;
        }
        pagerAdapter = new ViewPagerAdapter(itemViews);
//        pagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(pagerAdapter);
    }

    private class ItemTitlInfoUpdate implements BaseInfoUpdate{

        @Override
        public void update(Object object) {
            if(object==null)
                return;
            int index = (int)object;
//            if(currentSel==index)
//                return;
            currentSel = index;
            for (int i=0; i<titleViews.size(); i++){
                ItemTopicTitleView itemV = titleViews.get(i);
                if(index==i){
                    itemV.setStatus(true);
                    viewPager.setCurrentItem(index, false);
                    itemV.resetView();
//                    setBodyViews(index);
                }else {
                    itemV.setStatus(false);
                }
            }
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    List<CourseWareCategoryNode> nodes = (List<CourseWareCategoryNode>)msg.obj;
                    TLog.log("The nodes size:" + nodes.size());
                    if(nodes!=null)
                        addTitles(nodes);
//                    setViewsData(node);
                    break;
            }
        }
    };
}
