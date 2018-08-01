package com.huizhi.manage.request.home;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.base.ThreadPoolDo;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.CourseInfoNode;
import com.huizhi.manage.node.CourseNode;
import com.huizhi.manage.node.MessageNode;
import com.huizhi.manage.node.PictureNode;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.node.StudentNode;
import com.huizhi.manage.util.JSONUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeCourseGetRequest {

    /**
     * 取课程列表
     * @param teachernum
     * @param handler
     */
    public void getCourseList(String teachernum, String lessonDate, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_COURSE_LIST));
        params.add(new BasicNameValuePair("TeacherNum", teachernum));
        params.add(new BasicNameValuePair("LessonDate", lessonDate));
        ThreadPoolDo.getInstance().executeThread(new CourseListThread(params, handler));
    }

    private class CourseListThread extends Thread {
        private List<BasicNameValuePair> params;
        private Handler handler;

        public CourseListThread(List<BasicNameValuePair> params, Handler handler) {
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeCourseList(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    CourseInfoNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        private CourseInfoNode parseReturn(String jsonStr){
            CourseInfoNode infoNode = new CourseInfoNode();
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                infoNode.setTotLessonNum(JSONUtil.parseInt(jsonOb, "TotLessonNum"));
                infoNode.setDoneLessonNum(JSONUtil.parseInt(jsonOb, "DoneLessonNum"));
                infoNode.setCommentedNum(JSONUtil.parseInt(jsonOb, "CommentedNum"));
                infoNode.setToBeCommentNum(JSONUtil.parseInt(jsonOb, "ToBeCommentNum"));
                infoNode.setLessonFinishPercent(JSONUtil.parseString(jsonOb, "LessonFinishPercent"));
                infoNode.setCommentFinishPercent(JSONUtil.parseString(jsonOb, "CommentFinishPercent"));
                JSONArray jsonAr = jsonOb.getJSONArray("Lessons");
                if(jsonAr!=null){
                    JSONObject itemJS;
                    CourseNode node;
                    List<CourseNode> nodes = new ArrayList<>();
                    for (int i=0; i<jsonAr.length(); i++){
                        itemJS = jsonAr.getJSONObject(i);
                        node = new CourseNode();
                        node.setLessonNum(JSONUtil.parseString(itemJS, "LessonNum"));
                        node.setLessonName(JSONUtil.parseString(itemJS, "LessonName"));
                        node.setLessonStatus(JSONUtil.parseString(itemJS, "LessonStatus"));
                        node.setLessonTime(JSONUtil.parseString(itemJS, "LessonTime"));
                        node.setAllStuCount(JSONUtil.parseInt(itemJS, "AllStuCount"));
                        node.setSignedCount(JSONUtil.parseInt(itemJS, "SignedCount"));
                        node.setLeaveStuCount(JSONUtil.parseInt(itemJS, "LeaveStuCount"));
                        node.setPublishWorkCount(JSONUtil.parseInt(itemJS, "PublishWorkCount"));
                        node.setCommentedCount(JSONUtil.parseInt(itemJS, "CommentedCount"));
                        node.setCompletionRate(JSONUtil.parseString(itemJS, "CompletionRate"));
                        nodes.add(node);
                    }
                    infoNode.setLessons(nodes);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return infoNode;
        }
    }

    /**
     * 取课程详情
     * @param teacherName
     * @param lessonNum
     * @param handler
     */
    public void getCourseInfo(String teacherName, String lessonNum, String stuName, String status, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_COURSE_INFO));
        params.add(new BasicNameValuePair("TeacherName", teacherName));
        params.add(new BasicNameValuePair("LessonNum", lessonNum));
        params.add(new BasicNameValuePair("StuName", stuName));
        params.add(new BasicNameValuePair("Status", status));
        ThreadPoolDo.getInstance().executeThread(new CourseInfoThread(params, handler));
    }

    private class CourseInfoThread extends Thread {
        private List<BasicNameValuePair> params;
        private Handler handler;

        public CourseInfoThread(List<BasicNameValuePair> params, Handler handler) {
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeCourseInfo(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    CourseNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private CourseNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            CourseNode node = new CourseNode();
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                JSONObject lessonJS = jsonOb.getJSONObject("Lesson");
                node.setLessonName(JSONUtil.parseString(lessonJS, "LessonName"));
                node.setLessonTime(JSONUtil.parseString(lessonJS, "LessonTime"));
                node.setAllStuCount(JSONUtil.parseInt(lessonJS, "AllStuCount"));
                node.setSignedCount(JSONUtil.parseInt(lessonJS, "SignedCount"));
                node.setLeaveStuCount(JSONUtil.parseInt(lessonJS, "LeaveStuCount"));
                node.setPublishWorkCount(JSONUtil.parseInt(lessonJS, "PublishWorkCount"));
                node.setCommentedCount(JSONUtil.parseInt(lessonJS, "CommentedCount"));
                node.setCompletionRate(JSONUtil.parseString(lessonJS, "CompletionRate"));
                node.setSignInTeacher(JSONUtil.parseBoolean(lessonJS, "IsSignInTeacher"));
                node.setSignInTutor(JSONUtil.parseBoolean(lessonJS, "IsSignInTutor"));
                node.setIsUploadedWork(JSONUtil.parseInt(lessonJS, "IsUploadedWork"));

                JSONArray studentsJS = jsonOb.getJSONArray("Students");
                if(studentsJS!=null){
                    JSONObject itemJO;
                    List<StudentNode> studentNodes = new ArrayList<>();
                    StudentNode studentNode;
                    for (int i=0; i<studentsJS.length(); i++){
                        studentNode = new StudentNode();
                        itemJO = studentsJS.getJSONObject(i);
                        studentNode.setStuNum(JSONUtil.parseString(itemJO, "StuNum"));
                        studentNode.setStuName(JSONUtil.parseString(itemJO, "StuName"));
                        studentNode.setStuStatus(JSONUtil.parseInt(itemJO, "StuStatus"));
                        studentNode.setStrStuStatus(JSONUtil.parseString(itemJO, "StrStuStatus"));
                        studentNode.setPublished(JSONUtil.parseBoolean(itemJO, "IsPublished"));
                        studentNode.setFullHeadImgUrl(JSONUtil.parseString(itemJO, "FullHeadImgUrl"));
                        studentNodes.add(studentNode);
                    }
                    node.setStudentNodes(studentNodes);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return node;
        }
    }

    /**
     * 取学生作品信息
     * @param lessonNum
     * @param stuNum
     * @param handler
     */
    public void getCourseStuInfo(String lessonNum, String stuNum, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_COURSE_STU_INFO));
        params.add(new BasicNameValuePair("LessonNum", lessonNum));
        params.add(new BasicNameValuePair("StuNum", stuNum));
        ThreadPoolDo.getInstance().executeThread(new CourseStuInfoThread(params, handler));
    }

    private class CourseStuInfoThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public CourseStuInfoThread(List<BasicNameValuePair> params, Handler handler) {
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeCourseStuInfo(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    StudentNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private StudentNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            StudentNode node = new StudentNode();
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                node.setLessonNum(JSONUtil.parseString(jsonOb, "LessonNum"));
                node.setStuNum(JSONUtil.parseString(jsonOb, "StuNum"));
                node.setStuName(JSONUtil.parseString(jsonOb, "StuName"));
                node.setFullHeadImgUrl(JSONUtil.parseString(jsonOb, "FullHeadImgUrl"));
                node.setStuStatus(JSONUtil.parseInt(jsonOb, "StuStatus"));
                node.setStrStuStatus(JSONUtil.parseString(jsonOb, "StrStuStatus"));
                node.setWorkID(JSONUtil.parseString(jsonOb, "WorkID"));
                node.setTitle(JSONUtil.parseString(jsonOb, "Title"));
                node.setWorksPic(JSONUtil.parseString(jsonOb, "WorksPic"));
                node.setComment(JSONUtil.parseString(jsonOb, "Comment"));
                node.setPublished(JSONUtil.parseBoolean(jsonOb, "IsPublished"));
                JSONArray jsonAr = jsonOb.getJSONArray("Pictures"); //PictureNode
                if(jsonAr==null||jsonAr.length()==0)
                    return node;
                List<PictureNode> picNodes = new ArrayList<>();
                PictureNode picNode;
                JSONObject picJs;
                for (int i=0; i<jsonAr.length(); i++){
                    picNode = new PictureNode();
                    picJs = jsonAr.getJSONObject(i);
                    picNode.setServer(true);
                    picNode.setImageId(JSONUtil.parseString(picJs, "ImageId"));
                    picNode.setWorksId(JSONUtil.parseString(picJs, "WorksId"));
                    picNode.setUrl(JSONUtil.parseString(picJs, "ImageUrl"));
                    picNode.setFullImageUrl(JSONUtil.parseString(picJs, "FullImageUrl"));
                    picNode.setThumbImageUrl190(JSONUtil.parseString(picJs, "ThumbImageUrl190"));
                    picNode.setThumbImageUrl400(JSONUtil.parseString(picJs, "ThumbImageUrl400"));
                    picNodes.add(picNode);
                }
                node.setPictures(picNodes);
            }catch (Exception e){
                e.printStackTrace();
            }
            return node;
        }
    }
}
