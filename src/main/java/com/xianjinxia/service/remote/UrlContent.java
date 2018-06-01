package com.xianjinxia.service.remote;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by wangwei on 2017/11/25.
 */
public class UrlContent {
    private Map<String,String> headerParams;
    private String url;
    private String urlMethod;

    @Override
    public String toString() {
        return "UrlContent{" +
                "headerParams=" + headerParams +
                ", url='" + url + '\'' +
                ", urlMethod='" + urlMethod + '\'' +
                '}';
    }

    public Map<String, String> getHeaderParams() {
        return headerParams;
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.headerParams = headerParams;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlMethod() {
        return urlMethod;
    }

    public void setUrlMethod(String urlMethod) {
        this.urlMethod = urlMethod;
    }
}
