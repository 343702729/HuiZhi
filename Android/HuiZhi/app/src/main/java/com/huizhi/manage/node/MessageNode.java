package com.huizhi.manage.node;

import java.io.Serializable;

/**
 * Created by CL on 2018/1/25.
 */

public class MessageNode implements Serializable{
    private String noticeId;
    private String schoolId;
    private String title;
    private String content;
    private boolean isPush;
    private String sender;
    private String strCreateTime;
    private String StrCreateTime;
    private int isRead;
    private String schoolList;

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPush() {
        return isPush;
    }

    public void setPush(boolean push) {
        isPush = push;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getStrCreateTime() {
        return strCreateTime;
    }

    public void setStrCreateTime(String strCreateTime) {
        this.strCreateTime = strCreateTime;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getSchoolList() {
        return schoolList;
    }

    public void setSchoolList(String schoolList) {
        this.schoolList = schoolList;
    }
}
