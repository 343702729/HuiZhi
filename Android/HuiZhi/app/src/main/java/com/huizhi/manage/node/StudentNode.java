package com.huizhi.manage.node;

import java.io.Serializable;

public class StudentNode implements Serializable {
    private String stuNum;
    private String stuName;
    private int stuStatus;
    private String strStuStatus;
    private boolean isPublished;

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getStuStatus() {
        return stuStatus;
    }

    public void setStuStatus(int stuStatus) {
        this.stuStatus = stuStatus;
    }

    public String getStrStuStatus() {
        return strStuStatus;
    }

    public void setStrStuStatus(String strStuStatus) {
        this.strStuStatus = strStuStatus;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }
}
