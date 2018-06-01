package com.xianjinxia.service.remote;

import com.alibaba.fastjson.JSONObject;
import com.xianjinxia.conf.HttpTemplate;
import com.xianjinxia.conf.MyRestTemplate;
import com.xianjinxia.exception.ServiceException;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.response.HttpResponse;
import com.xianjinxia.response.JFResponse;
import com.xianjinxia.utils.RiskRequest;
import com.xianjinxia.utils.XRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;

import java.util.Map;

/**
 * Created by wangwei on 2017/11/25.
 */
@Service
public class RemoteService {
    private static final Logger logger  = LoggerFactory.getLogger(RemoteService.class);

    @Autowired
    private MyRestTemplate myRestTemplate;

    @Autowired
    private HttpTemplate httpTemplate;


    //内部转发到外部
    public Object doInner2OuterService(UrlContent urlContent, XRequest xRequest){
        try {

            logger.info("doInner2OuterService params:{}",xRequest);
            BaseResponse resp = myRestTemplate.absoluteUrlCall(urlContent, xRequest, new ParameterizedTypeReference<BaseResponse>() {});
            logger.info("doInner2OuterService response:{}",resp);
            if(resp == null){
                logger.warn("inner2outer: response is null!");
                throw new ServiceException(BaseResponse.ResponseCode.REJECT);
            }
            if(!BaseResponse.ResponseCode.SUCCESS.getCode().equals(resp.getCode())) {
                logger.warn("inner2outer: response code not success,code:{},msg:{}",resp.getCode(),resp.getMsg());
                throw new ServiceException(BaseResponse.ResponseCode.REJECT.getCode(), resp.getMsg());
            }
            return resp.getData();
        }catch (Throwable e) {
            if(e instanceof  ServiceException) {
                throw e;
            }else {
                logger.error("inner2outer failed!",e);
                throw new ServiceException(BaseResponse.ResponseCode.SYS_ERROR_NEED_RETRY);
            }
        }

    }

    //外部转发到内部
    public Object doOuter2InnerService(UrlContent urlContent, XRequest xRequest){
        try {
            logger.info("doOuter2InnerService params:{}",xRequest);
            BaseResponse resp = myRestTemplate.call(urlContent, JSONObject.parse(xRequest.getBiz_data()), new ParameterizedTypeReference<BaseResponse>() {});
            logger.info("doOuter2InnerService response:{}",resp);
            if(resp == null){
                logger.warn("outer2inner: response is null!");
                throw new ServiceException(BaseResponse.ResponseCode.REJECT);
            }
            if(!BaseResponse.ResponseCode.SUCCESS.getCode().equals(resp.getCode())) {
                logger.warn("outer2inner:response code not success,code:{},msg:{}",resp.getCode(),resp.getMsg());
                throw new ServiceException(BaseResponse.ResponseCode.REJECT.getCode(), resp.getMsg());
            }
            return resp.getData();
        }catch (Throwable e) {
            if(e instanceof  ServiceException) {
                throw e;
            }else {
                logger.error("outer2inner failed!",e);
                throw new ServiceException(BaseResponse.ResponseCode.SYS_ERROR_NEED_RETRY);
            }
        }

    }


    //内部转发到外部
    public Object doInner2OuterServiceForJF(UrlContent urlContent, Map<String,String> params){
        try {

            logger.info("doInner2OuterServiceForJF params:{}",params);
            JFResponse resp = myRestTemplate.httpPostForm(urlContent.getUrl(), params, JFResponse.class );
            logger.info("doInner2OuterServiceForJF response:{}",resp);
            if(resp == null){
                logger.warn("doInner2OuterServiceForJF: response is null!");
                throw new ServiceException(BaseResponse.ResponseCode.REJECT);
            }
            if(!"1".equals(resp.getStatus())) {
                logger.warn("doInner2OuterServiceForJF: status != 1");
                throw new ServiceException(resp.getStatus(),resp.getMessage());
            }
            return resp.getData();
        }catch (Throwable e) {
            if(e instanceof  ServiceException) {
                throw e;
            }else {
                logger.error("inner2outer failed!",e);
                throw new ServiceException(BaseResponse.ResponseCode.SYS_ERROR_NEED_RETRY);
            }
        }

    }


    //内部转发到外部
    public Object doInner2OuterService(UrlContent urlContent, XRequest xRequest, HttpHeaders headers){
        try {

            logger.info("doInner2OuterService params:{}",xRequest);
            BaseResponse resp = myRestTemplate.absoluteUrlCall(urlContent, xRequest, new ParameterizedTypeReference<BaseResponse>() {}, headers);
            logger.info("doInner2OuterService response:{}",resp);
            if(resp == null){
                logger.warn("inner2outer: response is null!");
                throw new ServiceException(BaseResponse.ResponseCode.REJECT);
            }
            if(!BaseResponse.ResponseCode.SUCCESS.getCode().equals(resp.getCode())) {
                logger.warn("inner2outer: response code not success,code:{},msg:{}",resp.getCode(),resp.getMsg());
                throw new ServiceException(BaseResponse.ResponseCode.REJECT.getCode(), resp.getMsg());
            }
            return resp.getData();
        }catch (Throwable e) {
            if(e instanceof  ServiceException) {
                throw e;
            }else {
                logger.error("inner2outer failed!",e);
                throw new ServiceException(BaseResponse.ResponseCode.SYS_ERROR_NEED_RETRY);
            }
        }

    }



    //风控外部转发到内部
    public Object riskOuter2InnerService(UrlContent urlContent, RiskRequest riskRequest){
        try {
            logger.info("riskOuter2InnerService params:{}",riskRequest);
            BaseResponse resp = myRestTemplate.call(urlContent, riskRequest.getBiz_data(), new ParameterizedTypeReference<BaseResponse>() {});
            logger.info("riskOuter2InnerService response:{}",resp);
            if(resp == null){
                logger.warn("riskouter2inner: response is null!");
                throw new ServiceException(BaseResponse.ResponseCode.REJECT);
            }
            if(!BaseResponse.ResponseCode.SUCCESS.getCode().equals(resp.getCode())) {
                logger.warn("riskouter2inner:response code not success,code:{},msg:{}",resp.getCode(),resp.getMsg());
                throw new ServiceException(BaseResponse.ResponseCode.REJECT.getCode(), resp.getMsg());
            }
            return resp.getData();
        }catch (Throwable e) {
            if(e instanceof  ServiceException) {
                throw e;
            }else {
                logger.error("riskouter2inner failed!",e);
                throw new ServiceException(BaseResponse.ResponseCode.SYS_ERROR_NEED_RETRY);
            }
        }

    }

    //风控内部转发到外部
    public Object riskInner2OuterService(UrlContent urlContent, RiskRequest xRequest, HttpHeaders headers) {
        logger.info("doInner2OuterService params:{}", xRequest);
        HttpResponse resp = httpTemplate.doService(urlContent, xRequest
                .getBiz_data().toJSONString(), headers);
        logger.info("doInner2OuterService response:{}", resp);
        String xTuId = resp.getRspHeaders().get("X-TUID");

        if (resp.getStatusCode() == HttpStatus.CREATED.value() || resp.getStatusCode() == HttpStatus.OK.value()) {
            return xTuId;
        }
        logger.warn("inner2outer: response code not success");
        throw new ServiceException(BaseResponse.ResponseCode.NO_SUCCESS.getCode(), resp.getMsg() + ",X-TUID=" + xTuId);
    }




}
