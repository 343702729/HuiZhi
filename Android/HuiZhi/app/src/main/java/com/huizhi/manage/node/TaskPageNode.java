package com.huizhi.manage.node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/19.
 */

public class TaskPageNode {
    private int myAssigned;
    private int myToBeApprove;
    private int totCount = 0;
    private int totPageCount = 0;
    private List<TaskNode> tasks = new ArrayList<>();

    public int getMyAssigned() {
        return myAssigned;
    }

    public void setMyAssigned(int myAssigned) {
        this.myAssigned = myAssigned;
    }

    public int getMyToBeApprove() {
        return myToBeApprove;
    }

    public void setMyToBeApprove(int myToBeApprove) {
        this.myToBeApprove = myToBeApprove;
    }

    public int getTotCount() {
        return totCount;
    }

    public void setTotCount(int totCount) {
        this.totCount = totCount;
    }

    public int getTotPageCount() {
        return totPageCount;
    }

    public void setTotPageCount(int totPageCount) {
        this.totPageCount = totPageCount;
    }

    public List<TaskNode> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskNode> tasks) {
        this.tasks = tasks;
    }
}
