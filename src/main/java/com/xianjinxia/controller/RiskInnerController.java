package com.xianjinxia.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xianjinxia.conf.ExtProperties;
import com.xianjinxia.exception.ServiceException;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.service.InnerProcessService;
import com.xianjinxia.utils.Base64Utils;
import com.xianjinxia.utils.RiskRequest;
/**
 * Created by chunliny on 2018/01/05.
 */
@RestController
@RequestMapping("/")
public class RiskInnerController {
	
    private static final String XJX = "XJX";

	private static final String X_CLIENT_CHANNEL = "X_CLIENT_CHANNEL";

	private static final Logger logger  = LoggerFactory.getLogger(RiskInnerController.class);
    
    @Autowired
    private InnerProcessService innerProcessService;
    
    @Autowired
    private ExtProperties extProperties;
	
    @PostMapping("loan-inner-gateway")
    BaseResponse service(@RequestBody RiskRequest riskRequest, @RequestHeader HttpHeaders headers) {
        logger.info("内部请求----->params:{}",riskRequest);
        extProperties.getRiskAddress().getUserName();
        try{
        	setHeaders(headers);
            Object result  = innerProcessService.processRisk(riskRequest, headers);
            BaseResponse<Void> baseResponse = BaseResponse.ok(null);
            baseResponse.setMsg(String.format("%s,X-TUID=%s", baseResponse.getMsg(), String.valueOf(result)));
            return baseResponse;
        }catch (NullPointerException npe){
            logger.error("内部请求参数检查错误",npe);
            return BaseResponse.reject();
        }catch (ServiceException se){
            logger.warn("业务异常",se);
            return new BaseResponse(se.getCode(),se.getMsg());
        }catch (Throwable t){
            logger.error("对外调用异常",t);
            return BaseResponse.sysError();
        }

    }

	private void setHeaders(HttpHeaders headers) {
		headers.clear();
		headers.add(X_CLIENT_CHANNEL, "XJX");
		String authorizationKey = Base64Utils.getBase64(String.format("%s:%s", extProperties.getRiskAddress().getUserName(), extProperties.getRiskAddress().getPassword()));
		//headers.add("Authorization", "Basic eGp4X2FwcGxpY2F0aW9uX2NsaWVudF90ZXN0OnhqeF90ZXN0");
		headers.add("Authorization", String.format("Basic %s", authorizationKey));
	}
}
