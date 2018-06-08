package com.huizhi.manage.request.home;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.base.ThreadPoolDo;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.util.JSONUtil;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class HomeCoursePostRequest {

    public void postStudentsSignInfo(String lessonNum, String stuNums, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_COURSE_STU_SIGN));
        params.add(new BasicNameValuePair("LessonNum", lessonNum));
        params.add(new BasicNameValuePair("StuNums", stuNums));
        ThreadPoolDo.getInstance().executeThread(new StudentsSignThread(params, handler));
    }

    private class StudentsSignThread extends Thread {
        private List<BasicNameValuePair> params;
        private Handler handler;

        public StudentsSignThread(List<BasicNameValuePair> params, Handler handler) {
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlHomeCourseStuSign(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS_ONE, resultNode.getMessage()));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
