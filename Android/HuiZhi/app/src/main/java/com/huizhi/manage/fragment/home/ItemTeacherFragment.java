package com.huizhi.manage.fragment.home;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huizhi.manage.R;
import com.huizhi.manage.activity.base.HtmlWebActivity;
import com.huizhi.manage.activity.home.course.CourseListActivity;
import com.huizhi.manage.adapter.home.ImagePagerAdapter;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.http.URLHtmlData;
import com.huizhi.manage.node.BannerNode;
import com.huizhi.manage.node.TeacherTrainingNode;
import com.huizhi.manage.request.teacher.TeacherRequest;
import com.huizhi.manage.util.TLog;
import com.huizhi.manage.wiget.GlideCircleTransform;
import com.huizhi.manage.wiget.banner.BannerViewFlow;
import com.huizhi.manage.wiget.banner.PointView;
import com.huizhi.manage.wiget.banner.ViewFlow;
import com.huizhi.manage.wiget.pullableview.PullToRefreshLayout;
import com.huizhi.manage.wiget.view.ItemCourseView;
import com.huizhi.manage.wiget.view.ItemStudyView;

import java.util.ArrayList;
import java.util.List;

public class ItemTeacherFragment extends Fragment {
    private View messageLayout;
    private Activity activity;
    private BannerViewFlow viewFlow;
    private LinearLayout pointsLL;
    private PointView pointView;
    private LinearLayout studyLL;
    private LinearLayout courseLL;
    private PullRefreshListener pullRefreshListener;

    private int imgSize = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.fragment_item_teacher, container, false);
        activity = getActivity();
        initViews();
		getDatas();
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

        studyLL = messageLayout.findViewById(R.id.study_ll);
        courseLL = messageLayout.findViewById(R.id.course_ll);

        TextView nameTV = messageLayout.findViewById(R.id.name_tv);
        nameTV.setText(UserInfo.getInstance().getUser().getTeacherName());

        String headImg = AsyncFileUpload.getInstance().getFileUrl(UserInfo.getInstance().getUser().getHeadImgUrl());
        TLog.log("The headimg is:" + headImg);
        ImageView headIV = messageLayout.findViewById(R.id.user_iv);
        Glide.with(activity).load(headImg)
                .error(R.mipmap.user_icon)
                //圆形
                .transform(new GlideCircleTransform(activity))
                .into(headIV);

        LinearLayout itembjLL = messageLayout.findViewById(R.id.item_bj_ll);
        itembjLL.setOnClickListener(itemClick);

        LinearLayout itemjyLL = messageLayout.findViewById(R.id.item_jy_ll);
        itemjyLL.setOnClickListener(itemClick);

        LinearLayout itemxxLL = messageLayout.findViewById(R.id.item_xx_ll);
        itemxxLL.setOnClickListener(itemClick);

        LinearLayout itemjlLL = messageLayout.findViewById(R.id.item_jl_ll);
        itemjlLL.setOnClickListener(itemClick);

        LinearLayout morexxLL = messageLayout.findViewById(R.id.more_xx_ll);
        morexxLL.setOnClickListener(itemClick);

        LinearLayout morekcLL = messageLayout.findViewById(R.id.more_kc_ll);
        morekcLL.setOnClickListener(itemClick);

    }

    private View.OnClickListener itemClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()){
                case R.id.item_bj_ll:
                    intent = new Intent();
                    intent.setClass(activity, CourseListActivity.class);
//                    activity.startActivity(intent);
                    activity.startActivityForResult(intent, Constants.REQUEST_CODE);
                    break;
                case R.id.item_xx_ll:
                case R.id.more_xx_ll:
                    intent = new Intent(activity, HtmlWebActivity.class);
                    intent.putExtra("Title", "学习");
                    intent.putExtra("Url", URLHtmlData.getStudyListUrl(UserInfo.getInstance().getUser().getTeacherId()));
                    activity.startActivity(intent);
                    break;
                case R.id.item_jy_ll:
                case R.id.more_kc_ll:
                    intent = new Intent(activity, HtmlWebActivity.class);
                    intent.putExtra("Title", "教研");
                    intent.putExtra("Url", URLHtmlData.getTrainingListUrl(UserInfo.getInstance().getUser().getTeacherId()));
                    activity.startActivity(intent);
                    break;
                case R.id.item_jl_ll:
                    intent = new Intent(activity, HtmlWebActivity.class);
                    intent.putExtra("Title", "学习记录");
                    intent.putExtra("Url", URLHtmlData.getTeacherLearning(UserInfo.getInstance().getUser().getTeacherId()));
                    activity.startActivity(intent);
                    break;
            }
        }
    };

    private void getDatas(){
		TeacherRequest request = new TeacherRequest();
		request.getTeacherData(UserInfo.getInstance().getUser().getTeacherId(), handler);
		request.getProgressData(UserInfo.getInstance().getUser().getTeacherId(), handler);
	}

	private void setViewsData(TeacherTrainingNode node){
    	if(node==null)
    		return;
		setNewsBanner(node.getObjBanner());
		addStudyDatas(node.getObjTraining());
		addCourseDatas(node.getObjTeachingTraining());
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
            node.setType(3);
            imgsList.add(node.getImgUrl());
            urlList.add(node.getNewsId());
        }
        viewFlow.setAdapter(new ImagePagerAdapter(activity, imgsList, urlList, titleList, bannerNodes).setInfiniteLoop(true));
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

    private void setProgressData(TeacherTrainingNode.ObjProgress node){
    	if(node==null)
    		return;
		ProgressBar kcPB = messageLayout.findViewById(R.id.kc_pb);
		TextView kcTV = messageLayout.findViewById(R.id.kc_tv);
		kcPB.setMax(node.getTotLessonNum());
		kcPB.setProgress(node.getDoneLessonNum());
		if(!TextUtils.isEmpty(node.getLessonFinishPercent()))
		    kcTV.setText(node.getLessonFinishPercent());

		ProgressBar bkPB = messageLayout.findViewById(R.id.bk_pg);
		TextView bkTV = messageLayout.findViewById(R.id.bk_tv);
		bkPB.setMax(node.getTotLessonNum());
		bkPB.setProgress(node.getDoneLessonPrepareNum());
        if(!TextUtils.isEmpty(node.getLessonPreparedPercent()))
		    bkTV.setText(node.getLessonPreparedPercent());

		ProgressBar pyPB = messageLayout.findViewById(R.id.py_pg);
		TextView pyTV = messageLayout.findViewById(R.id.py_tv);
		pyPB.setMax(node.getCommentedNum());
		pyPB.setProgress(node.getToBeCommentNum());
        if(!TextUtils.isEmpty(node.getCommentFinishPercent()))
		    pyTV.setText(node.getCommentFinishPercent());
	}

    private void addStudyDatas(List<TeacherTrainingNode.ObjTrainingItem> nodes){
        studyLL.removeAllViews();
    	if(nodes==null)
    		return;
    	for (TeacherTrainingNode.ObjTrainingItem node:nodes){
			studyLL.addView(new ItemStudyView(activity, node));
		}

    }

    private void addCourseDatas(List<TeacherTrainingNode.ObjTeachingTrainingItem> nodes){
        courseLL.removeAllViews();
    	if(nodes==null)
    		return;
		for (int i=0; i<nodes.size();){
			if(i>=nodes.size())
				return;
			TeacherTrainingNode.ObjTeachingTrainingItem item1 = null, item2 = null;
			item1 = nodes.get(i);
			i++;
			if(i<nodes.size()) {
				item2 = nodes.get(i);
				i++;
			}

			courseLL.addView(new ItemCourseView(activity, item1, item2));
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
					TeacherTrainingNode node = (TeacherTrainingNode)msg.obj;
					setViewsData(node);
                	break;
				case Constants.MSG_SUCCESS_FOUR:
					TeacherTrainingNode.ObjProgress progressNode = (TeacherTrainingNode.ObjProgress)msg.obj;
					setProgressData(progressNode);
					break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Constants.REQUEST_CODE){
//            Toast.makeText(activity, "Come into teacher activity result", Toast.LENGTH_LONG).show();
            getDatas();
        }
    }
}
