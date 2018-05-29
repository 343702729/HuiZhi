package com.huizhi.manage.node;

import java.io.Serializable;

/**
 * Created by CL on 2018/1/12.
 */

public class FolderNode implements Serializable{
    private String folderId;
    private String parentFolderId;
    private String folderName;
    private int sequcenceNumber;
    private int roleType;
    private String strRoleType;
    private String strCreateTime;
    private int subFoldNum = 0;

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getSequcenceNumber() {
        return sequcenceNumber;
    }

    public void setSequcenceNumber(int sequcenceNumber) {
        this.sequcenceNumber = sequcenceNumber;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public String getStrCreateTime() {
        return strCreateTime;
    }

    public void setStrCreateTime(String strCreateTime) {
        this.strCreateTime = strCreateTime;
    }

    public String getStrRoleType() {
        return strRoleType;
    }

    public void setStrRoleType(String strRoleType) {
        this.strRoleType = strRoleType;
    }

    public int getSubFoldNum() {
        return subFoldNum;
    }

    public void setSubFoldNum(int subFoldNum) {
        this.subFoldNum = subFoldNum;
    }
}
