package com.huizhi.manage.http;

public class URLHtmlData {
    private static String URL_HOST = URLData.URL_HOST;

    /**
     * 课件库地址
     * @param teacherid 教师编号
     * @param categoryid 当前课件分类(第二层)的编号
     * @param unit 单元, 1-3。如果没有第三层的时候传1
     * @return
     */
    public static String getCourseUrl(String teacherid, String categoryid, String unit){
        return URL_HOST + "/courseware/course/" + teacherid + "/" + categoryid + "/" + unit;
    }

}
