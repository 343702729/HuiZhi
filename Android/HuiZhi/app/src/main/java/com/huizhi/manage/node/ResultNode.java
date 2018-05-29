package com.huizhi.manage.node;

import org.json.JSONObject;

/**
 * Created by CL on 2018/1/6.
 */

public class ResultNode {
    private int result;
    private String message;
    private String returnObj;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(String returnObj) {
        this.returnObj = returnObj;
    }
}
