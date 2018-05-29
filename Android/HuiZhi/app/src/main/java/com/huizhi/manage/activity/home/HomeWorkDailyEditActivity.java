package com.huizhi.manage.activity.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.WorkDailyNode;
import com.huizhi.manage.request.home.WorkDailyGetRequest;
import com.huizhi.manage.request.home.WorkDailyPostRequest;
import com.huizhi.manage.util.AppUtil;

/**
 * Created by CL on 2018/1/3.
 * 工作日报
 */

public class HomeWorkDailyEditActivity extends Activity {
    private int year, month, day;
    private WorkDailyNode dailyNode;
    private EditText contentET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_daily_edit);
        initDatas();
        initViews();
        getDates();
    }

    private void initDatas(){
        year = getIntent().getIntExtra("Year", 0);
        month = getIntent().getIntExtra("Month", 0);
        day = getIntent().getIntExtra("Day", 0);
        dailyNode = (WorkDailyNode)getIntent().getSerializableExtra("Item");
        Log.i("Task", "The holidayname:" + dailyNode.getWorkDate());

        contentET = findViewById(R.id.content_et);

        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(dailySubmitClick);
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));
        TextView titleTV = (TextView)findViewById(R.id.record_title);
        titleTV.setText(year + "年" + month + "月" + day + "日");
    }

    private void getDates(){
        String dataM, dataD;
        if(month<10)
            dataM = "0" + month;
        else
            dataM = "" + month;
        if(day<10)
            dataD = "0" + day;
        else
            dataD = "" + day;
        String data = year + dataM + dataD;
        WorkDailyGetRequest getRequest = new WorkDailyGetRequest();
        getRequest.getWorkDailyDate(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), data, handler);
    }

    private View.OnClickListener dailySubmitClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String content = contentET.getText().toString();
            if(TextUtils.isEmpty(content)){
                Toast.makeText(HomeWorkDailyEditActivity.this, "日报内容不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            String dataM, dataD;
            if(month<10)
                dataM = "0" + month;
            else
                dataM = "" + month;
            if(day<10)
                dataD = "0" + day;
            else
                dataD = "" + day;
            String data = year + "-" + dataM + "-" + dataD;
            WorkDailyPostRequest dailyPostR = new WorkDailyPostRequest();
            dailyPostR.postWorkDaily(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), content, data, handler);
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case Constants.MSG_SUCCESS:
                    if(msg.obj==null)
                        return;
                    String messageS = (String)msg.obj;
                    Toast.makeText(HomeWorkDailyEditActivity.this, messageS, Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case Constants.MSG_FAILURE:
                    if(msg.obj==null)
                        return;
                    String messageF = (String)msg.obj;
                    Toast.makeText(HomeWorkDailyEditActivity.this, messageF, Toast.LENGTH_LONG).show();
                    break;
                case Constants.MSG_SUCCESS_ONE:
                    if(msg.obj==null)
                        return;
                    WorkDailyNode node = (WorkDailyNode)msg.obj;
                    setDailyContent(node);
                        break;
            }
        }
    };

    private void setDailyContent(WorkDailyNode node){
        if(node==null)
            return;
        contentET.setText(node.getWorkContent());
    }

}
