package com.huizhi.manage.fragment.home;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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
import com.huizhi.manage.activity.base.HtmlWebActivity;
import com.huizhi.manage.adapter.home.ImagePagerAdapter;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.URLHtmlData;
import com.huizhi.manage.node.BannerNode;
import com.huizhi.manage.node.HomeOperateNode;
import com.huizhi.manage.request.operate.OperateRequest;
import com.huizhi.manage.wiget.banner.BannerViewFlow;
import com.huizhi.manage.wiget.banner.PointView;
import com.huizhi.manage.wiget.banner.ViewFlow;
import com.huizhi.manage.wiget.pullableview.PullToRefreshLayout;
import com.huizhi.manage.wiget.view.ItemOperateTaskView;
import com.huizhi.manage.wiget.view.ItemOperateView;
import com.huizhi.manage.wiget.view.ItemProgrammeView;
import com.huizhi.manage.wiget.view.ItemQuestionView;

import java.util.ArrayList;
import java.util.List;

public class ItemOperateFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private BannerViewFlow viewFlow;
    private LinearLayout pointsLL;
    private PointView pointView;
    private LinearLayout taskLL;
    private LinearLayout yyLL;
    private LinearLayout faLL;
    private LinearLayout wbLL;
    private PullRefreshListener pullRefreshListener;

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
        pullRefreshListener = new PullRefreshListener();
        PullToRefreshLayout pullRL = (PullToRefreshLayout)messageLayout.findViewById(R.id.refreshview);
        pullRL.isPullUp(false);
        pullRL.setOnRefreshListener(pullRefreshListener);

        viewFlow = messageLayout.findViewById(R.id.viewflow);
        viewFlow.setOnViewSwitchListener(imageSwitchListener);
        pointsLL = messageLayout.findViewById(R.id.points_ll);

        taskLL = messageLayout.findViewById(R.id.tasks_ll);
        yyLL = messageLayout.findViewById(R.id.item_yy_ll);
        faLL = messageLayout.findViewById(R.id.item_fa_ll);
        wbLL = messageLayout.findViewById(R.id.item_wb_ll);

        LinearLayout rwLL = messageLayout.findViewById(R.id.rw_ll);
        LinearLayout dtLL = messageLayout.findViewById(R.id.dt_ll);
        LinearLayout faLL = messageLayout.findViewById(R.id.fa_ll);
        LinearLayout wdLL = messageLayout.findViewById(R.id.wd_ll);
        rwLL.setOnClickListener(itemLLClick);
        dtLL.setOnClickListener(itemLLClick);
        faLL.setOnClickListener(itemLLClick);
        wdLL.setOnClickListener(itemLLClick);

        LinearLayout moredtLL = messageLayout.findViewById(R.id.more_dt_ll);
        moredtLL.setOnClickListener(itemLLClick);
        LinearLayout morefaLL = messageLayout.findViewById(R.id.more_fa_ll);
        morefaLL.setOnClickListener(itemLLClick);
        LinearLayout morewdLL = messageLayout.findViewById(R.id.more_wd_ll);
        morewdLL.setOnClickListener(itemLLClick);
    }

    private View.OnClickListener itemLLClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, HtmlWebActivity.class);
            switch (view.getId()){
                case R.id.rw_ll:        //运行任务
                    intent.putExtra("Title", "运行任务");
                    intent.putExtra("Url", URLHtmlData.getOperateListUrl(UserInfo.getInstance().getUser().getTeacherId()));
                    activity.startActivity(intent);
                    break;
                case R.id.dt_ll:        //校区动态
                case R.id.more_dt_ll:   //校区动态 more
                    intent.putExtra("Title", "校区动态");
                    intent.putExtra("Url", URLHtmlData.getOperateNewsUrl(UserInfo.getInstance().getUser().getTeacherId()));
                    activity.startActivity(intent);
                    break;
                case R.id.fa_ll:        //运营指导
                case R.id.more_fa_ll:   //运营指导 more
                    intent.putExtra("Title", "运营指导");
                    intent.putExtra("Url", URLHtmlData.getOperatePlanUrl(UserInfo.getInstance().getUser().getTeacherId()));
                    activity.startActivity(intent);
                    break;
                case R.id.wd_ll:        //问答
                case R.id.more_wd_ll:   //问答 more
                    intent.putExtra("Title", "问答");
                    intent.putExtra("Url", URLHtmlData.getOperateAskUrl(UserInfo.getInstance().getUser().getTeacherId()));
                    activity.startActivity(intent);
                    break;
            }
        }
    };

    private void getDatas(){
        OperateRequest request = new OperateRequest();
        request.getHomeOperate(UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    private void setViewsData(HomeOperateNode node){
        if(node==null)
            return;
        setNewsBanner(node.getObjBanner());

        addOperateTasks(node.getObjTask());

        addOperateDatas(node.getObjNews());

        addProgrammeDatas(node.getObjKnowledge());

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
        pointsLL.removeAllViews();
        List<String> imgsList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();

        for(BannerNode node:bannerNodes){
//            node.setType(2);
            imgsList.add(node.getImgUrl());
            urlList.add(node.getNewsId());
        }
        viewFlow.setAdapter(new ImagePagerAdapter(activity, imgsList, urlList, titleList, bannerNodes, 1).setInfiniteLoop(true));
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
        yyLL.removeAllViews();
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

    private void addProgrammeDatas(List<HomeOperateNode.ObjKnowledge> nodes){
        faLL.removeAllViews();
        if(nodes==null)
            return;
        for (HomeOperateNode.ObjKnowledge node:nodes){
            faLL.addView(new ItemProgrammeView(activity, node));
        }
    }

    private void addQuesstionData(List<HomeOperateNode.ObjAsk> items){
        wbLL.removeAllViews();
        if(items==null)
            return;
        for (HomeOperateNode.ObjAsk item:items){
            wbLL.addView(new ItemQuestionView(activity, item));
        }

    }

    private class PullRefreshListener implements PullToRefreshLayout.OnRefreshListener {
        private PullToRefreshLayout refreshLayout, loadLayout;
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            refreshLayout = pullToRefreshLayout;
            getDatas();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            loadLayout = pullToRefreshLayout;
        }

        public void closeRefreshLoad(){
            if(refreshLayout!=null)
                refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            if(loadLayout!=null)
                loadLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(pullRefreshListener!=null)
                pullRefreshListener.closeRefreshLoad();
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
