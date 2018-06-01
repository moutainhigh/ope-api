package com.xianjinxia.dto;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by wangwei on 2017/12/3.
 */
public abstract  class BaseDto {

    /**
     * for console、logger
     * @return
     */
    public String toString(){
        return JSONObject.toJSONString(this);
    }

}
