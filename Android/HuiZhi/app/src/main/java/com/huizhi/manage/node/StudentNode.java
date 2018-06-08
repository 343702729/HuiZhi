package com.huizhi.manage.node;

import java.io.Serializable;
import java.util.List;

public class StudentNode implements Serializable {
    private String lessonNum;
    private String stuNum;
    private String stuName;
    private int stuStatus;
    private String strStuStatus;
    private boolean isPublished;
    private String workID;
    private String title;
    private String worksPic;
    private String comment;
    private List<PictureNode> pictures;

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

    public String getLessonNum() {
        return lessonNum;
    }

    public void setLessonNum(String lessonNum) {
        this.lessonNum = lessonNum;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorksPic() {
        return worksPic;
    }

    public void setWorksPic(String worksPic) {
        this.worksPic = worksPic;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<PictureNode> getPictures() {
        return pictures;
    }

    public void setPictures(List<PictureNode> pictures) {
        this.pictures = pictures;
    }
}
