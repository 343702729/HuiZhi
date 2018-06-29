package com.huizhi.manage.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/6.
 */

public class UserNode implements Serializable {
    private String teacherId;
    private String teacherName;
    private String phoneNumber;
    private String headImgUrl;
    private String email;
    private String strCreateTime;
    private String schoolId;
    private int districtId;
    private String schoolName;
    private int roleType;           //11：店长  2：老师
    private String roleTypeName;
    private String alias;
    private boolean isAdmin = false;
    private String appKey;
    private String chatToken;
    private int jobStatus;
    private int type;
    private int isDone;
    private String strIsDone;
    private String fullHeadImgUrlThumb;
    private List<SchoolNode> schools = new ArrayList<>();

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStrCreateTime() {
        return strCreateTime;
    }

    public void setStrCreateTime(String strCreateTime) {
        this.strCreateTime = strCreateTime;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public String getRoleTypeName() {
        return roleTypeName;
    }

    public void setRoleTypeName(String roleTypeName) {
        this.roleTypeName = roleTypeName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public String getStrIsDone() {
        return strIsDone;
    }

    public void setStrIsDone(String strIsDone) {
        this.strIsDone = strIsDone;
    }

    public String getFullHeadImgUrlThumb() {
        return fullHeadImgUrlThumb;
    }

    public void setFullHeadImgUrlThumb(String fullHeadImgUrlThumb) {
        this.fullHeadImgUrlThumb = fullHeadImgUrlThumb;
    }

    public List<SchoolNode> getSchools() {
        return schools;
    }

    public void setSchools(List<SchoolNode> schools) {
        this.schools = schools;
    }
}
