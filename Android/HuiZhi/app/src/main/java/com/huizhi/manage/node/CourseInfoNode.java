package com.huizhi.manage.node;

import java.io.Serializable;
import java.util.List;

public class CourseInfoNode implements Serializable {
    private int totLessonNum = 0;
    private int doneLessonNum = 0;
    private int commentedNum = 0;
    private int toBeCommentNum = 0;
    private List<CourseNode> lessons;

    public int getTotLessonNum() {
        return totLessonNum;
    }

    public void setTotLessonNum(int totLessonNum) {
        this.totLessonNum = totLessonNum;
    }

    public int getDoneLessonNum() {
        return doneLessonNum;
    }

    public void setDoneLessonNum(int doneLessonNum) {
        this.doneLessonNum = doneLessonNum;
    }

    public int getCommentedNum() {
        return commentedNum;
    }

    public void setCommentedNum(int commentedNum) {
        this.commentedNum = commentedNum;
    }

    public int getToBeCommentNum() {
        return toBeCommentNum;
    }

    public void setToBeCommentNum(int toBeCommentNum) {
        this.toBeCommentNum = toBeCommentNum;
    }

    public List<CourseNode> getLessons() {
        return lessons;
    }

    public void setLessons(List<CourseNode> lessons) {
        this.lessons = lessons;
    }
}
