package com.huizhi.manage.request.login;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.util.JSONUtil;
import com.huizhi.manage.base.ThreadPoolDo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by CL on 2018/1/6.
 */

public class LoginRequest {

    /**
     * 用户登录
     * @param account
     * @param password
     * @param phonetype 1:iPhone 2:Android
     * @param registrationid
     * @param handler
     */
    public void loginRequest(String account, String password, int phonetype, String registrationid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHOD_LOGIN));
        params.add(new BasicNameValuePair("UserName", account));
        params.add(new BasicNameValuePair("UserPsw", password));
        params.add(new BasicNameValuePair("PhoneType", String.valueOf(phonetype)));
        params.add(new BasicNameValuePair("RegistrationId", registrationid));
        ThreadPoolDo.getInstance().executeThread(new LoginThread(params, handler));
    }

    private class LoginThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public LoginThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlLogin(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, resultNode.getMessage()));
                }else
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_FAILURE, resultNode.getMessage()));
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        private void parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return;
            try {
                JSONObject json = new JSONObject(jsonStr);
                UserInfo.getInstance().getUser().setTeacherId(JSONUtil.parseString(json, "TeacherId"));
                UserInfo.getInstance().getUser().setTeacherName(JSONUtil.parseString(json, "TeacherName"));
                UserInfo.getInstance().getUser().setPhoneNumber(JSONUtil.parseString(json, "PhoneNumber"));
                UserInfo.getInstance().getUser().setHeadImgUrl(JSONUtil.parseString(json, "HeadImgUrl"));
                UserInfo.getInstance().getUser().setEmail(JSONUtil.parseString(json, "Email"));
                UserInfo.getInstance().getUser().setStrCreateTime(JSONUtil.parseString(json, "strCreateTime"));
                UserInfo.getInstance().getUser().setSchoolId(JSONUtil.parseString(json, "SchoolId"));
                UserInfo.getInstance().getUser().setDistrictId(JSONUtil.parseInt(json, "DistrictId"));
                UserInfo.getInstance().getUser().setSchoolName(JSONUtil.parseString(json, "SchoolName"));
                UserInfo.getInstance().getUser().setRoleType(JSONUtil.parseInt(json, "RoleType"));
                UserInfo.getInstance().getUser().setRoleTypeName(JSONUtil.parseString(json, "RoleTypeName"));
                UserInfo.getInstance().getUser().setAlias(JSONUtil.parseString(json, "Alias"));
                UserInfo.getInstance().getUser().setAdmin(JSONUtil.parseBoolean(json, "IsAdmin"));
                UserInfo.getInstance().getUser().setChatToken(JSONUtil.parseString(json, "token"));
                UserInfo.getInstance().getUser().setJobStatus(JSONUtil.parseInt(json, "JobStatus"));
                String appKey = JSONUtil.parseString(json, "appKey");
                Log.i("HuiZhi", "The rong key is:" + appKey);
                UserInfo.getInstance().getUser().setAppKey(appKey);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}
