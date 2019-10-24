package com.huizhi.manage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.fragment.home.ItemHomeFragment;
import com.huizhi.manage.fragment.home.ItemOperateFragment;
import com.huizhi.manage.fragment.home.ItemTeacherFragment;
import com.huizhi.manage.fragment.home.ItemTopicFragment;

public class NewHomeFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private FragmentManager fragmentManager;
    private Fragment itemHome,itemTeacher, itemOperate, itemTopic;
    private int currentIndex = 0;
    private FrameLayout item1FL, item2FL, item3FL, item4FL;
    private Fragment currentFG;
    private int changeIndex = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_new_home, container, false);
        activity = getActivity();
        initViews();
        return messageLayout;
    }

    private void initViews(){
        fragmentManager = getFragmentManager();

        item1FL = messageLayout.findViewById(R.id.item1_fl);
        item1FL.setOnClickListener(itemFLClick);
        item2FL = messageLayout.findViewById(R.id.item2_fl);
        item2FL.setOnClickListener(itemFLClick);
        item3FL = messageLayout.findViewById(R.id.item3_fl);
        item3FL.setOnClickListener(itemFLClick);
        item4FL = messageLayout.findViewById(R.id.item4_fl);
        item4FL.setOnClickListener(itemFLClick);

        setTabSelection(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(currentFG!=null)
            currentFG.onResume();
    }

    private View.OnClickListener itemFLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.item1_fl:
                    setTabSelection(1);
                    break;
                case R.id.item2_fl:
                    setTabSelection(2);
                    break;
                case R.id.item3_fl:
                    setTabSelection(3);
                    break;
                case R.id.item4_fl:
                    setTabSelection(4);
                    break;
            }
        }
    };

    private void setTabSelection(int index){
        if(index==currentIndex)
            return;
        currentIndex = index;
        setItemViewSel(index);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (index){
            case 1:
                if(itemHome==null){
                    itemHome = new ItemHomeFragment();
                    transaction.add(R.id.content_fl, itemHome);
                }else{
                    transaction.show(itemHome);
                    itemHome.onResume();
                }
                currentFG = itemHome;
                break;
            case 2:
                if(itemTeacher==null){
                    itemTeacher = new ItemTeacherFragment();
                    transaction.add(R.id.content_fl, itemTeacher);
                }else{
                    transaction.show(itemTeacher);
                    itemTeacher.onResume();
                }
                currentFG = itemTeacher;
                break;
            case 3:
                if(itemOperate==null){
                    itemOperate = new ItemOperateFragment();
                    transaction.add(R.id.content_fl, itemOperate);
                }else{
                    transaction.show(itemOperate);
                    itemOperate.onResume();
                }
                currentFG = itemOperate;
                break;
            case 4:
                if(itemTopic==null){
                    itemTopic = new ItemTopicFragment();
                    transaction.add(R.id.content_fl, itemTopic);
                }else{
                    transaction.show(itemTopic);
                    itemTopic.onResume();
                }
                currentFG = itemTopic;
                break;
        }
//        if(changeIndex==-1||changeIndex==index){
//
//        }else if(changeIndex<index)
//            transaction.setCustomAnimations(R.animator.slide_right_in, R.animator.slide_left_out);
//        else
//            transaction.setCustomAnimations(R.animator.slide_left_in, R.animator.slide_right_out);
//        transaction.replace(R.id.content_fl, currentFG);

//        transaction.setCustomAnimations(R.animator.slide_right_in, R.animator.slide_left_out).replace(R.id.content_fl, currentFG).commitAllowingStateLoss();
        transaction.commit();
        changeIndex = index;
    }

    private void hideFragment(FragmentTransaction transaction){
        if(itemHome!=null)
            transaction.hide(itemHome);
        if(itemTeacher!=null)
            transaction.hide(itemTeacher);
        if(itemOperate!=null)
            transaction.hide(itemOperate);
        if(itemTopic!=null)
            transaction.hide(itemTopic);
    }

    private void setItemViewSel(int index){
        TextView item1TV = item1FL.findViewWithTag("itemTV");
        View item1IV = item1FL.findViewWithTag("itemIV");
        TextView item2TV = item2FL.findViewWithTag("itemTV");
        View item2IV = item2FL.findViewWithTag("itemIV");
        TextView item3TV = item3FL.findViewWithTag("itemTV");
        View item3IV = item3FL.findViewWithTag("itemIV");
        TextView item4TV = item4FL.findViewWithTag("itemTV");
        View item4IV = item4FL.findViewWithTag("itemIV");

        item1TV.setTextColor(activity.getResources().getColor(R.color.gray_dark_text));
        item1TV.getPaint().setFakeBoldText(false);
        item1IV.setVisibility(View.INVISIBLE);
        item2TV.setTextColor(activity.getResources().getColor(R.color.gray_dark_text));
        item2TV.getPaint().setFakeBoldText(false);
        item2IV.setVisibility(View.INVISIBLE);
        item3TV.setTextColor(activity.getResources().getColor(R.color.gray_dark_text));
        item3TV.getPaint().setFakeBoldText(false);
        item3IV.setVisibility(View.INVISIBLE);
        item4TV.setTextColor(activity.getResources().getColor(R.color.gray_dark_text));
        item4TV.getPaint().setFakeBoldText(false);
        item4IV.setVisibility(View.INVISIBLE);
        switch (index){
            case 1:
                item1TV.setTextColor(activity.getResources().getColor(R.color.blue_light));
                item1TV.getPaint().setFakeBoldText(true);
                item1IV.setVisibility(View.VISIBLE);
                break;
            case 2:
                item2TV.setTextColor(activity.getResources().getColor(R.color.blue_light));
                item2TV.getPaint().setFakeBoldText(true);
                item2IV.setVisibility(View.VISIBLE);
                break;
            case 3:
                item3TV.setTextColor(activity.getResources().getColor(R.color.blue_light));
                item3TV.getPaint().setFakeBoldText(true);
                item3IV.setVisibility(View.VISIBLE);
                break;
            case 4:
                item4TV.setTextColor(activity.getResources().getColor(R.color.blue_light));
                item4TV.getPaint().setFakeBoldText(true);
                item4IV.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(currentFG!=null)
            currentFG.onActivityResult(requestCode, resultCode, data);
    }
}
