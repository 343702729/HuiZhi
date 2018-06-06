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
     * @param teachername
     * @param handler
     */
    public void getCourseList(String teachername, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_COURSE_LIST));
        params.add(new BasicNameValuePair("TeacherName", teachername));
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

    public void getCourseInfo(String teacherName, String LessonNum, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_COURSE_INFO));
        params.add(new BasicNameValuePair("TeacherName", teacherName));
        params.add(new BasicNameValuePair("LessonNum", LessonNum));
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
}