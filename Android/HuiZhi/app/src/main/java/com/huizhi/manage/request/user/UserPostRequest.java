package com.huizhi.manage.request.user;

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

/**
 * Created by CL on 2018/1/30.
 */

public class UserPostRequest {

    /**
     * 设置用户头像
     * @param teacherid
     * @param headimg
     * @param handler
     */
    public void postUserHeadPortrait(String teacherid, String headimg, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHRD_HEAD_PORTRAIT));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("HeadImg", headimg));
        ThreadPoolDo.getInstance().executeThread(new HeadPortraitThread(params, handler));
    }

    /**
     * 修改密码
     * @param teacherid
     * @param oldUserPsw
     * @param newUserPsw
     * @param handler
     */
    public void postUserPasswordEdit(String teacherid, String oldUserPsw, String newUserPsw, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_USER_PASSWORD_EDIT));
        params.add(new BasicNameValuePair("TeacherId", teacherid));
        params.add(new BasicNameValuePair("OldUserPsw", oldUserPsw));
        params.add(new BasicNameValuePair("NewUserPsw", newUserPsw));
        ThreadPoolDo.getInstance().executeThread(new UserPasswordEditThread(params, handler));
    }

    private class HeadPortraitThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public HeadPortraitThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlHeadPortrait(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){

            }
        }
    }

    private class UserPasswordEditThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public UserPasswordEditThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.postHttpConnect(URLData.getUrlUserPasswordEdit(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, resultNode.getMessage()));
                }else{
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
                }
            }catch (Exception e){

            }
        }
    }

}
