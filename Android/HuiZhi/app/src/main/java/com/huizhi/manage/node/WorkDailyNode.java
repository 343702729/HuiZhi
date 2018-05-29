package com.huizhi.manage.node;

import java.io.Serializable;

/**
 * Created by CL on 2018/1/11.
 */

public class WorkDailyNode implements Serializable{
    private String workDate;
    private boolean isHoliday;
    private String holidayName;
    private boolean isReported;
    private String timeSheetId;
    private String teacherId;
    private String workContent;
    private String strWorkDate;
    private String strCreateTime;

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean holiday) {
        isHoliday = holiday;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public String getTimeSheetId() {
        return timeSheetId;
    }

    public void setTimeSheetId(String timeSheetId) {
        this.timeSheetId = timeSheetId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getStrWorkDate() {
        return strWorkDate;
    }

    public void setStrWorkDate(String strWorkDate) {
        this.strWorkDate = strWorkDate;
    }

    public String getStrCreateTime() {
        return strCreateTime;
    }

    public void setStrCreateTime(String strCreateTime) {
        this.strCreateTime = strCreateTime;
    }
}
