package com.huizhi.manage.request.home;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.node.TaskAccessory;
import com.huizhi.manage.node.TaskAssignNode;
import com.huizhi.manage.node.TaskCategoryNode;
import com.huizhi.manage.node.TaskMouldNode;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.TaskPageNode;
import com.huizhi.manage.node.TaskSystemNode;
import com.huizhi.manage.util.JSONUtil;
import com.huizhi.manage.base.ThreadPoolDo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/8.
 */

public class HomeTaskGetRequest {

    /**
     * 获取类型列表
     * @param teacherid
     * @param handler
     */
    public void getMouldTasks(String teacherid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_MOULD_LIST));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        ThreadPoolDo.getInstance().executeThread(new MouldTaskListThread(params, handler));
    }

    /**
     * 加载任务模板详情的接口
     * @param categoryid
     * @param handler
     */
    public void getMouldTaskDetail(String categoryid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_MOULD_DETAIL));
        params.add(new BasicNameValuePair("CategoryId", categoryid));
        ThreadPoolDo.getInstance().executeThread(new MouldTaskDetail(params, handler));
    }

    /**
     * 查询任务列表
     * @param currentPage
     * @param pageSize
     * @param teacherid
     * @param schoolid
     * @param taskStatus
     * @param teacherName
     * @param taskTitle
     * @param priority
     * @param isTimeLimit
     * @param createTime
     * @param sortStr
     * @param handler
     */
    public void getAdminTaskList(int currentPage, int pageSize, String teacherid, String schoolid, int taskStatus, String teacherName, String taskTitle, int priority, String isTimeLimit, String createTime, String executeTechId, String sortStr, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_LIST));
        params.add(new BasicNameValuePair("currentPage", String.valueOf(currentPage)));
        params.add(new BasicNameValuePair("pageSize", String.valueOf(pageSize)));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        params.add(new BasicNameValuePair("TaskStatus", String.valueOf(taskStatus)));
        params.add(new BasicNameValuePair("TeacherName", teacherName));
        params.add(new BasicNameValuePair("TaskTitle", taskTitle));
        params.add(new BasicNameValuePair("Priority", String.valueOf(priority)));
        params.add(new BasicNameValuePair("IsTimeLimit", isTimeLimit));
        params.add(new BasicNameValuePair("CreateTime", createTime));
        params.add(new BasicNameValuePair("ExecuteTeacherId", executeTechId));
        params.add(new BasicNameValuePair("SortString", sortStr));
        ThreadPoolDo.getInstance().executeThread(new AdminTaskListThread(params, handler));
    }

    /**
     * 获取系统任务分类
     * @param userid
     * @param handler
     */
    public void getSystemTaskCategory(String userid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASK_SYSTEM_CATEGORY));
        params.add(new BasicNameValuePair("UserId", userid));
        ThreadPoolDo.getInstance().executeThread(new TaskCategoryThread(params, handler));
    }

    /**
     * 获取系统任务信息
     * @param userid
     * @param handler
     */
    public void getSystemTasks(String userid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_TASKS_SYSTEM));
        params.add(new BasicNameValuePair("UserId", userid));
        ThreadPoolDo.getInstance().executeThread(new TasksSystemThread(params, handler));
    }

    private class MouldTaskListThread extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public MouldTaskListThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeTaskMouldList(), params);
                Log.i("Home", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Home", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    List<TaskMouldNode> nodes = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, nodes));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private List<TaskMouldNode> parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            List<TaskMouldNode> nodes = new ArrayList<>();
            try {
//                JSONObject jsongOb = new JSONObject(jsonStr);
//                JSONArray jsonAr = jsongOb.getJSONArray("Items");
                JSONArray jsonAr = new JSONArray(jsonStr);
                TaskMouldNode node = null;
                for (int i=0; i<jsonAr.length(); i++){
                    JSONObject jsonOb = jsonAr.getJSONObject(i);
                    node = new TaskMouldNode();
                    node.setCategoryId(JSONUtil.parseString(jsonOb, "CategoryId"));
                    node.setCategoryName(JSONUtil.parseString(jsonOb, "CategoryName"));
                    node.setTeacherId(JSONUtil.parseString(jsonOb, "TeacherId"));
                    node.setSchoolId(JSONUtil.parseString(jsonOb, "SchoolId"));
                    node.setSequenceNumber(JSONUtil.parseInt(jsonOb, "SequenceNumber"));
                    node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                    nodes.add(node);
                    node = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return nodes;
        }
    }

    private class MouldTaskDetail extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public MouldTaskDetail(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeTaskMouldDetail(), params);
                Log.i("Home", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Home", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    TaskMouldNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private TaskMouldNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            TaskMouldNode node = new TaskMouldNode();
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                node.setCategoryId(JSONUtil.parseString(jsonOb, "CategoryId"));
                node.setCategoryName(JSONUtil.parseString(jsonOb, "CategoryName"));
                String tasks = JSONUtil.parseString(jsonOb, "TaskTemplateList");
                if(!TextUtils.isEmpty(tasks)){
                    JSONArray jsonAr = new JSONArray(tasks);
                    List<TaskMouldNode> taskNodes = new ArrayList<>();
                    TaskMouldNode itemTn;
                    JSONObject itemJs;
                    for (int i=0; i<jsonAr.length(); i++){
                        itemJs = jsonAr.getJSONObject(i);
                        itemTn = new TaskMouldNode();
                        itemTn.setTemplateId(JSONUtil.parseString(itemJs, "TemplateId"));
                        itemTn.setCategoryId(JSONUtil.parseString(itemJs, "CategoryId"));
                        itemTn.setCategoryName(JSONUtil.parseString(itemJs, "CategoryName"));
                        itemTn.setTeacherId(JSONUtil.parseString(itemJs, "TeacherId"));
                        itemTn.setTaskTitle(JSONUtil.parseString(itemJs, "TaskTitle"));
                        itemTn.setTaskDescription(JSONUtil.parseString(itemJs, "TaskDescription"));
                        itemTn.setPriority(JSONUtil.parseInt(itemJs, "Priority"));
                        itemTn.setExecuteTeacherId(JSONUtil.parseString(itemJs, "ExecuteTeacherId"));
                        itemTn.setStrCreateTime(JSONUtil.parseString(itemJs, "strCreateTime"));
                        taskNodes.add(itemTn);
                        itemTn = null;
                        itemJs = null;
                    }
                    node.setTaskTemplateList(taskNodes);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return node;
        }
    }

    private class AdminTaskListThread extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public AdminTaskListThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeTaskList(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    TaskPageNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private TaskPageNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            List<TaskNode> nodes = new ArrayList<>();
            TaskPageNode pageNode = new TaskPageNode();
            try {
                JSONObject jsongOb = new JSONObject(jsonStr);
                pageNode.setMyAssigned(JSONUtil.parseInt(jsongOb, "MyAssigned"));
                pageNode.setMyToBeApprove(JSONUtil.parseInt(jsongOb, "MyToBeApprove"));
                pageNode.setTotCount(JSONUtil.parseInt(jsongOb, "TotCount"));
                pageNode.setTotPageCount(JSONUtil.parseInt(jsongOb, "TotPageCount"));
                Log.i("HuiZhi", "The server totalpage:" + pageNode.getTotPageCount());
                JSONArray jsonAr = jsongOb.getJSONArray("Items");
//                JSONArray jsonAr = new JSONArray(jsonStr);
                TaskNode node = null;
                for (int i=0; i<jsonAr.length(); i++){
                    JSONObject jsonOb = jsonAr.getJSONObject(i);
                    node = new TaskNode();
                    String taskid = JSONUtil.parseString(jsonOb, "TaskId");
                    node.setTaskId(taskid);
                    node.setParentId(JSONUtil.parseString(jsonOb, "ParentTaskId"));
                    node.setTaskCode(JSONUtil.parseInt(jsonOb, "TaskCode"));
                    node.setSchoolId(JSONUtil.parseString(jsonOb, "SchoolId"));
                    node.setTaskTitle(JSONUtil.parseString(jsonOb, "TaskTitle"));
                    node.setTaskDescription(JSONUtil.parseString(jsonOb, "TaskDescription"));
                    node.setTimeLimit(JSONUtil.parseBoolean(jsonOb, "IsTimeLimit"));
                    node.setStrPlanEndTime(JSONUtil.parseString(jsonOb, "strPlanEndTime"));
                    node.setPriority(JSONUtil.parseInt(jsonOb, "Priority"));
                    node.setTaskStatus(JSONUtil.parseInt(jsonOb, "TaskStatus"));
                    node.setCreateTeacherId(JSONUtil.parseString(jsonOb, "CreateTeacherId"));
                    node.setAssignTeacherId(JSONUtil.parseString(jsonOb, "AssignTeacherId"));
                    node.setProcessingTeacherId(JSONUtil.parseString(jsonOb, "ProcessingTeacherId"));
                    node.setProcessingTeacherName(JSONUtil.parseString(jsonOb, "ProcessingTeacherName"));
                    node.setLastExecuteTeacherId(JSONUtil.parseString(jsonOb, "LastExecuteTeacherId"));
                    node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                    node.setStrModifyTime(JSONUtil.parseString(jsonOb, "strModifyTime"));
                    node.setIsJoin(JSONUtil.parseBoolean(jsonOb, "IsJoin"));
                    String assignJs = JSONUtil.parseString(jsonOb, "TaskAssignLst");
//                Log.i("Task", "The assign:" + assignJs);
                    if(!TextUtils.isEmpty(assignJs)){
                        node.setTaskAssignLst(parseTaskAssign(assignJs));
                    }
                    String accessoryJs = JSONUtil.parseString(jsonOb, "TaskAccessoryLst");
//                Log.i("Task", "The accesscory:" + accessoryJs);
                    if(!TextUtils.isEmpty(accessoryJs)){
                        node.setTaskAccessoryLst(parseTaskAccessory(accessoryJs));
                    }
                    nodes.add(node);
                    node = null;
                }
                pageNode.setTasks(nodes);
            }catch (Exception e){
                e.printStackTrace();
            }
            return pageNode;
        }

        private List<TaskAssignNode> parseTaskAssign(String assignJs){
            if(TextUtils.isEmpty(assignJs)||"null".equals(assignJs))
                return null;
            List<TaskAssignNode> nodes = new ArrayList<>();
            try {
                TaskAssignNode node = null;
                JSONArray jsonAr = new JSONArray(assignJs);
                for(int i=0; i<jsonAr.length(); i++){
                    JSONObject jsonOb = jsonAr.getJSONObject(i);
                    node = new TaskAssignNode();
                    node.setAssignReason(JSONUtil.parseString(jsonOb, "AssignReason"));
                    node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                    node.setMessage(JSONUtil.parseString(jsonOb, "Message"));
                    node.setHeadImgUrl(JSONUtil.parseString(jsonOb, "FullOriginalHeadImgUrlThumb"));
                    nodes.add(node);
                    node = null;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return nodes;
        }

        private List<TaskAccessory> parseTaskAccessory(String accessoryJs){
            if(TextUtils.isEmpty(accessoryJs)||"null".equals(accessoryJs))
                return null;
            List<TaskAccessory> nodes = new ArrayList<>();
            try {
                TaskAccessory node = null;
                JSONArray jsonAr = new JSONArray(accessoryJs);
                for(int i=0; i<jsonAr.length(); i++){
                    JSONObject jsonOb = jsonAr.getJSONObject(i);
                    node = new TaskAccessory();
                    node.setAccessoryId(JSONUtil.parseString(jsonOb, "AccessoryId"));
                    node.setTaskId(JSONUtil.parseString(jsonOb, "TaskId"));
                    node.setAssignId(JSONUtil.parseString(jsonOb, "AssignId"));
                    node.setAccessoryType(JSONUtil.parseInt(jsonOb, "AccessoryType"));
                    node.setFileType(JSONUtil.parseInt(jsonOb, "FileType"));
                    node.setFileUrl(JSONUtil.parseString(jsonOb, "FileUrl"));
                    node.setFileSize(JSONUtil.parseString(jsonOb, "FileSize"));
                    node.setFileName(JSONUtil.parseString(jsonOb, "FileName"));
                    node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                    nodes.add(node);
                    node = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return nodes;
        }
    }

    private class TaskCategoryThread extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public TaskCategoryThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeTaskSystemCategory(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    List<TaskCategoryNode> taskCategoryNodes = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, taskCategoryNodes));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private List<TaskCategoryNode> parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            List<TaskCategoryNode> nodes = new ArrayList<>();
            try {
                TaskCategoryNode node = null;
                JSONObject jsonOb = null;
                JSONArray jsonAr = new JSONArray(jsonStr);
                for(int i=0; i<jsonAr.length(); i++){
                    jsonOb = jsonAr.getJSONObject(i);
                    node = new TaskCategoryNode();
                    node.setCategoryId(JSONUtil.parseString(jsonOb, "CategoryId"));
                    node.setCategoryName(JSONUtil.parseString(jsonOb, "CategoryName"));
                    node.setSequenceNumber(JSONUtil.parseInt(jsonOb, "SequenceNumber"));
                    node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                    nodes.add(node);
                    jsonOb = null;
                    node = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return nodes;
        }
    }

    private class TasksSystemThread extends Thread{
        private List<BasicNameValuePair> params;
        private android.os.Handler handler;

        public TasksSystemThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeTasksSystem(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    List<TaskSystemNode> taskCategoryNodes = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, taskCategoryNodes));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private List<TaskSystemNode> parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            List<TaskSystemNode> nodes = new ArrayList<>();
            try {
                TaskSystemNode node = null;
                JSONObject jsonOb = null;
                JSONArray jsonAr = new JSONArray(jsonStr);
                for(int i=0; i<jsonAr.length(); i++){
                    jsonOb = jsonAr.getJSONObject(i);
                    node = new TaskSystemNode();
                    node.setTaskId(JSONUtil.parseString(jsonOb, "TaskId"));
                    node.setTaskTitle(JSONUtil.parseString(jsonOb, "TaskTitle"));
                    node.setTaskDescription(JSONUtil.parseString(jsonOb, "TaskDescription"));
                    node.setCategoryId(JSONUtil.parseString(jsonOb, "CategoryId"));
                    node.setCategoryName(JSONUtil.parseString(jsonOb, "CategoryName"));
                    node.setSequenceNumber(JSONUtil.parseInt(jsonOb, "SequenceNumber"));
                    node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                    nodes.add(node);
                    jsonOb = null;
                    node = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return nodes;
        }
    }
}
