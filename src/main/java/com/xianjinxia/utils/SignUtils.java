package com.xianjinxia.utils;

import com.alibaba.fastjson.JSONObject;
import com.site.lookup.util.StringUtils;
import java.util.*;
/**
 *
 * Created by wangwei on 2017/11/25.
 *
 */
public class SignUtils {


    /**
     * 把数组所有元素，按字母排序，然后按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要签名的参数
     * @return 签名的字符串
     */
    public static String CreateLinkString(Map<String, Object> params){
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuilder prestr = new StringBuilder();
        String key="";
        String value="";
        for (int i = 0; i < keys.size(); i++) {
            key=(String) keys.get(i);
            value = (String) params.get(key);
            if("".equals(value) || value == null ||
                    key.equalsIgnoreCase("sign") ){
                continue;
            }
            prestr.append(key).append("=").append(value).append("&");
        }
        if(StringUtils.isEmpty(prestr.toString())){
            return prestr.toString();
        }
        return prestr.deleteCharAt(prestr.length()-1).toString();
    }

    public static String CreateLinkString(String jsonStr){
        Map<String, Object> params = JSONObject.parseObject(jsonStr);
        return CreateLinkString(params);
    }

    public static String CreateLinkString_X1(XRequest xRequest){
        Map<String, Object> m = new HashMap<String, Object>();
        m.put(XRequestParamsUtils.BIZ_DATA,xRequest.getBiz_data());
        return CreateLinkString(m);
    }

    public static String CreateLinkString_X2(XRequest xRequest){
        Map<String, Object> m = new HashMap<String, Object>();
        m.put(XRequestParamsUtils.BIZ_DATA,xRequest.getBiz_data());
        m.put(XRequestParamsUtils.METHOD,xRequest.getMethod());
        m.put(XRequestParamsUtils.APP_ID,xRequest.getApp_id());
        m.put(XRequestParamsUtils.TIMESTAMP,xRequest.getTimestamp());
        m.put(XRequestParamsUtils.SIGN_TYPE,xRequest.getSign_type());
        m.put(XRequestParamsUtils.BIZ_ENC,xRequest.getBiz_enc());
        m.put(XRequestParamsUtils.VERSION,xRequest.getVersion());
        m.put(XRequestParamsUtils.FORMAT,xRequest.getFormat());
        return CreateLinkString(m);
    }

}
