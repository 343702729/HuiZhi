package com.huizhi.manage.node;

/**
 * Created by CL on 2018/1/24.
 */

public class AttendanceInfoNode {
    private String startTime;
    private String endTime;
    private String daysOFYear;
    private String daysOFMonth;
    private boolean isInRegion;
    private boolean isClockInLate;
    private boolean isClockOutEarly;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDaysOFYear() {
        return daysOFYear;
    }

    public void setDaysOFYear(String daysOFYear) {
        this.daysOFYear = daysOFYear;
    }

    public String getDaysOFMonth() {
        return daysOFMonth;
    }

    public void setDaysOFMonth(String daysOFMonth) {
        this.daysOFMonth = daysOFMonth;
    }

    public boolean isInRegion() {
        return isInRegion;
    }

    public void setInRegion(boolean inRegion) {
        isInRegion = inRegion;
    }

    public boolean isClockInLate() {
        return isClockInLate;
    }

    public void setClockInLate(boolean clockInLate) {
        isClockInLate = clockInLate;
    }

    public boolean isClockOutEarly() {
        return isClockOutEarly;
    }

    public void setClockOutEarly(boolean clockOutEarly) {
        isClockOutEarly = clockOutEarly;
    }
}
