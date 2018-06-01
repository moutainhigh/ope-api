package com.xianjinxia.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 *
 * Created by wangwei on 2017/11/25.
 *
 */
public class XRequestParamsUtils {

    public static final String SIGN = "sign";   //API请求的签名
    public static final String SIGN_TYPE = "sign_type"; //支持RSA
    public static final String BIZ_ENC = "biz_enc"; //biz_data加密方式（0不加密，1加密:采用DES加密算法）
    public static final String DES_KEY = "des_key";  //RSA加密后的密钥（biz_enc为1时为必传）
    public static final String VERSION = "version";  //API协议版本，默认值：1.0
    public static final String FORMAT = "format";    //响应格式。仅支持json
    public static final String TIMESTAMP = "timestamp";  //10位时间戳，精确到秒
    public static final String BIZ_DATA = "biz_data";
    public static final String METHOD = "method";
    public static final String APP_ID = "app_id";



    public static Map<String,String> transferRequestParams(String str){
        JSONObject jsonObject = JSONObject.parseObject(str);
        Map<String,String> params = new HashMap<String,String>();
        params.put(SIGN,jsonObject.getString(SIGN));
        params.put(BIZ_DATA,jsonObject.getString(BIZ_DATA));
        return params;
    }
}
