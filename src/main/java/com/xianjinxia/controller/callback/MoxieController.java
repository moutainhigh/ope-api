package com.xianjinxia.controller.callback;

import com.google.common.base.Strings;
import com.xianjinxia.request.BaseInnerRequest;
import com.xianjinxia.request.UserMoxieReq;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.service.remote.RemoteToUserAppService;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;

/**
 * @author chenfei
 * @date 2017/12/25
 */
@RestController
@RequestMapping("/service")
public class MoxieController {
    private static final Logger logger  = LoggerFactory.getLogger(MoxieController.class);
    private static final String HEADER_MOXIE_EVENT = "X-Moxie-Event";
    private static final String HEADER_MOXIE_TYPE = "X-Moxie-Type";
    private static final String HEADER_MOXIE_SIGNATURE = "X-Moxie-Signature";

    @Autowired
    private RemoteToUserAppService remoteToUserAppService;

    @PostMapping("/userJd/callback")
    public void jdCallback(@RequestBody UserMoxieReq userMoxieReq, ServletRequest request, ServletResponse response) {
        String type = checkParam(userMoxieReq, request);
        if(StringUtils.isEmpty(type)){
            writeMessage(response, HttpServletResponse.SC_BAD_REQUEST, "param check exception");
            return;
        }
        userMoxieReq.setType(type);
        logger.info("内部请求jd----->params:{}",userMoxieReq);
        BaseResponse result = remoteToUserAppService.callbackJdRequestToUserApp(userMoxieReq);
        logger.info("内部请求jd----->result:{}",result);
        if(result.success()){
            writeMessage(response, HttpServletResponse.SC_CREATED, "success");
        }else{
            writeMessage(response, HttpServletResponse.SC_BAD_REQUEST, result.getMsg());
        }
    }

/*    @PostMapping("/userAlipay/callback")
    public void alipayCallback(@RequestBody UserMoxieReq userMoxieReq, ServletRequest request, ServletResponse response) {
        String type = checkParam(userMoxieReq, request);
        if(StringUtils.isEmpty(type)){
            writeMessage(response, HttpServletResponse.SC_BAD_REQUEST, "param check exception");
            return;
        }
        userMoxieReq.setType(type);
        logger.info("内部请求alipay----->params:{}",userMoxieReq);
        BaseResponse result = remoteToUserAppService.callbackAlipayRequestToUserApp(userMoxieReq);
        logger.info("内部请求alipay----->result:{}",result);
        if(result.success()){
            writeMessage(response, HttpServletResponse.SC_CREATED, "success");
        }else{
            writeMessage(response, HttpServletResponse.SC_BAD_REQUEST, result.getMsg());
        }
    }*/

    @PostMapping("/userEmail/callback")
    public void emailCallback(@RequestBody UserMoxieReq userMoxieReq, ServletRequest request, ServletResponse response) {
        String type = checkParam(userMoxieReq, request);
        if(StringUtils.isEmpty(type)){
            writeMessage(response, HttpServletResponse.SC_BAD_REQUEST, "param check exception");
            return;
        }
        userMoxieReq.setType(type);
        logger.info("内部请求email----->params:{}",userMoxieReq);
        BaseResponse result = remoteToUserAppService.callbackEmailRequestToUserApp(userMoxieReq);
        logger.info("内部请求email----->result:{}",result);
        if(result.success()){
            writeMessage(response, HttpServletResponse.SC_CREATED, "success");
        }else{
            writeMessage(response, HttpServletResponse.SC_BAD_REQUEST, result.getMsg());
        }
    }
    
    
    @PostMapping("/userGjj/callback")
    public void gjjCallback(@RequestBody UserMoxieReq userMoxieReq, ServletRequest request, ServletResponse response) {
        String type = checkParam(userMoxieReq, request);
        if(StringUtils.isEmpty(type)){
            writeMessage(response, HttpServletResponse.SC_BAD_REQUEST, "param check exception");
            return;
        }
        userMoxieReq.setType(type);
        logger.info("内部请求gjj----->params:{}",userMoxieReq);
        BaseResponse result = remoteToUserAppService.callbackGjjRequestToUserApp(userMoxieReq);
        logger.info("内部请求gjj----->result:{}",result);
        if(result.success()){
            writeMessage(response, HttpServletResponse.SC_CREATED, "success");
        }else{
            writeMessage(response, HttpServletResponse.SC_BAD_REQUEST, result.getMsg());
        }
    }


    private String checkParam(BaseInnerRequest req, ServletRequest request){
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //事件类型：task.submit task bill
        String eventName = httpServletRequest.getHeader(HEADER_MOXIE_EVENT);
        //业务类型：email、jingdong、alipay 等
        String eventType = httpServletRequest.getHeader(HEADER_MOXIE_TYPE);
        //body签名
        String signature = httpServletRequest.getHeader(HEADER_MOXIE_SIGNATURE);
        logger.info("request body:" + req);
        if (Strings.isNullOrEmpty(eventName)) {
            return null;
        }
        if (Strings.isNullOrEmpty(eventType)) {
            return null;
        }
        if (Strings.isNullOrEmpty(signature)) {
            return null;
        }
        if (req == null) {
            return null;
        }
        return eventName.toLowerCase();
    }

    private void writeMessage(ServletResponse response, int status, String content) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(status);
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.write(content);
        } catch (IOException ignored) {
        }
    }
    
    /**
     * 是否验签
     * */
    public static String base64Hmac256(String payload, String secret) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
            sha256Hmac.init(secretKey);
            return Base64.encodeBase64String(sha256Hmac.doFinal(payload.getBytes()));
        } catch (Exception ignored) {
            return "";
        }
    }
}
