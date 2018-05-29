package com.huizhi.manage.adapter.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huizhi.manage.R;

/**
 * Created by CL on 2018/1/3.
 */

public class CalendarDateAdapter extends BaseAdapter {
    private int[] days = new int[42];
    private Context context;
    private int year;
    private int month;
    private Integer[] dailydo, dailyundo, holidays;

    public CalendarDateAdapter(Context context, int[] days, int year, int month) {
        this.context = context;
        int dayNum = 0;
        //将二维数组转化为一维数组，方便使用
//        for (int i = 0; i < days.length; i++) {
//            for (int j = 0; j < days[i].length; j++) {
//                this.days[dayNum] = days[i][j];
//                dayNum++;
//            }
//        }
        this.days = days;
        this.year = year;
        this.month = month;
    }

    public void updateDates(int[] days, int year, int month){
        int dayNum = 0;
        this.days = days;
        this.year = year;
        this.month = month;
        notifyDataSetChanged();
    }

    public void updateDates(Integer[] dailydo, Integer[] dailyundo, Integer[]holidays){
        this.dailydo = dailydo;
        this.dailyundo = dailyundo;
        this.holidays = holidays;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int i) {
        if (i < 7 && days[i] > 20) {
            return 0;
        } else if (i > 20 && days[i] < 15) {
            return 0;
        }
        return days[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.adapter_calendar_day, null);
            viewHolder = new ViewHolder();
            viewHolder.date_item = (TextView) view.findViewById(R.id.date_item);
            viewHolder.date_statue = (ImageView)view.findViewById(R.id.date_status);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.date_item.setText(days[i] + "");

        viewHolder.date_statue.setVisibility(View.INVISIBLE);

        if(dailydo!=null){
            for(int status:dailydo){
                if(days[i]==status){
                    viewHolder.date_statue.setBackground(context.getResources().getDrawable(R.drawable.frame_daily_do));
                    viewHolder.date_statue.setVisibility(View.VISIBLE);
                    break;
                }
            }

            for(int status:dailyundo){
                if(days[i]==status){
                    viewHolder.date_statue.setBackground(context.getResources().getDrawable(R.drawable.frame_daily_undo));
                    viewHolder.date_statue.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }

        if (i < 7 && days[i] > 20) {
            viewHolder.date_item.setText(" ");
            viewHolder.date_statue.setVisibility(View.INVISIBLE);
//            viewHolder.date_item.setTextColor(Color.rgb(204, 204, 204));//将上个月的和下个月的设置为灰色
        } else if (i > 20 && days[i] < 15) {
            viewHolder.date_item.setText(" ");
            viewHolder.date_statue.setVisibility(View.INVISIBLE);
//            viewHolder.date_item.setTextColor(Color.rgb(204, 204, 204));
        }


        return view;
    }

    class ViewHolder {
        TextView date_item;
        ImageView date_statue;
    }
}
