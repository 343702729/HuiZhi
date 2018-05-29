package com.huizhi.manage.node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/8.
 */

public class TaskCategoryNode {
    private String categoryId;
    private String categoryName;
    private int sequenceNumber;
    private String strCreateTime;
    private List<TaskSystemNode> taskSystemNodes = new ArrayList<>();

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

    public void addTaskSystemNode(TaskSystemNode node){
        taskSystemNodes.add(node);
    }

    public List<TaskSystemNode> getTaskSystemNodes() {
        return taskSystemNodes;
    }
}
