package com.huizhi.manage.data;

import android.text.TextUtils;

import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.node.SchoolNode;
import com.huizhi.manage.node.UserNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/6.
 */

public class UserInfo {
    private static UserInfo mUserInfo;
    private UserNode userNode;
    private List<UserNode> teamUsers = new ArrayList<>();
    private List<UserNode> taskUsers = new ArrayList<>();
    private List<UserNode> talkUsers = new ArrayList<>();
    private String registrationId;
    private final int phoneType = 2;
    private boolean isLogin = false;
    private BaseInfoUpdate taskCreatInfo, communicateInfo;
    private BaseInfoUpdate appDownloadUpdate;
    private SchoolNode switchSchool;

    private UserInfo(){
        userNode = new UserNode();
    }

    public static UserInfo getInstance(){
        if(mUserInfo==null)
            mUserInfo = new UserInfo();
        return mUserInfo;
    }

    public void setUser(UserNode node){
        userNode = node;
    }

    public UserNode getUser(){
        return userNode;
    }

    public List<UserNode> getTeamUsers() {
        return teamUsers;
    }

    public void setTeamUsers(List<UserNode> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public List<UserNode> getTaskUsers() {
        return taskUsers;
    }

    public void setTaskUsers(List<UserNode> taskUsers) {
        this.taskUsers = taskUsers;
    }

    public List<UserNode> getTalkUsers() {
        return talkUsers;
    }

    public void setTalkUsers(List<UserNode> talkUsers) {
        this.talkUsers = talkUsers;
    }

    public UserNode getUserByTeacherId(String teacherid){
        if(TextUtils.isEmpty(teacherid))
            return null;
        for(UserNode node:teamUsers){
            if(teacherid.equals(node.getTeacherId()))
                return node;
        }
        return null;
    }

    public UserNode getTaskUserByTeacherId(String teacherid){
        if(TextUtils.isEmpty(teacherid))
            return null;
        for(UserNode node:taskUsers){
            if(teacherid.equals(node.getTeacherId()))
                return node;
        }
        return null;
    }

    public UserNode getTalkUserByTeacherId(String teacherid){
        if(TextUtils.isEmpty(teacherid))
            return null;
        for(UserNode node:talkUsers){
            if(teacherid.equals(node.getTeacherId()))
                return node;
        }
        return null;
    }

    public UserNode getAdminUser(){
        for(UserNode node:teamUsers){
            if(node.isAdmin())
                return node;
        }
        return null;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public BaseInfoUpdate getTaskCreatInfo() {
        return taskCreatInfo;
    }

    public void setTaskCreatInfo(BaseInfoUpdate taskCreatInfo) {
        this.taskCreatInfo = taskCreatInfo;
    }

    public BaseInfoUpdate getCommunicateInfo() {
        return communicateInfo;
    }

    public void setCommunicateInfo(BaseInfoUpdate communicateInfo) {
        this.communicateInfo = communicateInfo;
    }

    public BaseInfoUpdate getAppDownloadUpdate() {
        return appDownloadUpdate;
    }

    public void setAppDownloadUpdate(BaseInfoUpdate appDownloadUpdate) {
        this.appDownloadUpdate = appDownloadUpdate;
    }

    public SchoolNode getSwitchSchool() {
        return switchSchool;
    }

    public void setSwitchSchool(SchoolNode switchSchool) {
        this.switchSchool = switchSchool;
    }
}
