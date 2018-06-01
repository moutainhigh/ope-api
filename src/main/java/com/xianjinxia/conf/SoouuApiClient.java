package com.xianjinxia.conf;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SoouuApiClient {
    private static final Logger logger = LoggerFactory.getLogger(SoouuApiClient.class);
    public String post(String url, Map<String, String> header, Object body){
        HttpResponse<String> res ;
        if (header == null){
            header = new HashMap<>(1);
        }

        if (body == null){
            body = new Object();
        }

        try {
            res = Unirest.post(url)
                    .headers(header)
                    .header("content-type", "application/json")
                    .header("cache-control", "no-cache")
                    .body(JSONObject.toJSON(body).toString())
                    .asString();
            return res.getBody();
        } catch (UnirestException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
