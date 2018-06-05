package com.huizhi.manage.node;

public class CourseNode {
    private String lessonName;
    private String lessonTime;
    private int allStuCount;
    private int signedCount;
    private int leaveStuCount;
    private int publishWorkCount;
    private int commentedCount;
    private String completionRate;

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
}
