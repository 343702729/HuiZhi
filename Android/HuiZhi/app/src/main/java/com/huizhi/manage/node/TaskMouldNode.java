package com.huizhi.manage.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2017/12/19.
 */

public class TaskMouldNode implements Serializable{
    private String uniqueId;
    private String categoryId;
    private String categoryName;
    private String teacherId;
    private String schoolId;
    private int sequenceNumber;
    private String strCreateTime;
    private String templateId;
    private String taskTitle;
    private String taskDescription;
    private int priority;
    private String executeTeacherId;
    private List<TaskMouldNode> taskTemplateList = new ArrayList<>();

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getStrCreateTime() {
        return strCreateTime;
    }

    public void setStrCreateTime(String strCreateTime) {
        this.strCreateTime = strCreateTime;
    }

    public List<TaskMouldNode> getTaskTemplateList() {
        return taskTemplateList;
    }

    public void setTaskTemplateList(List<TaskMouldNode> taskTemplateList) {
        this.taskTemplateList = taskTemplateList;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getExecuteTeacherId() {
        return executeTeacherId;
    }

    public void setExecuteTeacherId(String executeTeacherId) {
        this.executeTeacherId = executeTeacherId;
    }
}
