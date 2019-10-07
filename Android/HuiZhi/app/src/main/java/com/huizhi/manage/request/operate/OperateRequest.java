package com.huizhi.manage.request.operate;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.huizhi.manage.base.ThreadPoolDo;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.HomeOperateNode;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.util.JSONUtil;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class OperateRequest {

    public void getHomeOperate(Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_HOME_OPERATE));
        ThreadPoolDo.getInstance().executeThread(new HomeOperateThread(params, handler));
    }

    private class HomeOperateThread extends Thread {
        private List<BasicNameValuePair> params;
        private Handler handler;

        public HomeOperateThread(List<BasicNameValuePair> params, Handler handler) {
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlBusinessTaskService(), params);
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
                    HomeOperateNode node = gson.fromJson(data, HomeOperateNode.class);
                    if(handler!=null)
                        handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
