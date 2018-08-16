package com.huizhi.manage.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.rong.imageloader.utils.L;

/**
 * Created by CL on 2017/12/14.
 */

public class TaskNode implements Serializable {
    private int id;
//    private String uniqueId = "";
    private String parentId = "";
    private String taskId;
    private int taskCode;
    private String schoolId;
    private String taskTitle = "";
    private String taskDescription;
    private boolean isTimeLimit;            //true：限时任务1 false：一般任务0
    private String strPlanEndTime;
    private int priority;                   //3,高2,中 1,低
    private int taskStatus;                 //1, 新创建 2，待审核 3, 已完成 4, 已拒绝 5, 已关闭
    private String createTeacherId;
    private String createTeacherName;
    private String assignTeacherId;
    private String processingTeacherId;
    private String processingTeacherName;
    private int taskCreateType;
    private String strTaskCreateType;
    private String ccTeacherId;
    private String atAll;
    private boolean isJoin;
    private boolean forAllTeacher;
    private String lastExecuteTeacherId;
    private String strCreateTime;
    private String strModifyTime;
    private int takeTimeFromCreateTask = 0;
    private int takeTimeForFinishTask = 0;
    private int takeTimeForApproveTask = 0;
    private int canChooseApprover;
    private boolean isMoreProcessors;
    private List<TaskAssignNode> taskAssignLst;
    private List<TaskAccessory> taskAccessoryLst = new ArrayList<>();
    private List<TaskAccessory> taskMainAccessoryLst = new ArrayList<>();
    private List<TaskNode> subTaskNodes = new ArrayList<>();
    private List<UserNode> cCTeacherLst = new ArrayList<>();
    private List<UserNode> taskProcessorLst = new ArrayList<>();

    public int getId() {
        return id;
    }

//    public String getUniqueId() {
////        return uniqueId;
////    }
//
//    public void setUniqueId(String uniqueId) {
//        this.uniqueId = uniqueId;
//    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(int taskCode) {
        this.taskCode = taskCode;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public boolean isTimeLimit() {
        return isTimeLimit;
    }

    public void setTimeLimit(boolean timeLimit) {
        isTimeLimit = timeLimit;
    }

    public String getStrPlanEndTime() {
        return strPlanEndTime;
    }

    public void setStrPlanEndTime(String strPlanEndTime) {
        this.strPlanEndTime = strPlanEndTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCreateTeacherId() {
        return createTeacherId;
    }

    public void setCreateTeacherId(String createTeacherId) {
        this.createTeacherId = createTeacherId;
    }

    public String getCreateTeacherName() {
        return createTeacherName;
    }

    public void setCreateTeacherName(String createTeacherName) {
        this.createTeacherName = createTeacherName;
    }

    public String getAssignTeacherId() {
        return assignTeacherId;
    }

    public void setAssignTeacherId(String assignTeacherId) {
        this.assignTeacherId = assignTeacherId;
    }

    public String getProcessingTeacherId() {
        return processingTeacherId;
    }

    public void setProcessingTeacherId(String processingTeacherId) {
        this.processingTeacherId = processingTeacherId;
    }

    public String getProcessingTeacherName() {
        return processingTeacherName;
    }

    public void setProcessingTeacherName(String processingTeacherName) {
        this.processingTeacherName = processingTeacherName;
    }

    public int getTaskCreateType() {
        return taskCreateType;
    }

    public void setTaskCreateType(int taskCreateType) {
        this.taskCreateType = taskCreateType;
    }

    public String getStrTaskCreateType() {
        return strTaskCreateType;
    }

    public void setStrTaskCreateType(String strTaskCreateType) {
        this.strTaskCreateType = strTaskCreateType;
    }

    public String getCcTeacherId() {
        return ccTeacherId;
    }

    public void setCcTeacherId(String ccTeacherId) {
        this.ccTeacherId = ccTeacherId;
    }

    public String getAtAll() {
        return atAll;
    }

    public void setAtAll(String atAll) {
        this.atAll = atAll;
    }

    public boolean getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(boolean isJoin) {
        this.isJoin = isJoin;
    }

    public boolean isForAllTeacher() {
        return forAllTeacher;
    }

    public void setForAllTeacher(boolean forAllTeacher) {
        this.forAllTeacher = forAllTeacher;
    }

    public String getLastExecuteTeacherId() {
        return lastExecuteTeacherId;
    }

    public void setLastExecuteTeacherId(String lastExecuteTeacherId) {
        this.lastExecuteTeacherId = lastExecuteTeacherId;
    }

    public String getStrCreateTime() {
        return strCreateTime;
    }

    public void setStrCreateTime(String strCreateTime) {
        this.strCreateTime = strCreateTime;
    }

    public String getStrModifyTime() {
        return strModifyTime;
    }

    public void setStrModifyTime(String strModifyTime) {
        this.strModifyTime = strModifyTime;
    }

    public List<TaskAssignNode> getTaskAssignLst() {
        return taskAssignLst;
    }

    public void setTaskAssignLst(List<TaskAssignNode> taskAssignLst) {
        this.taskAssignLst = taskAssignLst;
    }

    public List<TaskAccessory> getTaskAccessoryLst() {
        return taskAccessoryLst;
    }

    public void setTaskAccessoryLst(List<TaskAccessory> taskAccessoryLst) {
        this.taskAccessoryLst = taskAccessoryLst;
    }

    public List<TaskAccessory> getTaskMainAccessoryLst() {
        return taskMainAccessoryLst;
    }

    public void setTaskMainAccessoryLst(List<TaskAccessory> taskMainAccessoryLst) {
        this.taskMainAccessoryLst = taskMainAccessoryLst;
    }

    public int getTakeTimeFromCreateTask() {
        return takeTimeFromCreateTask;
    }

    public void setTakeTimeFromCreateTask(int takeTimeFromCreateTask) {
        this.takeTimeFromCreateTask = takeTimeFromCreateTask;
    }

    public int getTakeTimeForFinishTask() {
        return takeTimeForFinishTask;
    }

    public void setTakeTimeForFinishTask(int takeTimeForFinishTask) {
        this.takeTimeForFinishTask = takeTimeForFinishTask;
    }

    public int getTakeTimeForApproveTask() {
        return takeTimeForApproveTask;
    }

    public void setTakeTimeForApproveTask(int takeTimeForApproveTask) {
        this.takeTimeForApproveTask = takeTimeForApproveTask;
    }

    public int getCanChooseApprover() {
        return canChooseApprover;
    }

    public void setCanChooseApprover(int canChooseApprover) {
        this.canChooseApprover = canChooseApprover;
    }

    public boolean isMoreProcessors() {
        return isMoreProcessors;
    }

    public void setMoreProcessors(boolean moreProcessors) {
        isMoreProcessors = moreProcessors;
    }

    public List<TaskNode> getSubTaskNodes() {
        return subTaskNodes;
    }

    public void setSubTaskNodes(List<TaskNode> subTaskNodes) {
        this.subTaskNodes = subTaskNodes;
    }

    public void addSubTaskNode(TaskNode taskNode){
        subTaskNodes.add(taskNode);
    }

    public List<UserNode> getcCTeacherLst() {
        return cCTeacherLst;
    }

    public void setcCTeacherLst(List<UserNode> cCTeacherLst) {
        this.cCTeacherLst = cCTeacherLst;
    }

    public List<UserNode> getTaskProcessorLst() {
        return taskProcessorLst;
    }

    public void setTaskProcessorLst(List<UserNode> taskProcessorLst) {
        this.taskProcessorLst = taskProcessorLst;
    }

    public String getTaskType(){
        if(isTimeLimit)
            return "限时任务";
        else
            return "一般任务";
    }
}
