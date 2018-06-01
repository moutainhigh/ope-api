package com.xianjinxia.enums;

import org.apache.commons.lang.StringUtils;

public enum ApiMethodEnum {

    PUSH_LOAN_ORDER_TO_RISK("CASHMAN-APP-PUSH-RISK","200112", "POST","/api/engine/frontend/v1/engine/task?productCode=DEFQ&phaseCode=LOAN","推送集团风控"),

    PUSH_SHOPPING_ORDER_TO_RISK("CASHMAN-APP-PUSH-RISK","200113", "POST","/api/engine/frontend/v1/engine/task?productCode=FQSC&phaseCode=LOAN","推送集团风控"),

    PUSH_XEJD_TO_RISK("CASHMAN-APP-PUSH-RISK-XEJD","200113", "POST","/api/engine/frontend/v1/engine/task?productCode=XEJD&phaseCode=LOAN","小额借贷推送集团风控"),

    RISK_CALL_BACK("DEFK","20001","POST","CASHMAN-APP/service/loan/risk-callback","风控结果通知"),

    RISK_GET_USER_DATA("DEFK","20002","POST","CASHMAN-APP/service/loan/queryOrderList","获取订单和用户数据");

    private String appId;

    private String method;

    private String requestMethod;

    private String url;

    private String desc;

    ApiMethodEnum(String appId, String method,String requestMethod,String url,String desc) {
        this.appId = appId;
        this.method = method;
        this.requestMethod = requestMethod;
        this.url = url;
        this.desc = desc;
    }


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static ApiMethodEnum getByAppIdAndMethod(String appId, String method){
        ApiMethodEnum[] values = ApiMethodEnum.values();
        for(ApiMethodEnum v:values){
            if(StringUtils.equals(appId,v.getAppId()) && StringUtils.equals(method,v.getMethod())){
                return v;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "ApiMethodEnum{" + "appId='" + appId + '\'' + ", method='" + method + '\'' + ", requestMethod='" + requestMethod + '\'' + ", url='" + url + '\'' + ", desc='" + desc + '\'' + '}';
    }
}
