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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.home.ImagePagerAdapter;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.node.BannerNode;
import com.huizhi.manage.node.HomeOperateNode;
import com.huizhi.manage.request.home.HomeUserGetRequest;
import com.huizhi.manage.request.operate.OperateRequest;
import com.huizhi.manage.wiget.banner.MyViewFlow;
import com.huizhi.manage.wiget.banner.PointView;
import com.huizhi.manage.wiget.banner.ViewFlow;
import com.huizhi.manage.wiget.view.ItemOperateTaskView;
import com.huizhi.manage.wiget.view.ItemOperateView;
import com.huizhi.manage.wiget.view.ItemProgrammeView;
import com.huizhi.manage.wiget.view.ItemQuestionView;

import java.util.ArrayList;
import java.util.List;

public class ItemOperateFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private MyViewFlow viewFlow;
    private LinearLayout pointsLL;
    private PointView pointView;
    private LinearLayout taskLL;
    private LinearLayout yyLL;
    private LinearLayout faLL;
    private LinearLayout wbLL;

    private int imgSize = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_item_operate, container, false);
        activity = getActivity();
        initViews();
        getDatas();
        return messageLayout;
    }

    private void initViews() {
        viewFlow = messageLayout.findViewById(R.id.viewflow);
        viewFlow.setOnViewSwitchListener(imageSwitchListener);
        pointsLL = messageLayout.findViewById(R.id.points_ll);

        taskLL = messageLayout.findViewById(R.id.tasks_ll);
        yyLL = messageLayout.findViewById(R.id.item_yy_ll);
        faLL = messageLayout.findViewById(R.id.item_fa_ll);
        wbLL = messageLayout.findViewById(R.id.item_wb_ll);


        addProgrammeDatas();

    }

    private void getDatas(){
        OperateRequest request = new OperateRequest();
        request.getHomeOperate(handler);
    }

    private void setViewsData(HomeOperateNode node){
        if(node==null)
            return;
        setNewsBanner(node.getObjBanner());

        addOperateDatas(node.getObjNews());

        addOperateTasks(node.getObjTask());

        addQuesstionData(node.getObjAsk());
    }

    private void setNewsBanner(List<BannerNode> bannerNodes){
        if(bannerNodes==null||bannerNodes.size()==0)
            return;
        imgSize = bannerNodes.size();
        FrameLayout bannerFL = messageLayout.findViewById(R.id.banner_fl);
        bannerFL.setVisibility(View.VISIBLE);
        initBanner(bannerNodes);
    }

    private void initBanner(List<BannerNode> bannerNodes){
        if(bannerNodes==null)
            return;
        List<String> imgsList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();

        for(BannerNode node:bannerNodes){
            imgsList.add(node.getImgUrl());
            urlList.add(node.getNewsId());
        }
        viewFlow.setAdapter(new ImagePagerAdapter(activity, imgsList, urlList, titleList).setInfiniteLoop(true));
        viewFlow.setmSideBuffer(bannerNodes.size());
        viewFlow.setTimeSpan(4000);
        viewFlow.setSelection(bannerNodes.size() * 1000);
        viewFlow.startAutoFlowTimer();
        pointView = new PointView(activity);
        pointView.addViews(pointsLL, bannerNodes.size());
    }

    private ViewFlow.ViewSwitchListener imageSwitchListener = new ViewFlow.ViewSwitchListener() {
        @Override
        public void onSwitched(View view, int position) {
            if(imgSize == 0)
                return;
            int count = position%imgSize;
            if(pointView!=null)
                pointView.setPointSelect(count);
        }
    };

    private void addOperateTasks(List<HomeOperateNode.ObjTask> items){
        taskLL.removeAllViews();
        if(items==null)
            return;
        for (HomeOperateNode.ObjTask item:items){
            taskLL.addView(new ItemOperateTaskView(activity, item));
        }
    }

    private void addOperateDatas(List<HomeOperateNode.ObjNew> items){
        if(items==null)
            return;
        for (int i=0; i<items.size();){
            if(i>=items.size())
                return;
            HomeOperateNode.ObjNew item1 = null, item2 = null;
            item1 = items.get(i);
            i++;
            if(i<items.size()) {
                item2 = items.get(i);
                i++;
            }

            yyLL.addView(new ItemOperateView(activity, item1, item2));
        }

    }

    private void addProgrammeDatas(){
        faLL.addView(new ItemProgrammeView(activity));
        faLL.addView(new ItemProgrammeView(activity));
    }

    private void addQuesstionData(List<HomeOperateNode.ObjAsk> items){
        if(items==null)
            return;
        wbLL.removeAllViews();
        for (HomeOperateNode.ObjAsk item:items){
            wbLL.addView(new ItemQuestionView(activity, item));
        }

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.MSG_SUCCESS_ONE:
                    if (msg.obj == null)
                        return;
                    List<BannerNode> bannerNodes = (List<BannerNode>) msg.obj;
                    setNewsBanner(bannerNodes);
                    break;
                case Constants.MSG_SUCCESS:
                    HomeOperateNode node = (HomeOperateNode)msg.obj;
                    setViewsData(node);
                    break;
            }
        }
    };
}
