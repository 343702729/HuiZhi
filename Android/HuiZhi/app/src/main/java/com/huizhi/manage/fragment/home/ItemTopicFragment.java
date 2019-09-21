package com.huizhi.manage.fragment.home;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.ViewPagerAdapter;
import com.huizhi.manage.base.BaseInfoUpdate;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_item_topic, container, false);
        activity = getActivity();
        initViews();
        return messageLayout;
    }

    private void initViews(){
        titlesLL = messageLayout.findViewById(R.id.item_title_ll);
        viewPager = messageLayout.findViewById(R.id.viewpager);
        addTitles();
        viewPager.setCurrentItem(0, false);
    }

    private void addTitles(){
        titleViews.clear();
        currentSel = 0;
        ItemTopicTitleView view1 = new ItemTopicTitleView(activity, true, 0, new ItemTitlInfoUpdate());
        titleViews.add(view1);
        ItemTopicTitleView view2 = new ItemTopicTitleView(activity, false, 1, new ItemTitlInfoUpdate());
        titleViews.add(view2);
        ItemTopicTitleView view3 = new ItemTopicTitleView(activity, false, 2, new ItemTitlInfoUpdate());
        titleViews.add(view3);
        ItemTopicTitleView view4 = new ItemTopicTitleView(activity, false, 3, new ItemTitlInfoUpdate());
        titleViews.add(view4);

        titlesLL.addView(view1);
        titlesLL.addView(view2);
        titlesLL.addView(view3);
        titlesLL.addView(view4);

        itemViews.add(view1.getBodyView());
        itemViews.add(view2.getBodyView());
        itemViews.add(view3.getBodyView());
        itemViews.add(view4.getBodyView());

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(itemViews);
        viewPager.setAdapter(pagerAdapter);
    }

    private class ItemTitlInfoUpdate implements BaseInfoUpdate{

        @Override
        public void update(Object object) {
            if(object==null)
                return;
            int index = (int)object;
            if(currentSel==index)
                return;
            currentSel = index;
            for (int i=0; i<titleViews.size(); i++){
                ItemTopicTitleView itemV = titleViews.get(i);
                if(index==i){
                    itemV.setStatus(true);
                    viewPager.setCurrentItem(index, false);
//                    setBodyViews(index);
                }else {
                    itemV.setStatus(false);
                }
            }
        }
    }
}
