package com.huizhi.manage.request.task;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.node.TaskAccessory;
import com.huizhi.manage.util.JSONUtil;
import com.huizhi.manage.base.ThreadPoolDo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/7.
 */

public class TaskPostRequest {

    /**
     * 完成任务，并提交审核
     * @param taskid
     * @param userid
     * @param nextapproveui
     * @param accessories
     * @param accessories
     */
    public void postTaskFinish(String taskid, String userid, String nextapproveui, String assignreason, String accessories, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_TASK_FINISH));
        params.add(new BasicNameValuePair("TaskId", taskid));
        params.add(new BasicNameValuePair("UserId", userid));
        params.add(new BasicNameValuePair("NextApproveUserId", nextapproveui));
        params.add(new BasicNameValuePair("AssignReason", assignreason));
        params.add(new BasicNameValuePair("AccessoryList", accessories));
        ThreadPoolDo.getInstance().executeThread(new TaskFinishThread(params, handler));

    }

    /**
     * 拒绝执行任务
     * @param taskid
     * @param userid
     * @param refusetype
     * @param assignreason
     * @param accessories
     * @param handler
     */
    public void postTaskUnFinish(String taskid, String userid, int refusetype, String approveuserid, String assignreason, List<TaskAccessory> accessories, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_TASK_UNFINISH));
        params.add(new BasicNameValuePair("TaskId", taskid));
        params.add(new BasicNameValuePair("UserId", userid));
        params.add(new BasicNameValuePair("ApproveUserId", approveuserid));
        params.add(new BasicNameValuePair("RefuseType", String.valueOf(refusetype)));
        params.add(new BasicNameValuePair("AssignReason", assignreason));
        JSONArray accessoriesJs = new JSONArray(accessories);
        String accessStr = null;
        if(accessoriesJs!=null)
            accessStr = accessoriesJs.toString();
        params.add(new BasicNameValuePair("AccessoryList", accessStr));
        ThreadPoolDo.getInstance().executeThread(new TaskUnFinishThread(params, handler));
    }

    private class TaskFinishThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskFinishThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlTaskFinish(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
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

    private class TaskUnFinishThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskUnFinishThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlTaskUnfinish(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
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
