package com.huizhi.manage.request.task;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.node.TaskAccessory;
import com.huizhi.manage.node.TaskAssignNode;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.util.JSONUtil;
import com.huizhi.manage.base.ThreadPoolDo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/6.
 */

public class TaskGetRequest {

    /**
     * 获取任务列表
     * @param userid
     * @param schoolid
     * @param priority
     * @param taskstatus
     * @param isTimeLimit
     * @param createtime
     * @param sortString
     * @param handler
     */
    public void getTaskList(String userid, String schoolid, int priority, int taskstatus, String isTimeLimit, String createtime, String sortString,  Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_TASK_LIST));
        params.add(new BasicNameValuePair("UserId", userid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        params.add(new BasicNameValuePair("Priority", String.valueOf(priority)));
        params.add(new BasicNameValuePair("TaskStatus", String.valueOf(taskstatus)));
        params.add(new BasicNameValuePair("IsTimeLimit", isTimeLimit));
        params.add(new BasicNameValuePair("CreateTime", createtime));
        params.add(new BasicNameValuePair("SortString", sortString));
        ThreadPoolDo.getInstance().executeThread(new TaskListThread(params, handler));
    }

    /**
     * 获取任务详情
     * @param taskid
     * @param handler
     */
    public void getTaskDetail(String taskid, String userid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_TASK_DETAIL));
        params.add(new BasicNameValuePair("TaskId", taskid));
        params.add(new BasicNameValuePair("UserId", userid));
        ThreadPoolDo.getInstance().executeThread(new TaskDetailThread(params, handler));
    }

    private class TaskListThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskListThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
//                String result = HttpConnect.getHttpConnect(URLData.getUrlTaskList(), params);
                String result = HttpConnect.postHttpConnect(URLData.getUrlTaskList(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    List<TaskNode> nodes = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, nodes));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private List<TaskNode> parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            List<TaskNode> nodes = new ArrayList<>();
            try {
                JSONArray jsonAr = new JSONArray(jsonStr);
                TaskNode node = null;
                for (int i=0; i<jsonAr.length(); i++){
                    JSONObject jsonOb = jsonAr.getJSONObject(i);
                    node = new TaskNode();
                    node.setTaskId(JSONUtil.parseString(jsonOb, "TaskId"));
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
                    node.setLastExecuteTeacherId(JSONUtil.parseString(jsonOb, "LastExecuteTeacherId"));
                    node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                    node.setStrModifyTime(JSONUtil.parseString(jsonOb, "strModifyTime"));
                    node.setIsJoin(JSONUtil.parseBoolean(jsonOb, "IsJoin"));
//                    node.setTaskAssignLst(JSONUtil.parseString(jsonOb, "TaskAssignLst"));
//                    node.setTaskAccessoryLst(JSONUtil.parseString(jsonOb, "TaskAccessoryLst"));
                    nodes.add(node);
                    node = null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return nodes;
        }
    }

    private class TaskDetailThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskDetailThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlTaskDetail(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    TaskNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private TaskNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            TaskNode node = new TaskNode();
            try {
                JSONArray jsonAr = new JSONArray(jsonStr);
                JSONObject jsonOb;
                for(int i=0; i<jsonAr.length(); i++){
                    jsonOb = jsonAr.getJSONObject(i);
                    String parentTaskId = JSONUtil.parseString(jsonOb, "ParentTaskId");
                    if(jsonAr.length()==1||TextUtils.isEmpty(parentTaskId)||"null".equals(parentTaskId)){
                        parseTask(node, jsonOb);
                    }else{
                        TaskNode subNode = new TaskNode();
                        parseTask(subNode, jsonOb);
                        node.addSubTaskNode(subNode);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return node;
        }

        private void parseTask(TaskNode node, JSONObject jsonOb){
            node.setTaskId(JSONUtil.parseString(jsonOb, "TaskId"));
            node.setParentId(JSONUtil.parseString(jsonOb, "ParentTaskId"));
            node.setTaskCode(JSONUtil.parseInt(jsonOb, "TaskCode"));
            node.setSchoolId(JSONUtil.parseString(jsonOb, "SchoolId"));
            node.setTaskTitle(JSONUtil.parseString(jsonOb, "TaskTitle"));
            node.setTaskDescription(JSONUtil.parseString(jsonOb, "TaskDescription"));
            node.setTimeLimit(JSONUtil.parseBoolean(jsonOb, "IsTimeLimit"));
            node.setStrPlanEndTime(JSONUtil.parseString(jsonOb, "StrPlanEndTime"));
            node.setPriority(JSONUtil.parseInt(jsonOb, "Priority"));
            node.setTaskStatus(JSONUtil.parseInt(jsonOb, "TaskStatus"));
            node.setCreateTeacherId(JSONUtil.parseString(jsonOb, "CreateTeacherId"));
            node.setAssignTeacherId(JSONUtil.parseString(jsonOb, "AssignTeacherId"));
            node.setProcessingTeacherId(JSONUtil.parseString(jsonOb, "ProcessingTeacherId"));
            node.setProcessingTeacherName(JSONUtil.parseString(jsonOb, "ProcessingTeacherName"));
            node.setLastExecuteTeacherId(JSONUtil.parseString(jsonOb, "LastExecuteTeacherId"));
            node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
            node.setStrModifyTime(JSONUtil.parseString(jsonOb, "strModifyTime"));
            node.setTakeTimeFromCreateTask(JSONUtil.parseInt(jsonOb, "TakeTimeFromCreateTask"));
            node.setTakeTimeForFinishTask(JSONUtil.parseInt(jsonOb, "TakeTimeForFinishTask"));
            node.setTakeTimeForApproveTask(JSONUtil.parseInt(jsonOb, "TakeTimeForApproveTask"));
            node.setIsJoin(JSONUtil.parseBoolean(jsonOb, "IsJoin"));
            node.setTaskCreateType(JSONUtil.parseInt(jsonOb, "TaskCreateType"));
            node.setStrTaskCreateType(JSONUtil.parseString(jsonOb, "strTaskCreateType"));
            node.setForAllTeacher(JSONUtil.parseBoolean(jsonOb, "ForAllTeacher"));
            node.setCanChooseApprover(JSONUtil.parseInt(jsonOb, "CanChooseApprover"));
            node.setMoreProcessors(JSONUtil.parseBoolean(jsonOb, "IsMoreProcessors"));

            String assignJs = JSONUtil.parseString(jsonOb, "TaskAssignLst");
//                Log.i("Task", "The assign:" + assignJs);
            if(!TextUtils.isEmpty(assignJs)){
                node.setTaskAssignLst(parseTaskAssign(assignJs));
            }

            String accessoryMJs = JSONUtil.parseString(jsonOb, "TaskMainAccessoryLst");
            if(!TextUtils.isEmpty(accessoryMJs)){
                node.setTaskMainAccessoryLst(parseTaskAccessory(accessoryMJs));
            }

            String accessoryJs = JSONUtil.parseString(jsonOb, "TaskAccessoryLst");
//                Log.i("Task", "The accesscory:" + accessoryJs);
            if(!TextUtils.isEmpty(accessoryJs)){
                node.setTaskAccessoryLst(parseTaskAccessory(accessoryJs));
            }

            String cCTeachersJs = JSONUtil.parseString(jsonOb, "CCTeacherList");
            if(!TextUtils.isEmpty(cCTeachersJs)){
                //parseCCTeachers
                node.setcCTeacherLst(parseCCTeachers(cCTeachersJs));
            }

            String processorJs = JSONUtil.parseString(jsonOb, "TaskProcessorLst");
            if(!TextUtils.isEmpty(processorJs)){
                node.setTaskProcessorLst(parseProcessors(processorJs));
            }
        }

        private List<TaskAssignNode> parseTaskAssign(String assignJs){
            if(TextUtils.isEmpty(assignJs))
                return null;
            List<TaskAssignNode> nodes = new ArrayList<>();
            try {
                TaskAssignNode node = null;
                JSONArray jsonAr = new JSONArray(assignJs);
                for(int i=0; i<jsonAr.length(); i++){
                    JSONObject jsonOb = jsonAr.getJSONObject(i);
                    node = new TaskAssignNode();
                    node.setAssignReason(JSONUtil.parseString(jsonOb, "AssignReason"));
                    node.setStrCreateTime(JSONUtil.parseString(jsonOb, "StrCreateTime"));
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
            if(TextUtils.isEmpty(accessoryJs))
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

        private List<UserNode> parseCCTeachers(String cCTeachersJs){
            if(TextUtils.isEmpty(cCTeachersJs))
                return null;
            try {
                JSONArray jsonAr = new JSONArray(cCTeachersJs);
                UserNode userNode = null;
                List<UserNode> users = new ArrayList<>();
                for(int i=0; i<jsonAr.length(); i++){
                    JSONObject itemJs = jsonAr.getJSONObject(i);
                    userNode = new UserNode();
                    userNode.setTeacherId(JSONUtil.parseString(itemJs, "TeacherId"));
                    userNode.setTeacherName(JSONUtil.parseString(itemJs, "TeacherName"));
                    users.add(userNode);
                    userNode = null;
                }
                return  users;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        private List<UserNode> parseProcessors(String processorJs){
            if(TextUtils.isEmpty(processorJs))
                return null;
            try {
                JSONArray jsonAr = new JSONArray(processorJs);
                UserNode userNode = null;
                List<UserNode> users = new ArrayList<>();
                for(int i=0; i<jsonAr.length(); i++){
                    JSONObject itemJs = jsonAr.getJSONObject(i);
                    userNode = new UserNode();
                    userNode.setTeacherId(JSONUtil.parseString(itemJs, "TeacherId"));
                    userNode.setTeacherName(JSONUtil.parseString(itemJs, "TeacherName"));
                    userNode.setFullHeadImgUrlThumb(JSONUtil.parseString(itemJs, "FullHeadImgUrlThumb"));
                    userNode.setIsDone(JSONUtil.parseInt(itemJs, "IsDone"));
                    userNode.setStrIsDone(JSONUtil.parseString(itemJs, "strIsDone"));
                    users.add(userNode);
                    userNode = null;
                }
                return  users;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
