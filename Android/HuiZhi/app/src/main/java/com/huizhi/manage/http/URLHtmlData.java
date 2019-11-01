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

    /**
     * 运营任务
     * @param teacherid
     * @return
     */
    public static String getOperateListUrl(String teacherid){
        return URL_HOST + "/operatetask/index/" + teacherid;
    }

    /**
     * 任务详情
     * @param teacherid
     * @param taskid
     * @return
     */
    public static String getOperateTaskUrl(String teacherid, String taskid){
        return URL_HOST + "/operatetask/details/" + teacherid + "/" + taskid;
    }

    /**
     * 运营动态
     * @param teacherid
     * @return
     */
    public static String getOperateNewsUrl(String teacherid){
        return URL_HOST + "/operatenews/index/" + teacherid;
    }

    /**
     * 运营动态详情
     * @param teacherid
     * @param newsid
     * @return
     */
    public static String getOperateNewDetail(String teacherid, String newsid){
        return URL_HOST + "/operatenews/details/" + teacherid + "/" + newsid;
    }

    /**
     * 运营方案
     * @param teacherid
     * @return
     */
    public static String getOperatePlanUrl(String teacherid){
        return URL_HOST + "/operatescheme/index/" + teacherid;
    }

    /**
     * 运营方案详情
     * @param teacherid
     * @return
     */
    public static String getOperatePlanDetailUrl(String teacherid, String knowledgeid){
        return URL_HOST + "/operatescheme/details/" + teacherid + "/" + knowledgeid;
    }

    /**
     * 问答
     * @param teacherid
     * @return
     */
    public static String getOperateAskUrl(String teacherid){
        return URL_HOST + "/ask/index/" + teacherid;
    }

    /**
     * 问答详情
     * @param teacherid
     * @param askid
     * @return
     */
    public static String getOperateAskDetailUrl(String teacherid, String askid){
        return URL_HOST + "/ask/queryitem/" + teacherid + "/" + askid;
    }

    /**
     *  学习列表页
     * @param teacherid
     * @return
     */
    public static String getStudyListUrl(String teacherid){
        return URL_HOST + "/study/index/" + teacherid;
    }

    /**
     *  学习展示页
     * @param teacherid
     * @param detailsid
     * @return
     */
    public static String getStudyDetailUrl(String teacherid, String detailsid){
        return URL_HOST + "/study/details/" + teacherid + "/" + detailsid;
    }

    /**
     * 课程列表
     * @param teacherid
     * @return
     */
    public static String getTrainingListUrl(String teacherid){
        return URL_HOST + "/training/index/" + teacherid;
    }

    /**
     * 课程详情
     * @param teacherid
     * @return
     */
    public static String getTrainingDetailUrl(String teacherid, String trainingid){
        return URL_HOST + "/training/details/" + teacherid + "/" + trainingid;
    }

    /**
     * 隐私协议的H5地址
     * @return
     */
    public static String getPrivacyPolicyUrl(){
        return URL_HOST + "/privacypolicy/";
    }

    /**
     * 备课详情
     * @return
     */
    public static String getPrepareDetailUrl(String courseNo){
        return URL_HOST + "/prepare/details/testnum/" + courseNo;
    }

    /**
     * 学习记录
     * @param teacherid
     * @return
     */
    public static String getTeacherLearning(String teacherid){
        return URL_HOST + "/teacher/learning/" + teacherid;
    }
}
