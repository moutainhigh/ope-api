package com.xianjinxia.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;

public class RiskRequest {

    private String app_id;

    private String method;

    private JSONObject biz_data;

    public RiskRequest(String app_id, String method, JSONObject biz_data) {
        this.app_id = app_id;
        this.method = method;
        this.biz_data = biz_data;
    }

    public RiskRequest(){

    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public JSONObject getBiz_data() {
        return biz_data;
    }

    public void setBiz_data(JSONObject biz_data) {
        this.biz_data = biz_data;
    }

    public static RiskRequest parse(String requestStr){
        return JSONObject.parseObject(requestStr,RiskRequest.class);
    }

    /**
     * 请求参数检查
     * @return
     */
    public boolean simpleValidate(){
        Preconditions.checkNotNull(method,"method can not be null!");
        Preconditions.checkNotNull(biz_data,"biz_data can not be null!");
        Preconditions.checkNotNull(app_id,"appId can not be null!");
        return true;
    }

    @Override
    public String toString() {
        return "RiskRequest{" +
                "app_id='" + app_id + '\'' +
                ", method='" + method + '\'' +
                ", biz_data=" + biz_data +
                '}';
    }
}
