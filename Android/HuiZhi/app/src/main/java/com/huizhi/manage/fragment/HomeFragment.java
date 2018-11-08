package com.huizhi.manage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.activity.home.HomeAttendanceActivity;
import com.huizhi.manage.activity.home.HomeEmailInfoActivity;
import com.huizhi.manage.activity.home.HomeFileMenuActivity;
import com.huizhi.manage.activity.home.HomeMessageActivity;
import com.huizhi.manage.activity.home.HomeNewsActivity;
import com.huizhi.manage.activity.home.HomeWenBaActivity;
import com.huizhi.manage.activity.home.HomeYunYinFXActivity;
import com.huizhi.manage.activity.home.course.CourseListActivity;
import com.huizhi.manage.activity.home.oa.OAActivity;
import com.huizhi.manage.activity.home.task.HomeTaskAgencyActivity;
import com.huizhi.manage.activity.home.task.HomeTaskAllocationActivity;
import com.huizhi.manage.activity.home.HomeWorkDailyActivity;
import com.huizhi.manage.activity.home.task.HomeTaskFixedActivity;
import com.huizhi.manage.adapter.home.ImagePagerAdapter;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.login.LoginActivity;
import com.huizhi.manage.node.BannerNode;
import com.huizhi.manage.node.EmailInfoNode;
import com.huizhi.manage.node.TaskSummaryNode;
import com.huizhi.manage.request.home.HomeUserGetRequest;
import com.huizhi.manage.util.PictureUtil;
import com.huizhi.manage.wiget.banner.MyViewFlow;
import com.huizhi.manage.wiget.banner.PointView;
import com.huizhi.manage.wiget.banner.ViewFlow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/13.
 */

public class HomeFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private MyViewFlow viewFlow;
    private LinearLayout pointsLL;
    private PointView pointView;
    private int imgSize = 0;
    private EmailInfoNode emailInfoNode;
    private ImageView headIV;
    private AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        initViews();
        getBannerDates();
        setHeadPortrait();
        return messageLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("HuiZhi", "coem into home fragment resumt");
        chargeDate();
        getDates();
        setHeadPortrait();
    }

    private void initViews(){

        headIV = messageLayout.findViewById(R.id.user_iv);

        viewFlow = messageLayout.findViewById(R.id.viewflow);
        viewFlow.setOnViewSwitchListener(imageSwitchListener);
        pointsLL = messageLayout.findViewById(R.id.points_ll);

        LinearLayout weekAllLL = messageLayout.findViewById(R.id.week_all_ll);
        weekAllLL.setOnClickListener(weekItemClick);
        LinearLayout weekDoneLL = messageLayout.findViewById(R.id.week_done_ll);
        weekDoneLL.setOnClickListener(weekItemClick);
        LinearLayout weekTodoLL = messageLayout.findViewById(R.id.week_todo_ll);
        weekTodoLL.setOnClickListener(weekItemClick);

        //代办任务
        LinearLayout dbLL = (LinearLayout)messageLayout.findViewById(R.id.user_db_ll);
        dbLL.setOnClickListener(itemOnClick);
        //考勤
        LinearLayout kqLL = (LinearLayout)messageLayout.findViewById(R.id.user_kq_ll);
        kqLL.setOnClickListener(itemOnClick);
        //资料中心
        LinearLayout zlLL = (LinearLayout)messageLayout.findViewById(R.id.user_zl_ll);
        zlLL.setOnClickListener(itemOnClick);
        //消息通知
        LinearLayout tzLL = (LinearLayout)messageLayout.findViewById(R.id.user_tz_ll);
        tzLL.setOnClickListener(itemOnClick);
        //未读邮件
        LinearLayout yjLL = (LinearLayout)messageLayout.findViewById(R.id.user_yj_ll);
        yjLL.setOnClickListener(itemOnClick);
        //绘智新闻
        LinearLayout xwLL = (LinearLayout)messageLayout.findViewById(R.id.user_xw_ll);
        xwLL.setOnClickListener(itemOnClick);
        //工作日志
        LinearLayout rzLL = (LinearLayout)messageLayout.findViewById(R.id.user_rz_ll);
        rzLL.setOnClickListener(itemOnClick);
        //问吧
        LinearLayout wbLL = (LinearLayout)messageLayout.findViewById(R.id.user_wb_ll);
        wbLL.setOnClickListener(itemOnClick);
        //校区运营分享
        LinearLayout fxLL = (LinearLayout)messageLayout.findViewById(R.id.user_fx_ll);
        fxLL.setOnClickListener(itemOnClick);
        //课程列表
        LinearLayout kcLL = (LinearLayout)messageLayout.findViewById(R.id.user_kc_ll);
        kcLL.setOnClickListener(itemOnClick);
        //OA
        LinearLayout oaLL = (LinearLayout)messageLayout.findViewById(R.id.user_oa_ll);
        oaLL.setOnClickListener(itemOnClick);

        if("ZYX".equals(UserInfo.getInstance().getUser().getSchoolType())){
            oaLL.setVisibility(View.VISIBLE);
        }else {
            oaLL.setVisibility(View.GONE);
        }

        if(UserInfo.getInstance().getUser().isAdmin()){
            //任务管理
            LinearLayout fpLL = (LinearLayout)messageLayout.findViewById(R.id.user_fp_ll);
            fpLL.setOnClickListener(itemOnClick);
            fpLL.setVisibility(View.VISIBLE);
            ImageView fpIV = messageLayout.findViewById(R.id.user_fp_iv);
            fpIV.setVisibility(View.VISIBLE);
        }

        if(UserInfo.getInstance().getUser().isEnableKnowledge()){
            //校区运营分享
            fxLL.setVisibility(View.VISIBLE);
        }

        setUserInfoViews();
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

    private void getDates(){
        HomeUserGetRequest getRequest = new HomeUserGetRequest();
        getRequest.getUserTaskSummary(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), handler);
        getRequest.getEmailInfo(UserInfo.getInstance().getUser().getEmail(), handler);
        getRequest.getFXNoReadCount(UserInfo.getInstance().getUser().getTeacherId(), handler);
        getRequest.getTZNoReadCount(UserInfo.getInstance().getUser().getTeacherId(), handler);
    }

    /**
     * 滚动图片
     */
    private void getBannerDates(){
        HomeUserGetRequest getRequest = new HomeUserGetRequest();
        getRequest.getUserNewsBanner(handler);
    }

    private void updateTaskInfo(TaskSummaryNode node){
        TextView weekAllTV = messageLayout.findViewById(R.id.week_all_tv);
        weekAllTV.setText(String.valueOf(node.getTotalWeekTaskCount()));
        TextView weekDoneTV = messageLayout.findViewById(R.id.week_done_tv);
        weekDoneTV.setText(String.valueOf(node.getTotalWeekDoneTaskCount()));
        TextView weekTodoTV = messageLayout.findViewById(R.id.week_todo_tv);
        weekTodoTV.setText(String.valueOf(node.getTotalWeekToDoTaskCount()));

        TextView totalTTV = messageLayout.findViewById(R.id.db_count_tv);
        totalTTV.setText(String.valueOf(node.getTotalToDoTaskCount()));
    }

    private void setUserInfoViews(){
        TextView schoolTV = (TextView)messageLayout.findViewById(R.id.school_tv);
        schoolTV.setText(UserInfo.getInstance().getUser().getSchoolName());
        TextView nameTV = (TextView)messageLayout.findViewById(R.id.name_tv);
        TextView roleTV = messageLayout.findViewById(R.id.role_tv);
        roleTV.setText(UserInfo.getInstance().getUser().getRoleTypeName());
        nameTV.setText(UserInfo.getInstance().getUser().getTeacherName());
//        ImageView userIV = (ImageView)messageLayout.findViewById(R.id.user_iv);
//        AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
//        try {
//            Bitmap bitmap = asyncBitmapLoader.loadBitmap(userIV, URLData.getUrlFile(UserInfo.getInstance().getUser().getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
//                @Override
//                public void imageLoad(ImageView imageView, Bitmap bitmap) {
//                    imageView.setImageBitmap(bitmap);
//                }
//            });
//            if(bitmap!=null){
//                userIV.setImageBitmap(bitmap);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        URLData
    }

    private View.OnClickListener weekItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(activity, HomeTaskFixedActivity.class);
            if(view.getId()==R.id.week_all_ll){
                intent.putExtra("Type", 1);
            }else if(view.getId()==R.id.week_done_ll){
                intent.putExtra("Type", 2);
            }else if(view.getId()==R.id.week_todo_ll){
                intent.putExtra("Type", 3);
            }
            startActivity(intent);
        }
    };

    private View.OnClickListener itemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.user_db_ll://代办任务
                    intent = new Intent();
                    intent.setClass(activity, HomeTaskAgencyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_kq_ll://考勤
                    intent = new Intent();
                    intent.setClass(activity, HomeAttendanceActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_zl_ll://资料中心
                    intent = new Intent();
                    intent.setClass(activity, HomeFileMenuActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_tz_ll://消息通知
                    intent = new Intent();
                    intent.setClass(activity, HomeMessageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_xw_ll://绘智新闻
                    intent = new Intent();
                    intent.setClass(activity, HomeNewsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_yj_ll://未读邮件
                    intent = new Intent();
                    intent.setClass(activity, HomeEmailInfoActivity.class);
                    if(emailInfoNode!=null)
                        intent.putExtra("ItemUrl", emailInfoNode.getMailboxUrl());
                    startActivity(intent);
                    break;
                case R.id.user_fp_ll://任务分配
                    intent = new Intent();
                    intent.setClass(activity, HomeTaskAllocationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_rz_ll://工作日志
                    intent = new Intent();
                    intent.setClass(activity, HomeWorkDailyActivity.class);
                    activity.startActivity(intent);
                    break;
                case R.id.user_wb_ll://问吧
                    intent = new Intent();
                    intent.setClass(activity, HomeWenBaActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_fx_ll://校区运营分享
                    intent = new Intent();
                    intent.setClass(activity, HomeYunYinFXActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_kc_ll://家校互联
                    intent = new Intent();
                    intent.setClass(activity, CourseListActivity.class);
                    activity.startActivity(intent);
                    break;
                case R.id.user_oa_ll://oa
                    intent = new Intent();
                    intent.setClass(activity, OAActivity.class);
                    activity.startActivity(intent);
                    break;
            }

        }
    };

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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj==null)
                        return;
                    List<BannerNode> bannerNodes = (List<BannerNode>)msg.obj;
                    setNewsBanner(bannerNodes);
                    break;
                case Constants.MSG_SUCCESS_TWO:
                    if(msg.obj==null)
                        return;
                    TaskSummaryNode summaryNode = (TaskSummaryNode)msg.obj;
                    updateTaskInfo(summaryNode);
                    break;
                case Constants.MSG_SUCCESS_THREE:
                    if(msg.obj==null)
                        return;
                    emailInfoNode = (EmailInfoNode)msg.obj;
                    setEmailInfo(emailInfoNode);
                    break;
                case Constants.MSG_SUCCESS_FOUR:
                    if(msg.obj==null)
                        return;
                    String count = (String)msg.obj;
                    Log.i("HuiZhi", "The fx count is:" + count);
                    TextView fxCountTV = messageLayout.findViewById(R.id.fx_count_tv);
                    fxCountTV.setText(count);
                    break;
                case Constants.MSG_SUCCESS_FIVE:
                    if(msg.obj==null)
                        return;
                    String ggcount = (String)msg.obj;
                    Log.i("HuiZhi", "The gg count is:" + ggcount);
                    ImageView ggCountIV = messageLayout.findViewById(R.id.gg_count_iv);
                    if("1".equals(ggcount))
                        ggCountIV.setVisibility(View.INVISIBLE);
                    else
                        ggCountIV.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    private void setNewsBanner(List<BannerNode> bannerNodes){
        if(bannerNodes==null||bannerNodes.size()==0)
            return;
        imgSize = bannerNodes.size();
        FrameLayout bannerFL = messageLayout.findViewById(R.id.banner_fl);
        bannerFL.setVisibility(View.VISIBLE);
        initBanner(bannerNodes);
    }

    private void setEmailInfo(EmailInfoNode node){
        TextView emailCountTV = messageLayout.findViewById(R.id.email_count_tv);
        LinearLayout yjLL = (LinearLayout)messageLayout.findViewById(R.id.user_yj_ll);
        if(node.getTotCount()>0){
            if(node.getTotCount()>=100)
                emailCountTV.setText("...");
            else
                emailCountTV.setText(String.valueOf(node.getTotCount()));//String.valueOf(node.getTotCount())
            yjLL.setOnClickListener(itemOnClick);
        }else{
            emailCountTV.setText("0");
        }

    }

    /**
     * 用户头像加载
     */
    private void setHeadPortrait(){
        String headImg = AsyncFileUpload.getInstance().getFileUrl(UserInfo.getInstance().getUser().getHeadImgUrl());
        Log.i("HuiZhi", "Come into load head portrait:" + headImg);
        try {
            Bitmap bitmap = asyncBitmapLoader.loadBitmap(headIV, headImg, new AsyncBitmapLoader.ImageCallBack() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                    imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            });
            if(bitmap!=null){
                headIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void chargeDate(){
        if(TextUtils.isEmpty(UserInfo.getInstance().getUser().getTeacherId())){
            Intent intent = new Intent();
            intent.setClass(activity, LoginActivity.class);
            startActivity(intent);
            activity.finish();
        }
    }
}
