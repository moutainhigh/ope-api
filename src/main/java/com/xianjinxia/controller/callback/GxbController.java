package com.xianjinxia.controller.callback;

import com.google.common.base.Strings;
import com.xianjinxia.request.UserGxbReq;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.service.remote.RemoteToUserAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author linsy
 */
@RestController
@RequestMapping("/service")
public class GxbController {
    private static final Logger logger  = LoggerFactory.getLogger(GxbController.class);
    private static final String authItem ="ecommerce";
    @Autowired
    private RemoteToUserAppService remoteToUserAppService;

    @PostMapping("/userAlipay/callback")
    public void alipayCallback(@RequestBody UserGxbReq userGxbReq, ServletRequest request, ServletResponse response) {
    	logger.info("request body:" + userGxbReq);
        if(checkParam(userGxbReq)){
        	writeMessage(response, HttpServletResponse.SC_OK,"{\"retCode\": 2,\"retMsg\": \"参数验证错误\"}");
            return;
        }
        BaseResponse result = remoteToUserAppService.callbackAlipayRequestToUserApp(userGxbReq);
        logger.info("内部请求alipay----->result:{}",result);
        if(result.success()){
            writeMessage(response, HttpServletResponse.SC_OK, "{\"retCode\": 1,\"retMsg\": \"success\"}");
        }else{
            writeMessage(response, HttpServletResponse.SC_OK,"{\"retCode\": 2,\"retMsg\": \"fail\"}");
        }
    }

    private boolean checkParam(UserGxbReq userGxbReq){
        if (Strings.isNullOrEmpty(userGxbReq.getAuthItem()) && authItem.equals(userGxbReq.getAuthItem())) {
            return true;
        }
        if (Strings.isNullOrEmpty(userGxbReq.getPhone())) {
            return true;
        }
        if (Strings.isNullOrEmpty(userGxbReq.getSequenceNo())) {
            return true;
        }
        if (Strings.isNullOrEmpty(userGxbReq.getToken())) {
            return true;
        }
        return false;
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
}
