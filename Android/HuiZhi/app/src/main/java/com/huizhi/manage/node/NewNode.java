package com.huizhi.manage.node;

import java.io.Serializable;

/**
 * Created by CL on 2018/1/2.
 */

public class NewNode implements Serializable{
    private String newsId;
    private String newsTitle;
    private String referenceNewsId;
    private String thumbImgUrl;
    private String strCreateTime1;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getReferenceNewsId() {
        return referenceNewsId;
    }

    public void setReferenceNewsId(String referenceNewsId) {
        this.referenceNewsId = referenceNewsId;
    }

    public String getThumbImgUrl() {
        return thumbImgUrl;
    }

    public void setThumbImgUrl(String thumbImgUrl) {
        this.thumbImgUrl = thumbImgUrl;
    }

    public String getStrCreateTime1() {
        return strCreateTime1;
    }

    public void setStrCreateTime1(String strCreateTime1) {
        this.strCreateTime1 = strCreateTime1;
    }
}
