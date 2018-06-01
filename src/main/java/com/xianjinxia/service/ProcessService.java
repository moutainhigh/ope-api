package com.xianjinxia.service;

import com.alibaba.fastjson.JSONObject;
import com.xianjinxia.bizdata.EncryptData;
import com.xianjinxia.bizdata.EncryptDataLoader;
import com.xianjinxia.bizdata.UrlData;
import com.xianjinxia.bizdata.UrlDataLoader;
import com.xianjinxia.conf.ExtProperties;
import com.xianjinxia.conf.HttpTemplate;
import com.xianjinxia.conf.MyRestTemplate;
import com.xianjinxia.exception.ServiceException;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.response.HttpResponse;
import com.xianjinxia.service.remote.RemoteService;
import com.xianjinxia.service.remote.UrlContent;
import com.xianjinxia.utils.RiskRequest;
import com.xianjinxia.utils.XRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public abstract class ProcessService {

    private static final Logger logger  = LoggerFactory.getLogger(ProcessService.class);

    @Autowired
    private UrlDataLoader urlDataLoader;
    @Autowired
    private EncryptDataLoader encryptDataLoader;
    @Autowired
    protected RemoteService remoteService;

    @Autowired
    private MyRestTemplate myRestTemplate;

    @Autowired
    private HttpTemplate httpTemplate;

    @Autowired
    private ExtProperties extProperties;

   public Object process(XRequest xRequest) throws Exception{
        String app_id = xRequest.getApp_id();

        // load 真实url
        UrlData.UrlDataWrap  urlDataWrap = urlDataLoader.load(xRequest);
        if(urlDataWrap == null){
            logger.error("路由配置缺失,app_id:{}",app_id);
            throw new ServiceException(BaseResponse.ResponseCode.URL_INVALID);
        }
        // 获取加签、加密方式及私钥
        EncryptData encryptData = encryptDataLoader.load(xRequest);
        if(encryptData == null){
            logger.error("秘钥配置缺失,app_id:{}",app_id);
            throw new ServiceException(BaseResponse.ResponseCode.ENCRYPT_INVALID);
        }
        return signEncryptAndDispatch(xRequest, urlDataWrap, encryptData);
    }
    
    public Object processRisk(RiskRequest riskRequest, HttpHeaders headers) throws Exception{
        JSONObject jsonObject = riskRequest.getBiz_data();
        String phase = jsonObject.getString("phase");
        if(StringUtils.equals(phase,"PRELOAN")){
            //授信通知回调
            HttpResponse httpResponse = httpTemplate.doPost(extProperties.getOldCashmanServerAddressConfig().getServerAddress()+"/refactor/risk/creditBigAmount",jsonObject.toJSONString(),headers);
            if(httpResponse.getStatusCode()!= HttpStatus.OK.value()){
                logger.info("调用cashman返回的http状态码为："+httpResponse.getStatusCode());
                throw new ServiceException(BaseResponse.ResponseCode.SYS_ERROR_NEED_RETRY);
            }
            return null;
        }
        String app_id = riskRequest.getApp_id();
        UrlContent  urlContent = urlDataLoader.getRiskUrlConfig(riskRequest);
        if(urlContent == null){
            logger.error("路由配置缺失,app_id:{}",app_id);
            throw new ServiceException(BaseResponse.ResponseCode.URL_INVALID);
        }
        return dispatchRisk(riskRequest, urlContent, headers);
    }

    public abstract Object signEncryptAndDispatch(XRequest xRequest, UrlData.UrlDataWrap  urlDataWrap, EncryptData encryptData);

    public abstract Object dispatchRisk(RiskRequest riskRequest, UrlContent urlContent, HttpHeaders headers);

}
