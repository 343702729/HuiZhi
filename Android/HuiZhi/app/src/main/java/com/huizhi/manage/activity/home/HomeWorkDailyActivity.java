package com.huizhi.manage.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huizhi.manage.R;
import com.huizhi.manage.adapter.user.CalendarDateAdapter;
import com.huizhi.manage.base.BackCliclListener;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.node.WorkDailyNode;
import com.huizhi.manage.request.home.WorkDailyGetRequest;
import com.huizhi.manage.util.AppUtil;
import com.huizhi.manage.util.CalendarDateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/3.
 * 工作日报
 */

public class HomeWorkDailyActivity extends Activity {
    private CalendarDateAdapter calendarDateAdapter;
    private int year;
    private int month;
    private TextView workDaysTV;
    private List<WorkDailyNode> dailyNodes;
//    private int[][] daysArray = new int[6][7];
//    private int[] days = new int[42];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_daily_calendar);
        initDate();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDates(year, month);
    }

    private void initDate() {
        //初始化日期数据
        year = CalendarDateUtil.getYear();
        month = CalendarDateUtil.getMonth();
    }

    private void initViews(){
        AppUtil.setNavigationBar(this);
        ImageButton backBtn = (ImageButton)findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new BackCliclListener(this));

        ImageView leftIV = (ImageView)findViewById(R.id.record_left);
        leftIV.setOnClickListener(monthBtnClick);
        ImageView rightIV = (ImageView)findViewById(R.id.record_right);
        rightIV.setOnClickListener(monthBtnClick);

        workDaysTV = findViewById(R.id.work_days_tv);

        setTile();

        GridView gridView = (GridView)findViewById(R.id.gridview);
        gridView.setOnItemClickListener(dailyItemClick);
        int[][] daysArray = CalendarDateUtil.getDayOfMonthFormat(year, month);
        calendarDateAdapter = new CalendarDateAdapter(this, arrangeDailyArray(daysArray), year, month);//传入当前
        gridView.setAdapter(calendarDateAdapter);
//        gridView.setVerticalSpacing(60);
        gridView.setEnabled(true);
    }

    private void getDates(int year, int month){
        WorkDailyGetRequest workDailyRequest = new WorkDailyGetRequest();
        String time = "";
        if(month<10)
            time = year + "0" + month;
        else
            time = year + "" + month;
        Log.i("User", "The time is:" + time);
        workDailyRequest.getWorkDailyOfMonth(UserInfo.getInstance().getUser().getTeacherId(), UserInfo.getInstance().getUser().getSchoolId(), time, handler);
    }

    /**
     * 下一个月
     *
     * @return
     */
    private int[][] nextMonth() {
        if (month == 12) {
            month = 1;
            year++;
        } else {
            month++;
        }
        int[][] daysArray = CalendarDateUtil.getDayOfMonthFormat(year, month);
        return daysArray;
    }

    /**
     * 上一个月
     *
     * @return
     */
    private int[][] prevMonth() {
        if (month == 1) {
            month = 12;
            year--;
        } else {
            month--;
        }
        int[][] daysArray = CalendarDateUtil.getDayOfMonthFormat(year, month);
        return daysArray;
    }

    private int[]  arrangeDailyArray(int[][] daysArray){
        int[] days = new int[42];
        int dayNum = 0;
        for (int i = 0; i < daysArray.length; i++) {
            for (int j = 0; j < daysArray[i].length; j++) {
                days[dayNum] = daysArray[i][j];
                dayNum++;
            }
        }
        return days;
    }

    /**
     * 设置标题
     */
    private void setTile() {
        TextView titleTV = (TextView)findViewById(R.id.record_title);
        String  title = year + "年" + month + "月";
        titleTV.setText(title);
    }

    private View.OnClickListener monthBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Integer[] dailyundo = {4, 11, 18, 25};
//            Integer[] dailydo = {1, 9, 14, 28};
            int[][] daysArray = null;
            switch (view.getId()){
                case R.id.record_left:
                    System.out.println("Come into prev month");
                    daysArray = prevMonth();
                    calendarDateAdapter.updateDates(arrangeDailyArray(daysArray), year, month);
                    getDates(year, month);
                    setTile();
                    break;
                case R.id.record_right:
                    System.out.println("Come into next month");
                    daysArray = nextMonth();
                    calendarDateAdapter.updateDates(arrangeDailyArray(daysArray), year, month);
                    getDates(year, month);
                    setTile();
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener dailyItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            int day = (int)calendarDateAdapter.getItem(i);
            int nowY = CalendarDateUtil.getYear();
            int nowM = CalendarDateUtil.getMonth();
            int nowD = CalendarDateUtil.getCurrentDayOfMonth();
            System.out.println("The daily is:" + day + "  i:" + i);
            if(nowY>year){
                toEdit(day);
                return;
            }
            if(nowY==year){
                if(nowM>month){
                    toEdit(day);
                    return;
                }

                if(nowM==month){
                    if(nowD>=day){
                        toEdit(day);
                        return;
                    }
                }
            }
            Toast.makeText(HomeWorkDailyActivity.this, "该日期还无法编写日报", Toast.LENGTH_LONG).show();
        }

        private void toEdit(int day){
            Intent intent = new Intent();
            intent.setClass(HomeWorkDailyActivity.this, HomeWorkDailyEditActivity.class);
            intent.putExtra("Year", year);
            intent.putExtra("Month", month);
            intent.putExtra("Day", day);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Item", dailyNodes.get(day-1));
            intent.putExtras(bundle);
            startActivity(intent);
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
                    dailyNodes = (List<WorkDailyNode>)msg.obj;
                    Log.i("User", "The daily size:" + dailyNodes.size());
                    updateWorkDailyDate(dailyNodes);
                    break;
            }
        }
    };

    private void updateWorkDailyDate(List<WorkDailyNode> nodes){
        if(nodes==null)
            return;
        Integer[] dailydo = null, dailyundo = null, holidays = null;
        List<Integer> doList = new ArrayList<>();
        List<Integer> undoList = new ArrayList<>();
        List<Integer> holidayList = new ArrayList<>();
        for(WorkDailyNode node:nodes){
            if(node.isHoliday()){
                Integer day = getDay(node.getWorkDate());
                if(day==0)
                    continue;
                else
                    holidayList.add(day);
                continue;
            }

            if(node.isReported()){
                Integer day = getDay(node.getWorkDate());
                if(day==0)
                    continue;
                else
                    doList.add(day);
                continue;
            }else if(!node.isHoliday()){
                Integer day = getDay(node.getWorkDate());
                if(day==0)
                    continue;
                else
                    undoList.add(day);
            }
        }
        workDaysTV.setText(undoList.size() + "天");
        dailydo = (Integer[])doList.toArray(new Integer[doList.size()]);
        dailyundo = (Integer[])undoList.toArray(new Integer[undoList.size()]);
        holidays = (Integer[])holidayList.toArray(new Integer[holidayList.size()]);

        calendarDateAdapter.updateDates(dailydo, dailyundo, holidays);
    }

    private int getDay(String data){
        if(TextUtils.isEmpty(data))
            return 0;
        String day = data.substring(6, data.length());
//        if(day.startsWith("0"))
//            day = day.replace("0", "");
        Log.i("User", "The day is:" + day);
        try{
            return Integer.parseInt(day);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
