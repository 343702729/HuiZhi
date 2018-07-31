package com.huizhi.manage.request.main;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.huizhi.manage.base.BaseInfoUpdate;
import com.huizhi.manage.data.Constants;
import com.huizhi.manage.data.UserInfo;
import com.huizhi.manage.http.HttpConnect;
import com.huizhi.manage.http.URLData;
import com.huizhi.manage.node.ResultNode;
import com.huizhi.manage.node.UserNode;
import com.huizhi.manage.node.VersionNode;
import com.huizhi.manage.util.JSONUtil;
import com.huizhi.manage.base.ThreadPoolDo;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CL on 2018/1/6.
 */

public class MainRequest {

    /**
     * 获取校区其他成员信息
     * @param userid
     * @param schoolid
     * @param handler
     */
    public void getTeamUsers(String userid, String schoolid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_TEAM_USERS));
        params.add(new BasicNameValuePair("UserId", userid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        ThreadPoolDo.getInstance().executeThread(new TeamUsersThread(params, handler));
    }

    /**
     * 取本校区人员和客服人员
     * @param userid
     * @param schoolid
     * @param handler
     */
    public void getTalkUsers(String userid, String schoolid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_TALK_USERS));
        params.add(new BasicNameValuePair("UserId", userid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        ThreadPoolDo.getInstance().executeThread(new TalkUsersThread(params, handler));
    }

    private class TeamUsersThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TeamUsersThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlTeamUsers(), params);
                Log.i("Login", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("Login", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                parseReturn(resultNode.getReturnObj());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return;
            try {
                JSONArray jsonAr = new JSONArray(jsonStr);
                UserNode userNode = null;
                List<UserNode> users = new ArrayList<>();
                for(int i=0; i<jsonAr.length(); i++){
                    JSONObject itemJs = jsonAr.getJSONObject(i);
                    userNode = new UserNode();
                    userNode.setTeacherId(JSONUtil.parseString(itemJs, "TeacherId"));
                    userNode.setTeacherName(JSONUtil.parseString(itemJs, "TeacherName"));
                    userNode.setPhoneNumber(JSONUtil.parseString(itemJs, "PhoneNumber"));
                    userNode.setHeadImgUrl(JSONUtil.parseString(itemJs, "HeadImgUrl"));
                    userNode.setStrCreateTime(JSONUtil.parseString(itemJs, "strCreateTime"));
                    userNode.setSchoolId(JSONUtil.parseString(itemJs, "SchoolId"));
                    userNode.setDistrictId(JSONUtil.parseInt(itemJs, "DistrictId"));
                    userNode.setSchoolName(JSONUtil.parseString(itemJs, "SchoolName"));
                    userNode.setRoleType(JSONUtil.parseInt(itemJs, "RoleType"));
                    userNode.setRoleTypeName(JSONUtil.parseString(itemJs, "RoleTypeName"));
                    userNode.setAdmin(JSONUtil.parseBoolean(itemJs, "IsAdmin"));
                    users.add(userNode);
                    userNode = null;
                }
                UserInfo.getInstance().setTeamUsers(users);
                UserInfo.getInstance().setTaskUsers(users);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private class TalkUsersThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public TalkUsersThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlTalkUsers(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                parseReturn(resultNode.getReturnObj());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private void parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return;
            try {
                JSONArray jsonAr = new JSONArray(jsonStr);
                List<UserNode> users = new ArrayList<>();
                for(int i=0; i<jsonAr.length(); i++){
                    JSONObject jsonOb = jsonAr.getJSONObject(i);
                    String groupName = JSONUtil.parseString(jsonOb, "GroupName");
//                    JSONArray jsonArItem = jsonOb.getJSONArray("UserList");
                    JSONArray jsonArItem = JSONUtil.parseArray(jsonOb, "UserList");
                    if(jsonArItem==null||jsonArItem.length()==0)
                        continue;
                    UserNode titleU = new UserNode();
                    titleU.setTeacherName(groupName);
                    titleU.setType(1);
                    users.add(titleU);
                    for(int j=0; j<jsonArItem.length(); j++){
                        JSONObject jsonObItem = jsonArItem.getJSONObject(j);
                        UserNode user = new UserNode();
                        user.setTeacherId(JSONUtil.parseString(jsonObItem, "TeacherId"));
                        user.setTeacherName(JSONUtil.parseString(jsonObItem, "TeacherName"));
                        user.setHeadImgUrl(JSONUtil.parseString(jsonObItem, "FullHeadImgUrlThumb"));
                        user.setRoleTypeName(JSONUtil.parseString(jsonObItem, "RoleTypeName"));
                        users.add(user);
                    }
                }
                UserInfo.getInstance().setTalkUsers(users);
            }catch (Exception e){

            }
        }
    }

    /**
     * 取本校区人员和客服人员
     * @param userid
     * @param schoolid
     * @param handler
     */
    public void getSchoolTalkUsers(String userid, String schoolid, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_TALK_USERS));
        params.add(new BasicNameValuePair("UserId", userid));
        params.add(new BasicNameValuePair("SchoolId", schoolid));
        ThreadPoolDo.getInstance().executeThread(new SchoolTalkUsersThread(params, handler));
    }

    private class SchoolTalkUsersThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public SchoolTalkUsersThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlTalkUsers(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
//                parseReturn(resultNode.getReturnObj());
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    List<UserNode> nodes = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, nodes));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private List<UserNode> parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            try {
                JSONArray jsonAr = new JSONArray(jsonStr);
                List<UserNode> users = new ArrayList<>();
                for(int i=0; i<jsonAr.length(); i++){
                    JSONObject jsonOb = jsonAr.getJSONObject(i);
                    String groupName = JSONUtil.parseString(jsonOb, "GroupName");
                    JSONArray jsonArItem = JSONUtil.parseArray(jsonOb, "UserList");
                    if(jsonArItem==null||jsonArItem.length()==0)
                        continue;
                    UserNode titleU = new UserNode();
                    titleU.setTeacherName(groupName);
                    titleU.setType(1);
                    users.add(titleU);
                    for(int j=0; j<jsonArItem.length(); j++){
                        JSONObject jsonObItem = jsonArItem.getJSONObject(j);
                        UserNode user = new UserNode();
                        user.setTeacherId(JSONUtil.parseString(jsonObItem, "TeacherId"));
                        user.setTeacherName(JSONUtil.parseString(jsonObItem, "TeacherName"));
                        user.setHeadImgUrl(JSONUtil.parseString(jsonObItem, "FullHeadImgUrlThumb"));
                        user.setRoleTypeName(JSONUtil.parseString(jsonObItem, "RoleTypeName"));
                        users.add(user);
                    }
                }
//                UserInfo.getInstance().setTalkUsers(users);
                return users;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 取聊天的人员（单人）
     * @param userid
     */
    public void getTalkItemUser(String userid, BaseInfoUpdate infoUpdate){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_TALK_ITEM_USER));
        params.add(new BasicNameValuePair("UserId", userid));
        ThreadPoolDo.getInstance().executeThread(new TalkItemUserThread(params, infoUpdate));
    }

    private class TalkItemUserThread extends Thread{
        private List<BasicNameValuePair> params;
        private BaseInfoUpdate infoUpdate;

        public TalkItemUserThread(List<BasicNameValuePair> params, BaseInfoUpdate infoUpdate){
            this.params = params;
            this.infoUpdate = infoUpdate;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlTalkItemUser(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    UserNode node = parseReturn(resultNode.getReturnObj());
                    if(infoUpdate!=null)
                        infoUpdate.update(node);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private UserNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                UserNode user = new UserNode();
                user.setTeacherId(JSONUtil.parseString(jsonOb, "TeacherId"));
                user.setTeacherName(JSONUtil.parseString(jsonOb, "TeacherName"));
                user.setHeadImgUrl(JSONUtil.parseString(jsonOb, "FullHeadImgUrlThumb"));
                user.setRoleTypeName(JSONUtil.parseString(jsonOb, "RoleTypeName"));
                return user;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 获取新版本
     * @param versioncode
     * @param handler
     */
    public void getVersion(int versioncode, Handler handler){
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("Method", URLData.METHORD_VERSION));
        params.add(new BasicNameValuePair("VersionCode", String.valueOf(versioncode)));
        ThreadPoolDo.getInstance().executeThread(new VersionGetThread(params, handler));
    }

    private class VersionGetThread extends Thread{
        private List<BasicNameValuePair> params;
        private Handler handler;

        public VersionGetThread(List<BasicNameValuePair> params, Handler handler){
            this.params = params;
            this.handler = handler;
        }

        @Override
        public void run() {
            super.run();
            try {
                String result = HttpConnect.getHttpConnect(URLData.getUrlVersion(), params);
                Log.i("HuiZhi", "The result:" + result);
                if(TextUtils.isEmpty(result))
                    return;
                ResultNode resultNode = JSONUtil.parseResult(result);
                Log.i("HuiZhi", "The result:" + resultNode.getResult() + "  message:" + resultNode.getMessage() + "  returnObj:" + resultNode.getReturnObj());
                if(resultNode == null)
                    return;
                if(resultNode.getResult() == Constants.RESULT_SUCCESS) {
                    VersionNode node = parseReturn(resultNode.getReturnObj());
                    handler.sendMessage(handler.obtainMessage(Constants.MSG_SUCCESS, node));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private VersionNode parseReturn(String jsonStr){
            if(TextUtils.isEmpty(jsonStr))
                return null;
            VersionNode node = new VersionNode();
            try {
                JSONObject jsonOb = new JSONObject(jsonStr);
                node.setVersionCode(JSONUtil.parseInt(jsonOb, "VersionCode"));
                node.setVersionName(JSONUtil.parseString(jsonOb, "VersionName"));
                node.setVersionDetail(JSONUtil.parseString(jsonOb, "VersionDetail"));
                node.setDownloadUrl(JSONUtil.parseString(jsonOb, "DownloadUrl"));
            }catch (Exception e){
                e.printStackTrace();
            }
            return node;
        }
    }
}
