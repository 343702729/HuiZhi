package com.huizhi.manage.node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/25.
 */

public class NewPageNode {
    private int totCount;
    private int totPageCount;
    private List<NewNode> news = new ArrayList<>();

    public int getTotCount() {
        return totCount;
    }

    public void setTotCount(int totCount) {
        this.totCount = totCount;
    }

    public int getTotPageCount() {
        return totPageCount;
    }

    public void setTotPageCount(int totPageCount) {
        this.totPageCount = totPageCount;
    }

    public List<NewNode> getNews() {
        return news;
    }

    public void setNews(List<NewNode> news) {
        this.news = news;
    }
}
