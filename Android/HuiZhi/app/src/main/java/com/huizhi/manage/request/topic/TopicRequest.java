package com.huizhi.manage.request.topic;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huizhi.manage.base.ThreadPoolDo;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.CourseWareCategoryNode;
import com.huizhi.manage.node.CourseWareTypeNode;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.util.JSONUtil;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class TopicRequest {

    /**
     * 取课件类型
     * @param categoryId
     * @param handler
     */
    public void getCourseWareType(String categoryId, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_COURSE_WARE_TYPE));
        params.add(new BasicNameValuePair("CategoryId", categoryId));
        ThreadPoolDo.getInstance().executeThread(new CourseWareTypeThread(params, handler));
    }

    private class CourseWareTypeThread extends Thread {
        private List<BasicNameValuePair> params;
        private Handler handler;

        public CourseWareTypeThread(List<BasicNameValuePair> params, Handler handler) {
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeCourseProject(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    String data = resultNode.getReturnObj();
                    if(TextUtils.isEmpty(data))
                        return;
                    Gson gson = new Gson();
                    CourseWareTypeNode node = gson.fromJson(data, CourseWareTypeNode.class);
                    if(handler!=null)
                        handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, node));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param categoryId
     * @param handler
     */
    public void getCourseCategory(String categoryId, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_COURSE_WARE_CATEGORY));
        params.add(new BasicNameValuePair("CategoryId", categoryId));
        ThreadPoolDo.getInstance().executeThread(new CourseWareCategoryThread(params, handler));
    }

    private class CourseWareCategoryThread extends Thread {
        private List<BasicNameValuePair> params;
        private Handler handler;

        public CourseWareCategoryThread(List<BasicNameValuePair> params, Handler handler) {
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeCourseProject(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    String data = resultNode.getReturnObj();
                    if(TextUtils.isEmpty(data))
                        return;
                    Gson gson = new Gson();
                    List<CourseWareCategoryNode> nodes = gson.fromJson(data, new TypeToken<List<CourseWareCategoryNode>>(){}.getType());
                    if(handler!=null)
                        handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, nodes));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
