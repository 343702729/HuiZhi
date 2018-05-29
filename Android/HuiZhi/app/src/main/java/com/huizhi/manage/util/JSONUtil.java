package com.huizhi.manage.util;

import android.text.TextUtils;

import com.huizhi.manage.node.ResultNode;

import org.json.JSONObject;

/**
 * Created by CL on 2018/1/6.
 */

public class JSONUtil {

    /**
     * 解析json为ResultNode对象
     * @param json
     * @return
     */
    public static ResultNode parseResult(String json){
        if(TextUtils.isEmpty(json))
            return null;
        ResultNode node = new ResultNode();
        try {
            JSONObject jsonOb = new JSONObject(json);
            node.setResult(jsonOb.getInt("Result"));
            node.setMessage(jsonOb.getString("Message"));
            node.setReturnObj(jsonOb.getString("ReturnObj"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return node;
    }

    public static int parseInt(JSONObject jsonOb, String name){
        try {
            return jsonOb.getInt(name);
        }catch (Exception e){
            return 0;
        }
    }

    public static String parseString(JSONObject jsonOb, String name){
        try {
            return jsonOb.getString(name);
        }catch (Exception e){
            return null;
        }
    }

    public static boolean parseBoolean(JSONObject jsonOb, String name){
        try {
            return jsonOb.getBoolean(name);
        }catch (Exception e){
            return false;
        }
    }
}
