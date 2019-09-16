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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.util.Constant;
import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.HomeAttendanceActivity;
import com.huizhi.manage.activity.home.HomeMessageActivity;
import com.huizhi.manage.activity.home.HomeWorkDailyActivity;
import com.huizhi.manage.activity.home.course.CourseListActivity;
import com.huizhi.manage.adapter.home.ImagePagerAdapter;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.node.BannerNode;
import com.huizhi.manage.request.home.HomeUserGetRequest;
import com.huizhi.manage.util.TLog;
import com.huizhi.manage.wiget.GlideCircleTransform;
import com.huizhi.manage.wiget.banner.MyViewFlow;
import com.huizhi.manage.wiget.banner.PointView;
import com.huizhi.manage.wiget.banner.ViewFlow;
import com.huizhi.manage.wiget.view.ItemCategoryView;
import com.huizhi.manage.wiget.view.ItemNewsView;

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
    private LinearLayout itemCategoryLL;
    private ImageView scanIV;

    private int imgSize = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_item_home, container, false);
        activity = getActivity();
        initViews();
        getBannerDates();
        return messageLayout;
    }

    private void initViews(){
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

        itemNewsLL = messageLayout.findViewById(R.id.item_news_ll);
        itemCategoryLL = messageLayout.findViewById(R.id.item_category_ll);

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

        addNewsLL();

        addCategoryLL();
    }

    /**
     * 滚动图片
     */
    private void getBannerDates(){
        HomeUserGetRequest getRequest = new HomeUserGetRequest();
        getRequest.getUserNewsBanner(handler);
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
            }
        }
    };

    private void addNewsLL(){
        itemNewsLL.addView(new ItemNewsView(activity, false));
        itemNewsLL.addView(new ItemNewsView(activity, true));
    }

    private void addCategoryLL(){
        itemCategoryLL.addView(new ItemCategoryView(activity, "教师"));
        itemCategoryLL.addView(new ItemCategoryView(activity, "运营"));
        itemCategoryLL.addView(new ItemCategoryView(activity, "学院"));
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
