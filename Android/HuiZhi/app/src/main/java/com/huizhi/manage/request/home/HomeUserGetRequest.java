package com.huizhi.manage.request.home;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.base.ThreadPoolDo;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.AttendanceInfoNode;
import com.huizhi.manage.node.BannerNode;
import com.huizhi.manage.node.EmailInfoNode;
import com.huizhi.manage.node.MessageNode;
import com.huizhi.manage.node.NewNode;
import com.huizhi.manage.node.NewPageNode;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.node.TaskNode;
import com.huizhi.manage.node.TaskSummaryNode;
import com.huizhi.manage.util.JSONUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/16.
 */

public class HomeUserGetRequest {

    /**
     * 获取个人的任务统计信息
     * @param userid
     * @param handler
     * @param schoolid
     */
    public void getUserTaskSummary(String userid, String schoolid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_TASK_INFO));
        params.add(new BasicNameValuePair("UserId", userid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        ThreadPoolDo.getInstance().executeThread(new TaskSummaryThread(params, handler));
    }

    /**
     * 获取未读邮件数和登录地址
     * @param email
     * @param handler
     */
    public void getEmailInfo(String email, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_EMAIL_INFO));
        params.add(new BasicNameValuePair("Email", email));
        ThreadPoolDo.getInstance().executeThread(new EmailInfoThread(params, handler));
    }

    /**
     * 主界面的新闻轮播图列表
     * @param handler
     */
    public void getUserNewsBanner(Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_NEWS_BANNER));
        ThreadPoolDo.getInstance().executeThread(new NewsBanner(params, handler));
    }

    /**
     * 取当日的考勤信息
     * @param teacherid
     * @param schoolid
     * @param handler
     */
    public void getUserAttendanceInfo(String teacherid, String schoolid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_ATTENDANCE_INFO));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        ThreadPoolDo.getInstance().executeThread(new AttendanceInfo(params, handler));
    }

    /**
     * 获取信息列表
     * @param teacherid
     * @param schoolid
     * @param handler
     */
    public void getUserMessageList(String teacherid, String schoolid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_MESSAGE_LIST));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        ThreadPoolDo.getInstance().executeThread(new MessageListThread(params, handler));
    }

    /**
     * 获取信息详情
     * @param messageid
     * @param teacherid
     * @param schoolid
     * @param handler
     */
    public void getUserMessageInfo(String messageid, String teacherid, String schoolid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_MESSAGE_INFO));
        params.add(new BasicNameValuePair("NoticeId", messageid));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        ThreadPoolDo.getInstance().executeThread(new MessageInfoThread(params, handler));
    }

    /**
     * 获取新闻列表
     * @param page
     * @param pagesize
     * @param handler
     */
    public void getNewList(int page, int pagesize, String catrgoryId, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_NEWS_LIST));
        params.add(new BasicNameValuePair("page", String.valueOf(page)));
        params.add(new BasicNameValuePair("pagesize", String.valueOf(pagesize)));
        params.add(new BasicNameValuePair("CategoryId", catrgoryId));
        ThreadPoolDo.getInstance().executeThread(new NewListThread(params, handler));
    }

    /**
     * 未运营分享未读数量
     * @param teacherId
     * @param handler
     */
    public void getFXNoReadCount(String teacherId, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_SHARE_COUNT));
        params.add(new BasicNameValuePair("TeacherId", teacherId));
        ThreadPoolDo.getInstance().executeThread(new FXNoReadCountThread(params, handler));
    }

    /**
     * 公告未读数量
     * @param teacherId
     * @param handler
     */
    public void getTZNoReadCount(String teacherId, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_NOTICE_COUNT));
        params.add(new BasicNameValuePair("TeacherId", teacherId));
        ThreadPoolDo.getInstance().executeThread(new NoticeNoReadCountThread(params, handler));
    }

    /**
     * 资料未读数量
     * @param teacherId
     * @param handler
     */
    public void getFileNoReadCound(String teacherId, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_FILE_COUNT));
        params.add(new BasicNameValuePair("TeacherId", teacherId));
        ThreadPoolDo.getInstance().executeThread(new FileNoReadCountThread(params, handler));
    }

    private class TaskSummaryThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TaskSummaryThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlTeamUsers(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    TaskSummaryNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_TWO, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private TaskSummaryNode parseReturn(String jsonStr){
            try {
                TaskSummaryNode node = new TaskSummaryNode();
                JSONObject jsonOb = new JSONObject(jsonStr);
                node.setTotalWeekTaskCount(JSONUtil.parseInt(jsonOb, "TotalWeekTaskCount"));
                node.setTotalWeekDoneTaskCount(JSONUtil.parseInt(jsonOb, "TotalWeekDoneTaskCount"));
                node.setTotalWeekToDoTaskCount(JSONUtil.parseInt(jsonOb, "TotalWeekToDoTaskCount"));
                node.setTotalTaskCount(JSONUtil.parseInt(jsonOb, "TotalTaskCount"));
                node.setTotalDoneTaskCount(JSONUtil.parseInt(jsonOb, "TotalDoneTaskCount"));
                node.setTotalToDoTaskCount(JSONUtil.parseInt(jsonOb, "TotalToDoTaskCount"));
                node.setTotalTodayToDoTaskCount(JSONUtil.parseInt(jsonOb, "TotalTodayToDoTaskCount"));
                return node;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class EmailInfoThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public EmailInfoThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeEmailInfo(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    EmailInfoNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_THREE, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private EmailInfoNode parseReturn(String jsonStr){
            try {
                EmailInfoNode node = new EmailInfoNode();
                JSONObject jsonOb = new JSONObject(jsonStr);
                node.setTotCount(JSONUtil.parseInt(jsonOb, "TotCount"));
                node.setMailboxUrl(JSONUtil.parseString(jsonOb, "MailboxUrl"));
                return node;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    private class NewsBanner extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public NewsBanner(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlNewsBanner(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    List<BannerNode> node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private List<BannerNode> parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            List<BannerNode> nodes = new ArrayList<>();
            try {
                JSONArray jsonAr = new JSONArray(jsonStr);
                BannerNode node = null;
                JSONObject jsonOb = null;
                for (int i=0; i<jsonAr.length(); i++){
                    jsonOb = jsonAr.getJSONObject(i);
                    node = new BannerNode();
                    node.setNewsId(JSONUtil.parseString(jsonOb, "NewsId"));
                    node.setImgUrl(JSONUtil.parseString(jsonOb, "ImgUrl"));
                    nodes.add(node);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return nodes;
        }
    }

    private class AttendanceInfo extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public AttendanceInfo(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeAttendanceInfo(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    AttendanceInfoNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private AttendanceInfoNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            AttendanceInfoNode node = new AttendanceInfoNode();
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                node.setStartTime(JSONUtil.parseString(jsonOb, "StartTime"));
                node.setEndTime(JSONUtil.parseString(jsonOb, "EndTime"));
                node.setDaysOFMonth(JSONUtil.parseString(jsonOb, "DaysOFMonth"));
                node.setDaysOFYear(JSONUtil.parseString(jsonOb, "DaysOFYear"));
                node.setClockInLate(JSONUtil.parseBoolean(jsonOb, "IsClockInLate"));
                node.setClockOutEarly(JSONUtil.parseBoolean(jsonOb, "IsClockOutEarly"));
            }catch (Exception e){

            }
            return node;
        }
    }

    private class MessageListThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public MessageListThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeMessageList(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    List<MessageNode> node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private List<MessageNode> parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            List<MessageNode> nodes = new ArrayList<>();
            try {
                JSONArray jsonAr = new JSONArray(jsonStr);
                MessageNode node = null;
                JSONObject jsonOb = null;
                for (int i=0; i<jsonAr.length(); i++){
                    jsonOb = jsonAr.getJSONObject(i);
                    node = new MessageNode();
                    node.setNoticeId(JSONUtil.parseString(jsonOb, "NoticeId"));
                    node.setSchoolId(JSONUtil.parseString(jsonOb, "SchoolId"));
                    node.setTitle(JSONUtil.parseString(jsonOb, "Title"));
                    node.setContent(JSONUtil.parseString(jsonOb, "Content"));
                    node.setPush(JSONUtil.parseBoolean(jsonOb, "IsPush"));
                    node.setSender(JSONUtil.parseString(jsonOb, "Sender"));
                    node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                    node.setIsRead(JSONUtil.parseInt(jsonOb, "IsRead"));
                    node.setSchoolList(JSONUtil.parseString(jsonOb, "SchoolList"));
                    nodes.add(node);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return nodes;
        }
    }

    private class MessageInfoThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public MessageInfoThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeMessageInfo(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    MessageNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private MessageNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            MessageNode node = null;
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                node = new MessageNode();
                node.setNoticeId(JSONUtil.parseString(jsonOb, "NoticeId"));
                node.setSchoolId(JSONUtil.parseString(jsonOb, "SchoolId"));
                node.setTitle(JSONUtil.parseString(jsonOb, "Title"));
                node.setContent(JSONUtil.parseString(jsonOb, "Content"));
                node.setPush(JSONUtil.parseBoolean(jsonOb, "IsPush"));
                node.setSender(JSONUtil.parseString(jsonOb, "Sender"));
                node.setStrCreateTime(JSONUtil.parseString(jsonOb, "strCreateTime"));
                node.setIsRead(JSONUtil.parseInt(jsonOb, "IsRead"));
                node.setSchoolList(JSONUtil.parseString(jsonOb, "SchoolList"));
            }catch (Exception e){
                e.printStackTrace();
            }
            return node;
        }
    }

    private class NewListThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public NewListThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlNewsList(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    NewPageNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private NewPageNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            NewPageNode newPageNode = new NewPageNode();
            try {
                JSONObject jsonIt = new JSONObject(jsonStr);
                newPageNode.setTotCount(JSONUtil.parseInt(jsonIt, "TotCount"));
                newPageNode.setTotPageCount(JSONUtil.parseInt(jsonIt, "TotPageCount"));

                List<NewNode> nodes = new ArrayList<>();
                JSONArray jsonAr = jsonIt.getJSONArray("ItemList");
                NewNode node = null;
                JSONObject jsonOb = null;
                for (int i=0; i<jsonAr.length(); i++){
                    jsonOb = jsonAr.getJSONObject(i);
                    node = new NewNode();
                    node.setNewsId(JSONUtil.parseString(jsonOb, "NewsId"));
                    node.setNewsTitle(JSONUtil.parseString(jsonOb, "NewsTitle"));
                    node.setReferenceNewsId(JSONUtil.parseString(jsonOb, "ReferenceNewsId"));
                    node.setThumbImgUrl(JSONUtil.parseString(jsonOb, "ThumbImgUrl"));
                    node.setStrCreateTime1(JSONUtil.parseString(jsonOb, "strCreateTime1"));
                    nodes.add(node);
                }
                newPageNode.setNews(nodes);
            }catch (Exception e){
                e.printStackTrace();
            }
            return newPageNode;
        }

    }

    private class FXNoReadCountThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public FXNoReadCountThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlShareCount(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_FOUR, getNoReadCount(resultNode.getReturnObj())));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private String getNoReadCount(String result){
        try {
            JSONObject jsonIt = new JSONObject(result);
            return jsonIt.getString("NoReadCount");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "0";
    }

    private class FileNoReadCountThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public FileNoReadCountThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlFileCount(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, getNoReadCount(resultNode.getReturnObj())));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class NoticeNoReadCountThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public NoticeNoReadCountThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlNoticeCount(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_FIVE, getNoReadCount(resultNode.getReturnObj())));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
