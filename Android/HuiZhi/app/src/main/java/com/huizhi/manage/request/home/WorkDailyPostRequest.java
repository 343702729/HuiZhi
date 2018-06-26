package com.huizhi.manage.request.home;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.util.JSONUtil;
import com.huizhi.manage.base.ThreadPoolDo;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/12.
 */

public class WorkDailyPostRequest {

    /**
     * 提交工作日报
     * @param teacherid
     * @param schoolid
     * @param workcontent
     * @param workdate
     * @param handler
     */
    public void postWorkDaily(String teacherid, String schoolid, String workcontent, String personalNotes, String workdate, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_USER_WORK_DAILY_SAVE));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        params.add(new BasicNameValuePair("WorkContent", workcontent));
        params.add(new BasicNameValuePair("WorkDate", workdate));
        params.add(new BasicNameValuePair("PersonalNotes", personalNotes));
        ThreadPoolDo.getInstance().executeThread(new WorkDailyThread(params, handler));
    }

    private class WorkDailyThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public WorkDailyThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlUserWorkDailySave(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){

            }
        }
    }
}
