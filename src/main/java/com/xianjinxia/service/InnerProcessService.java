package com.xianjinxia.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.xianjinxia.bizdata.EncryptData;
import com.xianjinxia.bizdata.UrlData;
import com.xianjinxia.enums.OpenApiMethodEnum;
import com.xianjinxia.service.remote.UrlContent;
import com.xianjinxia.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Service
public class InnerProcessService extends ProcessService{

    private static final Logger logger  = LoggerFactory.getLogger(InnerProcessService.class);

    public Object signEncryptAndDispatch(XRequest xRequest, UrlData.UrlDataWrap  urlDataWrap, EncryptData encryptData){
        if(OpenApiMethodEnum.JF_UNION_LOGIN.getCode().equals(xRequest.getMethod())){ // 玖富万卡对接采用 玖富的通信方式
            return actionForJF(xRequest, urlDataWrap, encryptData);
        }else{
            return action(xRequest, urlDataWrap, encryptData);
        }
    }

    private Object action(XRequest xRequest, UrlData.UrlDataWrap  urlDataWrap, EncryptData encryptData) {
        String sign = RSAEncrypt.sign(SignUtils.CreateLinkString_X2(xRequest), encryptData.getPrivate_key());
        xRequest.setSign(sign);
        // 发送给第三方
        UrlContent urlContent = new UrlContent();
        urlContent.setUrl(urlDataWrap.getUrl());
        urlContent.setUrlMethod(urlDataWrap.getUrl_method());
        return remoteService.doInner2OuterService(urlContent, xRequest);
    }

    private Object actionForJF(XRequest xRequest, UrlData.UrlDataWrap  urlDataWrap, EncryptData encryptData) {
        JSONObject jsonObject = JSONObject.parseObject(xRequest.getBiz_data());
        Map<String, String > paramsData = Maps.newHashMap();
        paramsData.put("phoneNo",jsonObject.getString("phoneNo"));
        paramsData.put("requestSourceIp",jsonObject.getString("requestSourceIp"));
        paramsData.put("realName",jsonObject.getString("realName"));
        paramsData.put("IdCardNo",jsonObject.getString("IdCardNo"));
        logger.info("公钥:" + encryptData.getPublic_key());
        try {
            JfRsa.collectEncrypt(encryptData.getPublic_key(),paramsData);
        } catch (Exception e) {
            logger.error("加密发生异常.",e);
            throw  new RuntimeException(e);
        }
        paramsData.put("parterId",xRequest.getApp_id());
        UrlContent urlContent = new UrlContent();
        urlContent.setUrl(urlDataWrap.getUrl());
        urlContent.setUrlMethod(urlDataWrap.getUrl_method());
        return remoteService.doInner2OuterServiceForJF(urlContent, paramsData);
    }

    @Override
    public Object dispatchRisk(RiskRequest riskRequest, UrlContent urlContent, HttpHeaders headers) {
        // 发送给第三方

        return remoteService.riskInner2OuterService(urlContent,riskRequest,headers);
    }

}
