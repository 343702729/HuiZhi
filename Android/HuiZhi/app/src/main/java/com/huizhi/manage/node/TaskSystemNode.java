package com.huizhi.manage.node;

import java.io.Serializable;

/**
 * Created by CL on 2018/1/8.
 */

public class TaskSystemNode extends TaskCategoryNode implements Serializable{
    private String taskId;
    private String taskTitle;
    private String taskDescription;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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
}
