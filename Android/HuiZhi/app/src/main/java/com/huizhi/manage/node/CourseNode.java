package com.huizhi.manage.node;

import java.util.List;

public class CourseNode {
    private String lessonNum;
    private String lessonName;
    private String lessonTime;
    private int allStuCount;
    private int signedCount;
    private int leaveStuCount;
    private int publishWorkCount;
    private int commentedCount;
    private String completionRate;
    private boolean isSignInTeacher;
    private boolean isSignInTutor;
    private List<StudentNode> studentNodes;

    public String getLessonNum() {
        return lessonNum;
    }

    public void setLessonNum(String lessonNum) {
        this.lessonNum = lessonNum;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(String lessonTime) {
        this.lessonTime = lessonTime;
    }

    public int getAllStuCount() {
        return allStuCount;
    }

    public void setAllStuCount(int allStuCount) {
        this.allStuCount = allStuCount;
    }

    public int getSignedCount() {
        return signedCount;
    }

    public void setSignedCount(int signedCount) {
        this.signedCount = signedCount;
    }

    public int getLeaveStuCount() {
        return leaveStuCount;
    }

    public void setLeaveStuCount(int leaveStuCount) {
        this.leaveStuCount = leaveStuCount;
    }

    public int getPublishWorkCount() {
        return publishWorkCount;
    }

    public void setPublishWorkCount(int publishWorkCount) {
        this.publishWorkCount = publishWorkCount;
    }

    public int getCommentedCount() {
        return commentedCount;
    }

    public void setCommentedCount(int commentedCount) {
        this.commentedCount = commentedCount;
    }

    public String getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(String completionRate) {
        this.completionRate = completionRate;
    }

    public boolean isSignInTeacher() {
        return isSignInTeacher;
    }

    public void setSignInTeacher(boolean signInTeacher) {
        isSignInTeacher = signInTeacher;
    }

    public boolean isSignInTutor() {
        return isSignInTutor;
    }

    public void setSignInTutor(boolean signInTutor) {
        isSignInTutor = signInTutor;
    }

    public List<StudentNode> getStudentNodes() {
        return studentNodes;
    }

    public void setStudentNodes(List<StudentNode> studentNodes) {
        this.studentNodes = studentNodes;
    }
}
