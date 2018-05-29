package com.huizhi.manage.activity.home.task;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.AsyncBitmapLoader;
import com.huizhi.manage.http.AsyncFileUpload;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.PictureUtil;

/**
 * Created by CL on 2018/2/26.
 * 任务详情
 */

public class HomeTaskMouldSubInfoActivity extends Activity {
    private TaskMouldNode taskNode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_task_mould_sub_info);
        getIntentDate();
        initViews();
    }

    private void getIntentDate(){
        taskNode = (TaskMouldNode)getIntent().getSerializableExtra("Item");
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton) findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        TextView titleTV = findViewById(R.id.title_tv);
        TextView descriptionTV = findViewById(R.id.description_tv);

        if(taskNode!=null){
            titleTV.setText(taskNode.getTaskTitle());
            descriptionTV.setText(taskNode.getTaskDescription());
            priorityBtnSel(taskNode.getPriority());
            setPersonInfo(taskNode.getExecuteTeacherId());
        }
    }

    private void priorityBtnSel(int index){
        Button priHBtn = (Button)findViewById(R.id.pro_h_btn);
        Button priMbtn = (Button)findViewById(R.id.pro_m_btn);
        Button priLBtn = (Button)findViewById(R.id.pro_l_btn);

        priHBtn.setVisibility(View.GONE);
        priMbtn.setVisibility(View.GONE);
        priLBtn.setVisibility(View.GONE);
        if(index==3){
            priHBtn.setBackground(getResources().getDrawable(R.drawable.frame_bg_red));
            priHBtn.setTextColor(getResources().getColor(R.color.white));
            priHBtn.setVisibility(View.VISIBLE);
        }else if(index==2){
            priMbtn.setBackground(getResources().getDrawable(R.drawable.frame_bg_red));
            priMbtn.setTextColor(getResources().getColor(R.color.white));
            priMbtn.setVisibility(View.VISIBLE);
        }else if(index==1){
            priLBtn.setBackground(getResources().getDrawable(R.drawable.frame_bg_red));
            priLBtn.setTextColor(getResources().getColor(R.color.white));
            priLBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setPersonInfo(String personId){
        TextView personTV = findViewById(R.id.person_tv);
        ImageView personIV = findViewById(R.id.person_iv);

        if(TextUtils.isEmpty(personId)){
            personTV.setVisibility(View.GONE);
            personIV.setVisibility(View.GONE);
            return;
        }
        UserNode user = UserInfo.getInstance().getUserByTeacherId(personId);
        if(user==null)
            return;
        personTV.setText(user.getTeacherName());
        personIV.setImageBitmap(null);
        personIV.setBackgroundResource(R.mipmap.user_icon);
        try {
            AsyncBitmapLoader asyncBitmapLoader = new AsyncBitmapLoader();
//            asyncBitmapLoader.showPicByVolleyRequest(HomeTaskMouldSubFoundActivity.this, URLData.getUrlFile(UserInfo.getInstance().getUser().getHeadImgUrl()), personIV);
            Bitmap bitmap = asyncBitmapLoader.loadBitmapFromServer(personIV, AsyncFileUpload.getInstance().getFileUrl(user.getHeadImgUrl()), new AsyncBitmapLoader.ImageCallBack() {
                @Override
                public void imageLoad(ImageView imageView, Bitmap bitmap) {
                    imageView.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
                }
            });
            if(bitmap!=null){
                personIV.setImageBitmap(PictureUtil.toRoundBitmap(bitmap));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        personTV.setVisibility(View.VISIBLE);
        personIV.setVisibility(View.VISIBLE);
    }
}
