package com.huizhi.manage.request.home;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.http.multipart.MultipartEntity;
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
 * Created by CL on 2018/1/8.
 */

public class HomeTaskPostRequest {

    /**
     * 任务审核通过
     * @param taskid
     * @param userid
     * @param assignreason
     * @param accessories
     * @param handler
     */
    public void postVerifyPass(String taskid, String userid, String assignreason, String accessories, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_VERIFY_PASS));
        params.add(new BasicNameValuePair("TaskId", taskid));
        params.add(new BasicNameValuePair("UserId", userid));
        params.add(new BasicNameValuePair("AssignReason", assignreason));
        params.add(new BasicNameValuePair("AccessoryList", ""));
        Log.i("HuiZhi", "The accessorylist:" + accessories);
        ThreadPoolDo.getInstance().executeThread(new AdminTaskVerifyPass(params, handler));
    }

    /**
     * 任务审核未通过
     * @param taskid
     * @param userid
     * @param refusetype
     * @param assignreason
     * @param accessories
     * @param handler
     */
    public void postVerifyFailed(String taskid, String userid, int refusetype, String assignreason, String accessories, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_VERIFY_FAILED));
        params.add(new BasicNameValuePair("TaskId", taskid));
        params.add(new BasicNameValuePair("UserId", userid));
        params.add(new BasicNameValuePair("RefuseType", String.valueOf(refusetype)));
        params.add(new BasicNameValuePair("AssignReason", assignreason));
        params.add(new BasicNameValuePair("AccessoryList", ""));
        ThreadPoolDo.getInstance().executeThread(new AdminTaskVerifyFailed(params, handler));
    }

    /**
     * 任务重新指派
     * @param currentUserId
     * @param nextUserId
     * @param assignReason
     * @param accessoryLisst
     * @param handler
     */
    public void postReassignTask(String taskid, String currentUserId, String nextUserId, String assignReason, String accessoryLisst, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_REASSIGN));
        params.add(new BasicNameValuePair("TaskId", taskid));
        params.add(new BasicNameValuePair("CurrentUserId", currentUserId));
        params.add(new BasicNameValuePair("NextUserId", nextUserId));
        params.add(new BasicNameValuePair("AssignReason", assignReason));
        params.add(new BasicNameValuePair("AccessoryList", accessoryLisst));
        ThreadPoolDo.getInstance().executeThread(new ReassignThread(params, handler));
    }

    /**
     * 删除任务
     * @param taskid
     * @param handler
     */
    public void postAdminTaskDelete(String taskid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_DELETE));
        params.add(new BasicNameValuePair("TaskId", taskid));
        ThreadPoolDo.getInstance().executeThread(new AdminTaskDelete(params, handler));
    }

    /**
     * 修改任务
     * @param teacherid
     * @param schoolid
     * @param tasklist
     */
    public void postAdminTaskEdit(String teacherid, String schoolid, String tasklist, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_EDIT));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("TaskList", tasklist));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        ThreadPoolDo.getInstance().executeThread(new AdminTaskEdit(params, handler));
    }

    /**
     * 创建任务模板
     * @param teacherid
     * @param schoolid
     * @param categoryName
     * @param taskList
     * @param handler
     */
    public void postMouldTaskCreate(String teacherid, String schoolid, String categoryName, String taskList, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_MOULD_CREATE));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        params.add(new BasicNameValuePair("CategoryName", categoryName));
        params.add(new BasicNameValuePair("TaskTemplateList", taskList));
        ThreadPoolDo.getInstance().executeThread(new TaskMouldCreate(params, handler));
    }

    /**
     * 编辑任务模板
     * @param teacherid
     * @param categoryid
     * @param categoryName
     * @param taskList
     * @param handler
     */
    public void postMouldTaskEdit(String teacherid, String categoryid, String categoryName, String taskList, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_MOULD_EDIT));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("CategoryId", categoryid));
        params.add(new BasicNameValuePair("CategoryName", categoryName));
        params.add(new BasicNameValuePair("TaskTemplateList", taskList));
        ThreadPoolDo.getInstance().executeThread(new TaskMouldEdit(params, handler));
    }

    /**
     * 删除模版
     * @param templateid
     * @param handler
     */
    public void postMouldTaskItemDelete(String templateid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_MOULD_ITEM_DELETE));
        params.add(new BasicNameValuePair("TemplateId", templateid));
        ThreadPoolDo.getInstance().executeThread(new TaskMouldItemDelete(params, handler));
    }

    /**
     * 删除类型
     * @param categoryid
     */
    public void postDeleteMould(String categoryid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_MOULD_DELETE));
        params.add(new BasicNameValuePair("CategoryId", categoryid));
        ThreadPoolDo.getInstance().executeThread(new TaskMouldDelete(params, handler));
    }

    /**
     * 根据模版创建任务
     * @param categoryid
     * @param teacherid
     * @param assignreason
     * @param handler
     */
    public void postCreateMouldTask(String categoryid, String teacherid, String assignreason, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_MOULD_TASK_CREATE));
        params.add(new BasicNameValuePair("CategoryId", categoryid));
        params.add(new BasicNameValuePair("CreateTeacherId", teacherid));
        params.add(new BasicNameValuePair("AssignReason", assignreason));
        ThreadPoolDo.getInstance().executeThread(new MouldTaskCreate(params, handler));
    }

    /**
     * 模版类型排序
     * @param categoryids
     * @param handler
     */
    public void postMoveMouldPosition(String categoryids, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_MOULD_MOVE));
        params.add(new BasicNameValuePair("CategoryList", categoryids));
        ThreadPoolDo.getInstance().executeThread(new TaskMouldMove(params, handler));
    }

    /**
     * 根据系统任务创建任务
     * @param sysTaskid
     * @param createTechid
     * @param schoolid
     * @param isTimelimit
     * @param planEndtime
     * @param priority
     * @param proTechid
     * @param assignReason
     * @param handler
     */
    public void postSystemTask(String sysTaskid, String createTechid, String schoolid, int isTimelimit, String planEndtime,
                               int priority, String proTechid, String assignReason, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_SYSTEM_CREATE));
        params.add(new BasicNameValuePair("SysTaskId", sysTaskid));
        params.add(new BasicNameValuePair("CreateTeacherId", createTechid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        params.add(new BasicNameValuePair("IsTimeLimit", String.valueOf(isTimelimit)));
        params.add(new BasicNameValuePair("PlanEndTime", planEndtime));
        params.add(new BasicNameValuePair("Priority", String.valueOf(priority)));
        params.add(new BasicNameValuePair("ProcessingTeacherId", proTechid));
        params.add(new BasicNameValuePair("AssignReason", assignReason));
        ThreadPoolDo.getInstance().executeThread(new TaskSystemCreateThread(params, handler));
    }

    /**
     * 创建自定义任务
     * @param createTechid
     * @param schoolid
     * @param taskList
     * @param handler
     */
    public void postCustomTask(String createTechid, String schoolid, String taskList, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_CUSTOM_CREATE));
        params.add(new BasicNameValuePair("TaskList", taskList));
        params.add(new BasicNameValuePair("CreateTeacherId", createTechid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
//        params.add(new BasicNameValuePair("AtAll", atall));
        ThreadPoolDo.getInstance().executeThread(new TaskCustomCreateThread(params, handler));
    }

    private class AdminTaskVerifyPass extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public AdminTaskVerifyPass(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTaskVerifyPass(), params);
                Log.i("Task", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Task", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
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

    private class AdminTaskVerifyFailed extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public AdminTaskVerifyFailed(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTaskVerifyFailed(), params);
                Log.i("Task", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Task", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
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

    private class ReassignThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public ReassignThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTaskkReassign(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_THREE, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){

            }
        }
    }

    private class AdminTaskDelete extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public AdminTaskDelete(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTaskDelete(), params);
                Log.i("Task", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Task", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){

            }
        }
    }

    private class AdminTaskEdit extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public AdminTaskEdit(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTaskEdit(), params);
                Log.i("Task", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Task", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class TaskMouldCreate extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskMouldCreate(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTaskMouldCreate(), params);
                Log.i("Task", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Task", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){

            }
        }
    }

    private class TaskMouldEdit extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskMouldEdit(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTaskMouldEdit(), params);
                Log.i("Task", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Task", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){

            }
        }
    }

    private class TaskMouldItemDelete extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskMouldItemDelete(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTaskMouldItemDelete(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
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

    private class TaskMouldDelete extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskMouldDelete(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTaskMouldDelete(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){

            }
        }
    }

    private class MouldTaskCreate extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public MouldTaskCreate(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeMouldTaskCreate(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class TaskMouldMove extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskMouldMove(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTaskMouldMove(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){

            }
        }
    }

    private class TaskSystemCreateThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskSystemCreateThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTasksSystemCreate(), params);
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

    private class TaskCustomCreateThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskCustomCreateThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHomeTasksCustomCreate(), params);
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
