package com.huizhi.manage.http;

import android.text.TextUtils;

import com.huizhi.manage.data.UserInfo;

/**
 * Created by CL on 2017/12/13.
 */

public class URLData {
//    private static final String URL_HOST = "http://app.huizhiart.com";    //http://app.huizhiart.com    http://hzapp.dewinfo.com
    public static final String URL_HOST = "http://hzapp.dewinfo.com";    //http://app.huizhiart.com    http://hzapp.dewinfo.com

    public static final String URL_ABOUT = URL_HOST + "/app/aboutus/";

    private static final String URL_HOME_WEBBA = "/ask/index-";

    private static final String URL_HOME_YUNYIN = "/knowledgelist";

    private static final String URL_LOGIN = "/service/UserService.ashx";
    public static final String METHOD_LOGIN = "userLogin";

    private static final String URL_HOME_EMAIL_INFO = "/service/UserService.ashx";
    public static final String METHORD_HOME_EMAIL_INFO = "getEmailInfo";

    private static final String URL_HOME_ATTENDANCE_INFO = "/service/AttendanceService.ashx";
    public static final String METHORD_HOME_ATTENDANCE_INFO = "getAttendanceInfo";

    private static final String URL_HOME_MESSAGE_LIST = "/service/NoticeService.ashx";
    public static final String METHORD_HOME_MESSAGE_LIST = "getNoticeList";

    private static final String URL_HOME_MESSAGE_INFO = "/service/NoticeService.ashx";
    public static final String METHORD_HOME_MESSAGE_INFO = "getNoticeInfo";

    private static final String URL_HOME_ATTENDANCE_IS_IN_REGION = "/service/AttendanceService.ashx";
    public static final String METHORD_HOME_ATTENDANCE_IS_IN_REGION = "checkIsInTheRegion";

    private static final String URL_HOME_ATTENDANCE_SUBMIT = "/service/AttendanceService.ashx";
    public static final String METHORD_HOME_ATTENDANCE_SUBMIT = "saveAttendanceInfo";

    private static final String URL_TEAM_USERS = "/service/UserService.ashx";
    public static final String METHORD_TEAM_USERS = "getTeamList";

    private static final String URL_TALK_USERS = "/service/UserService.ashx";
    public static final String METHORD_TALK_USERS = "getTalkUserList";

    private static final String URL_TALK_ITEM_USER = "/service/UserService.ashx";
    public static final String METHORD_TALK_ITEM_USER = "getTalkUserInfo";

    private static final String URL_VERSION = "/service/ClientUpgradeService.ashx";
    public static final String METHORD_VERSION = "GetNewVersion";

    private static final String URL_NEWS_BANNER = "/service/HZNewsService.ashx";
    public static final String METHORD_NEWS_BANNER = "getNewsBannerList";

    private static final String URL_SHARE_COUNT = "/Service/KnowledgeService.ashx";
    public static final String METHORD_SHARE_COUNT = "getNoReadCount";

    private static final String URL_NOTICE_COUNT = "/Service/NoticeService.ashx";
    public static final String METHORD_NOTICE_COUNT = "getNoReadCount";

    private static final String URL_FILE_COUNT = "/Service/FilesService.ashx";
    public static final String METHORD_FILE_COUNT = "getNoReadCount";

    private static final String URL_NEWS_LIST = "/service/HZNewsService.ashx";
    public static final String METHORD_NEWS_LIST = "getNewsList";

    private static final String URL_TASK_INFO = "/service/UserService.ashx";
    public static final String METHORD_TASK_INFO = "getTaskSummaryInfo";

    private static final String URL_TASK_LIST = "/service/TaskService.ashx";
    public static final String METHORD_TASK_LIST = "getTaskList";

    private static final String URL_TASK_DETAIL = "/service/TaskService.ashx";
    public static final String METHORD_TASK_DETAIL = "getTaskInfo";

    private static final String URL_TASK_FINISH = "/service/TaskService.ashx";
    public static final String METHORD_TASK_FINISH = "finishTask";

    private static final String URL_TASK_UNFINISH = "/service/TaskService.ashx";
    public static final String METHORD_TASK_UNFINISH = "refuseTask";

    private static final String URL_HOME_TASK_MOULD_LIST = "/service/PersonalTaskTemplateService.ashx";
    public static final String METHORD_HOME_TASK_MOULD_LIST = "getCategoryList";

    private static final String URL_HOME_TASK_MOULD_CREATE = "/service/PersonalTaskTemplateService.ashx";
    public static final String METHORD_HOME_TASK_MOULD_CREATE = "addTemplates";

    private static final String URL_HOME_TASK_MOULD_EDIT = "/service/PersonalTaskTemplateService.ashx";
    public static final String METHORD_HOME_TASK_MOULD_EDIT = "updateTemplates";

    private static final String URL_HOME_TASK_MOULD_ITEM_DELETE = "/service/PersonalTaskTemplateService.ashx";
    public static final String METHORD_HOME_TASK_MOULD_ITEM_DELETE = "deleteTemplate";

    private static final String URL_HOME_TASK_MOULD_DELETE = "/service/PersonalTaskTemplateService.ashx";
    public static final String METHORD_HOME_TASK_MOULD_DELETE = "deleteCategory";

    private static final String URL_HOME_MOULD_TASK_CREATE = "/service/TaskService.ashx";
    public static final String METHORD_HOME_MOULD_TASK_CREATE = "createTemplateTask";

    private static final String URL_HOME_TASK_MOULD_MOVE = "/service/PersonalTaskTemplateService.ashx";
    public static final String METHORD_HOME_TASK_MOULD_MOVE = "sortCategorys";

    private static final String URL_HOME_TASK_MOULD_DETAIL = "/service/PersonalTaskTemplateService.ashx";
    public static final String METHORD_HOME_TASK_MOULD_DETAIL = "getTaskTemplateFullInfo";

    private static final String URL_HOME_TASK_SYSTEM_CATEGORY = "/service/SystemTaskService.ashx";
    public static final String METHORD_HOME_TASK_SYSTEM_CATEGORY = "getSystemTaskCategory";

    private static final String URL_HOME_TASK_LIST = "/service/TaskService.ashx";
    public static final String METHORD_HOME_TASK_LIST = "searchTaskList";

    private static final String URL_HOME_TASK_VERIFY_PASS = "/service/TaskService.ashx";
    public static final String METHORD_HOME_TASK_VERIFY_PASS = "taskReviewPass";

    private static final String URL_HOME_TASK_VERIFY_FAILED = "/service/TaskService.ashx";
    public static final String METHORD_HOME_TASK_VERIFY_FAILED = "taskReviewFailed";

    private static final String URL_HOME_TASKK_REASSIGN = "/service/TaskService.ashx";
    public static final String METHORD_HOME_TASK_REASSIGN = "reassignTask";

    private static final String URL_HOME_TASK_DELETE = "/service/TaskService.ashx";
    public static final String METHORD_HOME_TASK_DELETE = "deleteTask";

    private static final String URL_HOME_TASK_EDIT = "/service/TaskService.ashx";
    public static final String METHORD_HOME_TASK_EDIT = "editTasks";

    private static final String URL_HOME_TASKS_SYSTEM = "/service/SystemTaskService.ashx";
    public static final String METHORD_HOME_TASKS_SYSTEM = "getSystemTaskList";

    private static final String URL_HOME_TASKS_SYSTEM_CREATE = "/service/TaskService.ashx";
    public static final String METHORD_HOME_TASK_SYSTEM_CREATE = "createSystemTask";

    private static final String URL_HOME_TASKS_CUSTOM_CREATE = "/service/TaskService.ashx";
    public static final String METHORD_HOME_TASK_CUSTOM_CREATE = "createCustomTasks";

    private static final String URL_USER_WORK_DAILY_MONTH = "/service/TimeSheetService.ashx";
    public static final String METHORD_USER_WORK_DAILY_MONTH = "getTimeSheetList";

    private static final String URL_USER_WORK_DAILY_SAVE = "/service/TimeSheetService.ashx";
    public static final String METHORD_USER_WORK_DAILY_SAVE = "saveTimeSheetInfo";

    private static final String URL_USER_WORK_DAILY_DATE = "/service/TimeSheetService.ashx";
    public static final String METHORD_USER_WORK_DAILY_DATE = "getTimeSheetInfo";

    private static final String URL_FILE_UPLOAD_TOKEN = "/Service/FilesService.ashx";
    public static final String METHORD_FILE_UPLOAD_TOKEN = "getUploadToken";

    private static final String URL_FILE_SEARCH = "/service/FilesService.ashx";
    public static final String METHORD_FILE_SEARCH = "searchFileList";

    private static final String URL_FILE_MENU = "/service/FilesService.ashx";
    public static final String METHORD_FILE_MENU = "getFileFolderList";

    private static final String URL_FILE_FOLDER_LIST = "/service/FilesService.ashx";
    public static final String METHORD_FILE_FOLDER_LIST = "getFolderSubItems";

    private static final String URL_FILE_LIST = "/service/FilesService.ashx";
    public static final String METHORD_FILE_LIST = "getFileList";

    private static final String URL_FILE_INFO = "/service/FilesService.ashx";
    public static final String METHORD_FILE_INFO = "getFileInfo";

    private static final String URL_FILE_FAVORITE = "/service/FilesService.ashx";
    public static final String METHORD_FILE_FAVORITE = "getFavoriteFileList";

    private static final String URL_FILE_FAVORITE_ADD = "/service/FilesService.ashx";
    public static final String METHORD_FILE_FAVORITE_ADD = "addFavorite";

    private static final String URL_FILE_FAVORITE_CANCEL = "/service/FilesService.ashx";
    public static final String METHORD_FILE_FAVORITE_CANCEL = "removeFavorite";

    private static final String URL_HEAD_PORTRAIT = "/service/UserService.ashx";
    public static final String METHRD_HEAD_PORTRAIT = "setUserHeadImg";

    private static final String URL_USER_PASSWORD_EDIT = "/service/UserService.ashx";
    public static final String METHORD_USER_PASSWORD_EDIT = "changePassword";

    private static final String URL_HOME_COURSE_LIST = "/service/TeacherService.ashx";
    public static final String METHORD_HOME_COURSE_LIST = "getLessonList";

    private static final String URL_HOME_COURSE_INFO = "/service/TeacherService.ashx";
    public static final String METHORD_HOME_COURSE_INFO = "getLessonInfo";

    private static final String URL_HOME_COURSE_STU_SIGN = "/service/TeacherService.ashx";
    public static final String METHORD_HOME_COURSE_STU_SIGN = "studentSign";

    private static final String URL_HOME_COURSE_STU_INFO = "/service/TeacherService.ashx";
    public static final String METHORD_HOME_COURSE_STU_INFO = "getStuWorksInfo";

    private static final String URL_HOME_COURSE_SIGN_TEACHER = "/service/TeacherService.ashx";
    public static final String METHORD_HOME_COURSE_SIGN_TEACHER = "TeacherSignIn";

    private static final String URL_HOME_COURSE_SIGN_TUTOR = "/service/TeacherService.ashx";
    public static final String METHORD_HOME_COURSE_SIGN_TUTOR = "TutorSignIn";

    private static final String URL_HOME_COURSE_STU_PUBLISH = "/service/TeacherService.ashx";
    public static final String METHORD_HOME_COURSE_STU_PUBLISH = "publishStuWorks";

    private static final String URL_HOME_COURSE_PROJECT = "/ServiceII/ProjectLibService.ashx";
    public static final String METHORD_HOME_COURSE_WARE_TYPE = "GetCoursewareType";

    public static final String METHORD_HOME_COURSE_WARE_CATEGORY = "GetProjectLibCategory";

    private static final String URL_BUSINESS_TASK_SERVICE = "/ServiceII/BusinessTaskService.ashx";
    public static final String METHORD_HOME_OPERATE = "GetHomeInfo";

    private static final String URL_TEACHER_TRAINING_SERVICE = "/ServiceII/TeachingTrainingService.ashx";
    public static final String METHORD_TEACHER_TRAINING = "GetHomeInfo";

    public static final String METHORD_TEACHER_PROGRESS = "GetProgress";

    private static final String URL_MESSAGE_SERVICE = "/ServiceII/MessageService.ashx";
    public static final String METHORD_MESSAGE_LIST = "GetMessageList";
    public static final String METHORD_MESSAGE_READ = "ReadMessage";

    private static final String URL_HOME_SERVICE = "/ServiceII/HomeService.ashx";
    public static final String METHORD_HOME_INFO = "GetHomeInfo";

    private static final String URL_LOGIN_SERVICE = "/ServiceII/LoginService.ashx";
    public static final String METHORD_SUBMIT_LOGIN_INFO = "SubmitLoginInfo";


    /**
     * 用户登录
     * @return
     */
    public static String getUrlLogin(){
        return URL_HOST + URL_LOGIN;
    }

    /**
     * 图片链接
     * @param imgurl
     * @return
     */
    public static String getUrlFile(String imgurl){
        if(TextUtils.isEmpty(imgurl))
            return null;
        if(imgurl.startsWith("http"))
            return imgurl;
        if(imgurl.startsWith("/"))
            return URL_HOST + imgurl;
        return URL_HOST + "/" + imgurl;
    }

    /**
     * 问吧
     * @param teacherid
     * @return
     */
    public static String getUrlWenBa(String teacherid){
        return URL_HOST + URL_HOME_WEBBA + teacherid;
    }

    public static String getUrlHomeYunyin(){
        return URL_HOST + URL_HOME_YUNYIN;
    }

    /**
     * 新闻详情
     * @param newsId
     * @return
     */
    public static String getUrlNews(String newsId, String teacherid){
        return URL_HOST + "/news/" + teacherid + "/" + newsId;
    }

    /**
     * 通知详情
     * @param noticeId
     * @return
     */
    public static String getUrlNotice(String noticeId){
        return URL_HOST + "/notice/" + noticeId + "/" + UserInfo.getInstance().getUser().getTeacherId();
    }

    /**
     * 获取未读邮件数和登录地址
     * @return
     */
    public static String getUrlHomeEmailInfo(){
        return URL_HOST + URL_HOME_EMAIL_INFO;
    }

    /**
     * 取当日的考勤信息
     * @return
     */
    public static String getUrlHomeAttendanceInfo(){
        return URL_HOST + URL_HOME_ATTENDANCE_INFO;
    }

    /**
     * 获取信息列表
     * @return
     */
    public static String getUrlHomeMessageList(){
        return URL_HOST + URL_HOME_MESSAGE_LIST;
    }

    /**
     * 获取信息详情
     * @return
     */
    public static String getUrlHomeMessageInfo(){
        return URL_HOST + URL_HOME_MESSAGE_INFO;
    }

    /**
     * 判断是否在考勤区域
     * @return
     */
    public static String getUrlHomeAttendanceIsInRegion(){
        return URL_HOST + URL_HOME_ATTENDANCE_IS_IN_REGION;
    }

    /**
     * 提交考勤信息
     * @return
     */
    public static String getUrlHomeAttendanceSubmit(){
        return URL_HOST + URL_HOME_ATTENDANCE_SUBMIT;
    }

    /**
     * 其他成员信息
     * @return
     */
    public static String getUrlTeamUsers(){
        return URL_HOST + URL_TEAM_USERS;
    }

    /**
     * 取本校区人员和客服人员
     * @return
     */
    public static String getUrlTalkUsers(){
        return URL_HOST + URL_TALK_USERS;
    }

    /**
     * 取聊天的人员（单人）
     * @return
     */
    public static String getUrlTalkItemUser(){
        return URL_HOST +URL_TALK_ITEM_USER;
    }

    /**
     * 获取新版本
     * @return
     */
    public static String getUrlVersion(){
        return URL_HOST +URL_VERSION;
    }

    /**
     * 主界面的新闻轮播图列表
     * @return
     */
    public static String getUrlNewsBanner(){
        return URL_HOST + URL_NEWS_BANNER;
    }

    /**
     * 未运营分享未读数量
     * @return
     */
    public static String getUrlShareCount(){
        return URL_HOST + URL_SHARE_COUNT;
    }

    /**
     * 通知未读消息
     * @return
     */
    public static String getUrlNoticeCount(){
        return URL_HOST + URL_NOTICE_COUNT;
    }

    /**
     * 资料未读数量
     * @return
     */
    public static String getUrlFileCount(){
        return URL_HOST + URL_FILE_COUNT;
    }

    /**
     * 获取新闻列表
     * @return
     */
    public static String getUrlNewsList(){
        return URL_HOST + URL_NEWS_LIST;
    }

    /**
     * 获取个人的任务统计信息
     * @return
     */
    public static String getUrlTaskInfo(){
        return URL_HOST + URL_TASK_INFO;
    }

    /**
     * 获取任务列表
     * @return
     */
    public static String getUrlTaskList(){
        return URL_HOST + URL_TASK_LIST;
    }

    /**
     * 获取任务详情
     * @return
     */
    public static String getUrlTaskDetail(){
        return URL_HOST + URL_TASK_DETAIL;
    }

    /**
     * 完成任务，并提交审核
     * @return
     */
    public static String getUrlTaskFinish(){
        return URL_HOST + URL_TASK_FINISH;
    }

    /**
     * 拒绝执行任务
     * @return
     */
    public static String getUrlTaskUnfinish(){
        return URL_HOST + URL_TASK_UNFINISH;
    }

    /**
     * 获取类型列表
     * @return
     */
    public static String getUrlHomeTaskMouldList(){
        return URL_HOST + URL_HOME_TASK_MOULD_LIST;
    }

    /**
     * 创建任务模板
     * @return
     */
    public static String getUrlHomeTaskMouldCreate(){
        return URL_HOST +URL_HOME_TASK_MOULD_CREATE;
    }

    /**
     * 编辑任务模板
     * @return
     */
    public static String getUrlHomeTaskMouldEdit(){
        return URL_HOST +URL_HOME_TASK_MOULD_EDIT;
    }

    /**
     * 删除模版
     * @return
     */
    public static String getUrlHomeTaskMouldItemDelete(){
        return URL_HOST + URL_HOME_TASK_MOULD_ITEM_DELETE;
    }

    /**
     * 删除类型
     * @return
     */
    public static String getUrlHomeTaskMouldDelete(){
        return URL_HOST + URL_HOME_TASK_MOULD_DELETE;
    }

    /**
     * 根据模版创建任务
     * @return
     */
    public static String getUrlHomeMouldTaskCreate(){
        return URL_HOST + URL_HOME_MOULD_TASK_CREATE;
    }

    /**
     * 模版排序
     * @return
     */
    public static String getUrlHomeTaskMouldMove(){
        return URL_HOST + URL_HOME_TASK_MOULD_MOVE;
    }

    /**
     * 加载任务模板详情的接口
     * @return
     */
    public static String getUrlHomeTaskMouldDetail(){
        return URL_HOST + URL_HOME_TASK_MOULD_DETAIL;
    }

    /**
     * 获取系统任务分类
     * @return
     */
    public static String getUrlHomeTaskSystemCategory(){
        return URL_HOST + URL_HOME_TASK_SYSTEM_CATEGORY;
    }

    /**
     * 查询任务列表
     * @return
     */
    public static String getUrlHomeTaskList(){
        return URL_HOST + URL_HOME_TASK_LIST;
    }

    /**
     * 任务审核通过
     * @return
     */
    public static String getUrlHomeTaskVerifyPass(){
        return URL_HOST + URL_HOME_TASK_VERIFY_PASS;
    }

    /**
     * 任务审核未通过
     * @return
     */
    public static String getUrlHomeTaskVerifyFailed(){
        return URL_HOST + URL_HOME_TASK_VERIFY_FAILED;
    }

    /**
     * 任务重新指派
     * @return
     */
    public static String getUrlHomeTaskkReassign(){
        return URL_HOST + URL_HOME_TASKK_REASSIGN;
    }

    /**
     * 删除任务
     * @return
     */
    public static String getUrlHomeTaskDelete(){
        return URL_HOST + URL_HOME_TASK_DELETE;
    }

    /**
     * 修改任务
     * @return
     */
    public static String getUrlHomeTaskEdit(){
        return URL_HOST + URL_HOME_TASK_EDIT;
    }

    /**
     * 获取系统任务信息
     * @return
     */
    public static String getUrlHomeTasksSystem(){
        return URL_HOST + URL_HOME_TASKS_SYSTEM;
    }

    /**
     * 根据系统任务创建任务
     * @return
     */
    public static String getUrlHomeTasksSystemCreate(){
        return URL_HOST + URL_HOME_TASKS_SYSTEM_CREATE;
    }

    /**
     * 创建自定义任务
     * @return
     */
    public static String getUrlHomeTasksCustomCreate(){
        return URL_HOST + URL_HOME_TASKS_CUSTOM_CREATE;
    }

    /**
     * 取单月的日报列表
     * @return
     */
    public static String getUrlUserWorkDailyMonth(){
        return URL_HOST + URL_USER_WORK_DAILY_MONTH;
    }

    /**
     * 提交工作日报
     * @return
     */
    public static String getUrlUserWorkDailySave(){
        return URL_HOST + URL_USER_WORK_DAILY_SAVE;
    }

    /**
     * 取某一天的日报内容
     * @return
     */
    public static String getUrlUserWorkDailyDate(){
        return URL_HOST + URL_USER_WORK_DAILY_DATE;
    }

    /**
     * 文件上传token(七牛)
     * @return
     */
    public static String getUrlFileUploadToken(){
        return URL_HOST + URL_FILE_UPLOAD_TOKEN;
    }

    /**
     * 取文件目录
     * @return
     */
    public static String getUrlFileMenu(){
        return URL_HOST + URL_FILE_MENU;
    }

    /**
     *根据文件名查询文件
     * @return
     */
    public static String getUrlFileSearch(){
        return URL_HOST + URL_FILE_SEARCH;
    }

    /**
     * 取收藏的文件列表
     * @return
     */
    public static String getUrlFileFolderList(){
        return URL_HOST + URL_FILE_FOLDER_LIST;
    }

    /**
     * 取文件列表
     * @return
     */
    public static String getUrlFileList(){
        return URL_HOST + URL_FILE_LIST;
    }

    /**
     * 取文件信息
     * @return
     */
    public static String getUrlFileInfo(){
        return URL_HOST + URL_FILE_INFO;
    }

    /**
     * 取收藏的文件列表
     * @return
     */
    public static String getUrlFileFavorite(){
        return URL_HOST + URL_FILE_FAVORITE;
    }

    /**
     * 添加收藏
     * @return
     */
    public static String getUrlFileFavoriteAdd(){
        return URL_HOST + URL_FILE_FAVORITE_ADD;
    }

    /**
     * 取消收藏
     * @return
     */
    public static String getUrlFileFavoriteCancel(){
        return URL_HOST + URL_FILE_FAVORITE_CANCEL;
    }

    /**
     * 设置用户头像
     * @return
     */
    public static String getUrlHeadPortrait(){
        return URL_HOST + URL_HEAD_PORTRAIT;
    }

    /**
     * 修改密码
     * @return
     */
    public static String getUrlUserPasswordEdit(){
        return URL_HOST + URL_USER_PASSWORD_EDIT;
    }

    /**
     * 获取课程列表
     * @return
     */
    public static String getUrlHomeCourseList(){
        return  URL_HOST + URL_HOME_COURSE_LIST;
    }

    /**
     * 取课程详情
     * @return
     */
    public static String getUrlHomeCourseInfo(){
        return URL_HOST + URL_HOME_COURSE_INFO;
    }

    /**
     * 签到
     * @return
     */
    public static String getUrlHomeCourseStuSign(){
        return  URL_HOST + URL_HOME_COURSE_STU_SIGN;
    }

    /**
     * 取学生作品信息
     * @return
     */
    public static String getUrlHomeCourseStuInfo(){
        return URL_HOST + URL_HOME_COURSE_STU_INFO;
    }

    /**
     * 教师签到
     * @return
     */
    public static String getUrlHomeCourseSignTeacher(){
        return URL_HOST + URL_HOME_COURSE_SIGN_TEACHER;
    }

    /**
     * 助教签到
     * @return
     */
    public static String getUrlHomeCourseSignTutor(){
        return URL_HOST + URL_HOME_COURSE_SIGN_TUTOR;
    }

    /**
     * 发布作品
     * @return
     */
    public static String getUrlHomeCourseStuPublish(){
        return URL_HOST + URL_HOME_COURSE_STU_PUBLISH;
    }

    /**
     * 取课件类型
     * @return
     */
    public static String getUrlHomeCourseProject(){
        return URL_HOST + URL_HOME_COURSE_PROJECT;
    }

    /**
     * 运营主界面信息
     * @return
     */
    public static String getUrlBusinessTaskService(){
        return URL_HOST + URL_BUSINESS_TASK_SERVICE;
    }

	/**
	 * 教学主界面信息
	 * @return
	 */
	public static String getUrlTeacherTrainingService(){
    	return URL_HOST + URL_TEACHER_TRAINING_SERVICE;
	}

    /**
     * 取用户的消息
     * @return
     */
	public static String getUrlMessageService(){
        return URL_HOST + URL_MESSAGE_SERVICE;
    }

    /**
     * 主界面信息
     * @return
     */
    public static String getUrlHomeService(){
	    return URL_HOST + URL_HOME_SERVICE;
    }

    /**
     * 提交登录信息
     * @return
     */
    public static String getUrlLoginService(){
        return URL_HOST + URL_LOGIN_SERVICE;
    }

}
