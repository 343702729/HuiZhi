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
import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.wiget.view.ItemTopicTitle;

import java.util.ArrayList;
import java.util.List;

public class ItemTopicFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private LinearLayout titlesLL;
    private LinearLayout itemBodyLL;
    private List<ItemTopicTitle> titleViews = new ArrayList<>();
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
        itemBodyLL = messageLayout.findViewById(R.id.item_body_ll);
        addTitles();
        setBodyViews(0);
    }

    private void addTitles(){
        titleViews.clear();
        currentSel = 0;
        ItemTopicTitle view1 = new ItemTopicTitle(activity, true, 0, new ItemTitlInfoUpdate());
        titleViews.add(view1);
        ItemTopicTitle view2 = new ItemTopicTitle(activity, false, 1, new ItemTitlInfoUpdate());
        titleViews.add(view2);
        ItemTopicTitle view3 = new ItemTopicTitle(activity, false, 2, new ItemTitlInfoUpdate());
        titleViews.add(view3);
        ItemTopicTitle view4 = new ItemTopicTitle(activity, false, 3, new ItemTitlInfoUpdate());
        titleViews.add(view4);
        titlesLL.addView(view1);
        titlesLL.addView(view2);
        titlesLL.addView(view3);
        titlesLL.addView(view4);
    }

    private void setBodyViews(int type){
//        itemBodyLL
    }

    private class ItemTitlInfoUpdate implements BaseInfoUpdate{

        public ItemTitlInfoUpdate(){
        }

        @Override
        public void update(Object object) {
            if(object==null)
                return;
            int index = (int)object;
            if(currentSel==index)
                return;
            currentSel = index;
            for (int i=0; i<titleViews.size(); i++){
                ItemTopicTitle itemV = titleViews.get(i);
                if(index==i){
                    itemV.setStatus(true);
                }else {
                    itemV.setStatus(false);
                }
            }
        }
    }
}
