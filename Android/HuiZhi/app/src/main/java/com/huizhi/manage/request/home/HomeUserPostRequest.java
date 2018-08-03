package com.huizhi.manage.request.home;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.base.ThreadPoolDo;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.AttendanceInfoNode;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.util.JSONUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/24.
 */

public class HomeUserPostRequest {

    /**
     * 提交考勤信息
     * @param teacherid
     * @param attendanceType 1：上班  2：下班
     * @param isInRegion      1：在考勤范围 2：不在考勤范围
     * @param notInReason
     * @param handler
     */
    public void postUserAttendance(String teacherid, String schoolid, int attendanceType, int isInRegion, String notInReason, String lateReason, String earlyReason, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_ATTENDANCE_SUBMIT));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        params.add(new BasicNameValuePair("AttendanceType", String.valueOf(attendanceType)));
        params.add(new BasicNameValuePair("IsInTheRegion", String.valueOf(isInRegion)));
        params.add(new BasicNameValuePair("NotInRegionReason", notInReason));
        params.add(new BasicNameValuePair("ClockInLateReason", lateReason));
        params.add(new BasicNameValuePair("ClockOutEarlyReason", earlyReason));
        ThreadPoolDo.getInstance().executeThread(new UserAttendanceSubmint(params, handler));
    }

    /**
     * 判断是否在考勤区域
     * @param schoolid
     * @param lat
     * @param lng
     * @param handler
     */
    public void postUserAttendanceIsInRegion(String schoolid, double lat, double lng, String macAddress, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_ATTENDANCE_IS_IN_REGION));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        params.add(new BasicNameValuePair("Longitude", String.valueOf(lng)));
        params.add(new BasicNameValuePair("Latitude", String.valueOf(lat)));
        params.add(new BasicNameValuePair("MacAddress", macAddress));
        ThreadPoolDo.getInstance().executeThread(new AttendanceIsInRegion(params, handler));
    }

    private class UserAttendanceSubmint extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public UserAttendanceSubmint(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeAttendanceSubmit(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_TWO, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){

            }
        }
    }

    private class AttendanceIsInRegion extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public AttendanceIsInRegion(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeAttendanceIsInRegion(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    AttendanceInfoNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, node));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){

            }
        }

        private AttendanceInfoNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            AttendanceInfoNode node = new AttendanceInfoNode();
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                node.setInRegion(JSONUtil.parseBoolean(jsonOb, "IsInTheRegion"));
            }catch (Exception e){

            }
            return node;
        }
    }
}
