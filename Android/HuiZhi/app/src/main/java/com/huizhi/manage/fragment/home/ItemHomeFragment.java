package com.huizhi.manage.fragment.home;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;
import com.huizhi.manage.R;
import com.huizhi.manage.activity.base.HtmlWebActivity;
import com.huizhi.manage.activity.home.HomeAttendanceActivity;
import com.huizhi.manage.activity.home.HomeMessageActivity;
import com.huizhi.manage.activity.home.HomeNewsActivity;
import com.huizhi.manage.activity.home.HomeWorkDailyActivity;
import com.huizhi.manage.activity.home.course.CourseListActivity;
import com.huizhi.manage.adapter.home.ImagePagerAdapter;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLHtmlData;
import com.huizhi.manage.node.BannerNode;
import com.huizhi.manage.node.HomeInfoNode;
import com.huizhi.manage.node.HomeOperateNode;
import com.huizhi.manage.node.TeacherTrainingNode;
import com.huizhi.manage.request.home.HomeNewRequest;
import com.huizhi.manage.request.home.HomeUserGetRequest;
import com.huizhi.manage.request.teacher.TeacherRequest;
import com.huizhi.manage.util.TLog;
import com.huizhi.manage.wiget.GlideCircleTransform;
import com.huizhi.manage.wiget.banner.MyViewFlow;
import com.huizhi.manage.wiget.banner.PointView;
import com.huizhi.manage.wiget.banner.ViewFlow;
import com.huizhi.manage.wiget.pullableview.PullToRefreshLayout;
import com.huizhi.manage.wiget.view.ItemCategoryView;
import com.huizhi.manage.wiget.view.ItemCourseView;
import com.huizhi.manage.wiget.view.ItemNewsView;
import com.huizhi.manage.wiget.view.ItemOperateView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ItemHomeFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private MyViewFlow viewFlow;
    private LinearLayout pointsLL;
    private PointView pointView;
    private LinearLayout itemNewsLL;
    private LinearLayout itemTeacherLL;
    private LinearLayout itemCategoryLL;
    private ImageView scanIV;

    private PullRefreshListener pullRefreshListener;

    private int imgSize = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_item_home, container, false);
        activity = getActivity();
        initViews();
        return messageLayout;
    }

    private void initViews(){
        pullRefreshListener = new PullRefreshListener();
        PullToRefreshLayout pullRL = (PullToRefreshLayout)messageLayout.findViewById(R.id.refreshview);
        pullRL.isPullUp(false);
        pullRL.setOnRefreshListener(pullRefreshListener);

        viewFlow = messageLayout.findViewById(R.id.viewflow);
        viewFlow.setOnViewSwitchListener(imageSwitchListener);
        pointsLL = messageLayout.findViewById(R.id.points_ll);

        scanIV = messageLayout.findViewById(R.id.scan_iv);
        scanIV.setOnClickListener(scanIVClick);

        LinearLayout jxhlLL = messageLayout.findViewById(R.id.user_kc_ll);
        jxhlLL.setOnClickListener(itemOnClick);
        LinearLayout kqLL = messageLayout.findViewById(R.id.user_kq_ll);
        kqLL.setOnClickListener(itemOnClick);
        LinearLayout rzLL = messageLayout.findViewById(R.id.user_rz_ll);
        rzLL.setOnClickListener(itemOnClick);
        LinearLayout ggLL = messageLayout.findViewById(R.id.user_gg_ll);
        ggLL.setOnClickListener(itemOnClick);

        ImageView jxhlIV = messageLayout.findViewById(R.id.jxhl_fl);
        jxhlIV.setOnClickListener(itemOnClick);

        LinearLayout morexwLL = messageLayout.findViewById(R.id.more_xw_ll);
        morexwLL.setOnClickListener(itemOnClick);

        LinearLayout morejsLL = messageLayout.findViewById(R.id.more_teacher_ll);
        morejsLL.setOnClickListener(itemOnClick);

        LinearLayout moreyyLL = messageLayout.findViewById(R.id.more_yy_ll);
        moreyyLL.setOnClickListener(itemOnClick);

        itemNewsLL = messageLayout.findViewById(R.id.item_news_ll);
        itemTeacherLL = messageLayout.findViewById(R.id.item_teacher_ll);
        itemCategoryLL = messageLayout.findViewById(R.id.item_operate_ll);

        TextView nameTV = messageLayout.findViewById(R.id.name_tv);
        nameTV.setText(UserInfo.getInstance().getUser().getTeacherName());
        TextView schoolTV = messageLayout.findViewById(R.id.school_tv);
        schoolTV.setText(UserInfo.getInstance().getUser().getSchoolName());

        String headImg = AsyncFileUpload.getInstance().getFileUrl(UserInfo.getInstance().getUser().getHeadImgUrl());
        TLog.log("The headimg is:" + headImg);
        ImageView headIV = messageLayout.findViewById(R.id.user_iv);
        Glide.with(activity).load(headImg)
                .error(R.mipmap.user_icon)
                //圆形
                .transform(new GlideCircleTransform(activity))
                .into(headIV);

        getDatas();
    }

    @Override
    public void onResume() {
        super.onResume();
        getTZDatas();
    }

    private void getDatas(){
//        TeacherRequest request = new TeacherRequest();
//        request.getProgressData(UserInfo.getInstance().getUser().getTeacherId(), handler);
        HomeNewRequest newRequest = new HomeNewRequest();
        newRequest.getHomeInfo(handler);
    }

    private void getTZDatas(){
        HomeUserGetRequest request = new HomeUserGetRequest();
        request.getTZNoReadCount(UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    private void setViewsData(HomeInfoNode node){
        if(node==null)
            return;
        setNewsBanner(node.getObjBanner());
        addNewsLL(node.getObjNews());
        addTeacherLL(node.getObjTeachingTraining());
        addOperateLL(node.getObjBusinessNews());
    }

    private void setProgressData(TeacherTrainingNode.ObjProgress progressNode){
        if(progressNode==null)
            return;
        TextView courseTV = messageLayout.findViewById(R.id.course_tv);
        courseTV.setText(progressNode.getDoneLessonNum() + "/" + progressNode.getTotLessonNum());
        TextView commentTV = messageLayout.findViewById(R.id.comment_tv);
        commentTV.setText(progressNode.getToBeCommentNum() + "/" + progressNode.getCommentedNum());
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
            imgsList.add(node.getImgUrl());
            urlList.add(node.getNewsId());
        }
        viewFlow.setAdapter(new ImagePagerAdapter(activity, imgsList, urlList, titleList, bannerNodes).setInfiniteLoop(true));
        viewFlow.setmSideBuffer(bannerNodes.size());
        viewFlow.setTimeSpan(4000);
        viewFlow.setSelection(bannerNodes.size() * 1000);
        viewFlow.startAutoFlowTimer();
        pointView = new PointView(activity, 1);
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

    private class PullRefreshListener implements PullToRefreshLayout.OnRefreshListener {
        private PullToRefreshLayout refreshLayout, loadLayout;
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            refreshLayout = pullToRefreshLayout;
//            handler.sendEmptyMessageDelayed(2, 3000);
            getDatas();
            getTZDatas();
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

    private View.OnClickListener scanIVClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, CaptureActivity.class);
            startActivityForResult(intent, Constant.REQ_QR_CODE);
        }
    };

    private View.OnClickListener itemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.jxhl_fl:
                case R.id.user_kc_ll://家校互联
                    intent = new Intent();
                    intent.setClass(activity, CourseListActivity.class);
                    activity.startActivity(intent);
                    break;
                case R.id.user_kq_ll://考勤
                    intent = new Intent();
                    intent.setClass(activity, HomeAttendanceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_rz_ll://工作日志
                    intent = new Intent();
                    intent.setClass(activity, HomeWorkDailyActivity.class);
                    activity.startActivity(intent);
                    break;
                case R.id.user_gg_ll://消息通知
                    intent = new Intent();
                    intent.setClass(activity, HomeMessageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.more_xw_ll://新闻more
                    intent = new Intent();
                    intent.setClass(activity, HomeNewsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.more_teacher_ll://教师more
                    intent = new Intent(activity, HtmlWebActivity.class);
                    intent.putExtra("Title", "教学中心");
                    intent.putExtra("Url", URLHtmlData.getTrainingListUrl(UserInfo.getInstance().getUser().getTeacherId()));
                    activity.startActivity(intent);
                    break;
                case R.id.more_yy_ll:
                    intent = new Intent(activity, HtmlWebActivity.class);
                    intent.putExtra("Title", "运营中心");
                    intent.putExtra("Url", URLHtmlData.getOperatePlanUrl(UserInfo.getInstance().getUser().getTeacherId()));
                    activity.startActivity(intent);
                    break;
            }
        }
    };

    private void addNewsLL(List<HomeInfoNode.ObjNew> items){
        itemNewsLL.removeAllViews();
        if(items==null)
            return;
        for (int i=0; i<items.size();i++){
            if(i==0)
                itemNewsLL.addView(new ItemNewsView(activity, items.get(i), false));
            else
                itemNewsLL.addView(new ItemNewsView(activity, items.get(i), true));
        }

    }

    private void addTeacherLL(List<TeacherTrainingNode.ObjTeachingTrainingItem> items){
        itemTeacherLL.removeAllViews();
        if(items==null)
            return;
        for (int i=0; i<items.size();){
            if(i>=items.size())
                return;
            TeacherTrainingNode.ObjTeachingTrainingItem item1 = null, item2 = null;
            item1 = items.get(i);
            i++;
            if(i<items.size()) {
                item2 = items.get(i);
                i++;
            }

            if(i+1<items.size())
                itemTeacherLL.addView(new ItemCourseView(activity, item1, item2));
            else
                itemTeacherLL.addView(new ItemCourseView(activity, item1, item2, true));
        }
    }

    private void addOperateLL(List<HomeOperateNode.ObjNew> items){
        itemCategoryLL.removeAllViews();
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

            if(i+1<items.size())
                itemCategoryLL.addView(new ItemOperateView(activity, item1, item2));
            else
                itemCategoryLL.addView(new ItemOperateView(activity, item1, item2, true));
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(pullRefreshListener!=null)
                pullRefreshListener.closeRefreshLoad();
            switch (msg.what) {
                case Constants.MSG_SUCCESS:
                    HomeInfoNode node = (HomeInfoNode)msg.obj;
                    setViewsData(node);
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    if (msg.obj == null)
                        return;
                    List<BannerNode> bannerNodes = (List<BannerNode>) msg.obj;
                    setNewsBanner(bannerNodes);
                    break;
                case Constants.MSG_SUCCESS_FOUR:
                    TeacherTrainingNode.ObjProgress progressNode = (TeacherTrainingNode.ObjProgress)msg.obj;
                    setProgressData(progressNode);
                    break;
                case Constants.MSG_SUCCESS_FIVE:
                    if(msg.obj==null)
                        return;
                    String ggcount = (String)msg.obj;
                    Log.i("HuiZhi", "The gg count is:" + ggcount);
                    ImageView ggCountIV = messageLayout.findViewById(R.id.gg_count_iv);
                    if("0".equals(ggcount))
                        Glide.with(activity).load(R.mipmap.icon_home_gg).into(ggCountIV);
                    else
                        Glide.with(activity).load(R.mipmap.icon_home_gg_fc).into(ggCountIV);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            TLog.log("The scan result:" + scanResult);
        }
    }
}
