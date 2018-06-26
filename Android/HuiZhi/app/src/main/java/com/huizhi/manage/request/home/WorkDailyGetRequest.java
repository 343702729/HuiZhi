package com.huizhi.manage.request.home;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.node.WorkDailyNode;
import com.huizhi.manage.util.JSONUtil;
import com.huizhi.manage.base.ThreadPoolDo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/11.
 */

public class WorkDailyGetRequest {

    /**
     * 取单月的日报列表
     * @param teacherid
     * @param schoolid
     * @param month
     * @param handler
     */
    public void getWorkDailyOfMonth(String teacherid, String schoolid, String month, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_USER_WORK_DAILY_MONTH));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        params.add(new BasicNameValuePair("Month", month));
        ThreadPoolDo.getInstance().executeThread(new WorkDailyOfMonth(params, handler));
    }

    /**
     * 取某一天的日报内容
     * @param teacherid
     * @param schoolid
     * @param data
     * @param handler
     */
    public void getWorkDailyDate(String teacherid, String schoolid, String data, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_USER_WORK_DAILY_DATE));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        params.add(new BasicNameValuePair("Date", data));
        ThreadPoolDo.getInstance().executeThread(new WorkDailyDate(params, handler));
    }

    private class WorkDailyDate extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public WorkDailyDate(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }


        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlUserWorkDailyDate(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    WorkDailyNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, node));
                }else {
//                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private WorkDailyNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;

            WorkDailyNode node = new WorkDailyNode();
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                node.setTimeSheetId(JSONUtil.parseString(jsonOb, "TimeSheetId"));
                node.setTeacherId(JSONUtil.parseString(jsonOb, "TeacherId"));
                node.setWorkContent(JSONUtil.parseString(jsonOb, "WorkContent"));
                node.setPersonalNotes(JSONUtil.parseString(jsonOb, "PersonalNotes"));
                node.setStrWorkDate(JSONUtil.parseString(jsonOb, "strWorkDate"));
                node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
            }catch (Exception e){
                e.printStackTrace();
            }
            return node;
        }
    }

    private class WorkDailyOfMonth extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public WorkDailyOfMonth(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlUserWorkDailyMonth(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    List<WorkDailyNode> nodes = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, nodes));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private List<WorkDailyNode> parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            List<WorkDailyNode> nodes = new ArrayList<>();
            try {
                JSONArray jsonAr = new JSONArray(jsonStr);
                WorkDailyNode node = null;
                for (int i=0; i<jsonAr.length(); i++){
                    JSONObject jsonOb = jsonAr.getJSONObject(i);
                    node = new WorkDailyNode();
                    node.setWorkDate(JSONUtil.parseString(jsonOb, "WorkDate"));
                    node.setHoliday(JSONUtil.parseBoolean(jsonOb, "IsHoliday"));
                    node.setHolidayName(JSONUtil.parseString(jsonOb, "HolidayName"));
                    node.setReported(JSONUtil.parseBoolean(jsonOb, "IsReported"));
                    nodes.add(node);
                    node = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return nodes;
        }
    }
}
