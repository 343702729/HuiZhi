package com.huizhi.manage.node;

import java.io.Serializable;
import java.util.List;

public class CourseInfoNode implements Serializable {
    private int totLessonNum = 0;
    private int doneLessonNum = 0;
    private int commentedNum = 0;
    private int toBeCommentNum = 0;
    private String lessonFinishPercent;
    private String commentFinishPercent;
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

    public String getLessonFinishPercent() {
        return lessonFinishPercent;
    }

    public void setLessonFinishPercent(String lessonFinishPercent) {
        this.lessonFinishPercent = lessonFinishPercent;
    }

    public String getCommentFinishPercent() {
        return commentFinishPercent;
    }

    public void setCommentFinishPercent(String commentFinishPercent) {
        this.commentFinishPercent = commentFinishPercent;
    }

    public List<CourseNode> getLessons() {
        return lessons;
    }

    public void setLessons(List<CourseNode> lessons) {
        this.lessons = lessons;
    }
}
