package com.wkswind.demo.network;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016-11-19.
 */

public class HttpResponse {
    private static final int OK = 0, ERROR = -1;
    int status;
    JSONObject content;
    String desc;

    public HttpResponse(JSONObject json) {
        status = json.optInt("status", ERROR);
        content = json.optJSONObject("content");
        desc = json.optString("desc");
    }

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
