package com.xianjinxia.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.internal.LinkedTreeMap;
import com.xianjinxia.conf.ExtProperties;
import com.xianjinxia.enums.ApiMethodEnum;
import com.xianjinxia.utils.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xianjinxia.exception.ServiceException;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.service.OuterProcessService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by chunliny on 2018/01/05.
 */
@RestController
@RequestMapping("/")
public class RiskOuterController {
	 private static final Logger logger  = LoggerFactory.getLogger(RiskOuterController.class);

	    @Autowired
	    private OuterProcessService outerProcessService;

	    @Autowired
	    private ExtProperties extProperties;

	    @PostMapping(value="risk-gateway",produces="application/json;charset=UTF-8")
	    public ResponseEntity riskGateway(@RequestBody String requestStr,@RequestHeader("Authorization") String authorization,@RequestHeader HttpHeaders headers) {
	    	logger.info("外部请求----->params:{}",requestStr);
			Map<String,String> map = new HashMap<String,String>();
			String authorizationKey = "Basic "+Base64Utils.getBase64(String.format("%s:%s", extProperties.getAuthorization().getUserName(), extProperties.getAuthorization().getPassword()));
	        if(!authorizationKey.equals(authorization)){
	        	return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}
			RiskRequest riskRequest = null;
	        try{
				riskRequest = RiskRequest.parse(requestStr);
	            Object result  = outerProcessService.processRisk(riskRequest, headers);
	            if(result instanceof LinkedTreeMap){
					Object obj = ((LinkedTreeMap)result).get("user_info");
					if(obj == null){
						return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
					}
				}
	            return new ResponseEntity<>(result,HttpStatus.OK);
	        }catch (ServiceException se){
	            logger.error("业务异常",se);
				map.put("message",se.getMsg());
				return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
	        }catch (Throwable t){
	            logger.error("处理异常",t);
				map.put("message",t.toString());
	            return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }




	/**
	 *
	 * 风控结果通知（授信，机审，人工审核）
	 * @param groupRiskRequest
	 * @param authorization
	 * @return
	 *
	 */
	@PostMapping(value="risk-callback",produces="application/json;charset=UTF-8")
	public ResponseEntity riskCallback(HttpServletRequest request,@RequestBody GroupRiskRequest groupRiskRequest, @RequestHeader("Authorization") String authorization) {
		String ip = IpUtils.getIpAddr(request);
		logger.info("request source ip={},risk-callback外部请求----->params:{}",ip,groupRiskRequest);
		Map<String,String> map = new HashMap<String,String>();
		String authorizationKey = "Basic "+Base64Utils.getBase64(String.format("%s:%s", extProperties.getAuthorization().getUserName(), extProperties.getAuthorization().getPassword()));
		if(!authorizationKey.equals(authorization)){
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(groupRiskRequest));
		RiskRequest riskRequest = new RiskRequest(ApiMethodEnum.RISK_CALL_BACK.getAppId(),ApiMethodEnum.RISK_CALL_BACK.getMethod(),jsonObject);
		try{
			Object result  = outerProcessService.processRisk(riskRequest, null);
			return new ResponseEntity<>(result,HttpStatus.OK);
		}catch (ServiceException se){
			logger.error("业务异常",se);
			map.put("message",se.getMsg());
			return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Throwable t){
			logger.error("处理异常",t);
			map.put("message",t.toString());
			return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	/**
	 * 查询用户自有数据
	 * @param
	 * @param authorization
	 * @return
	 */
	@PostMapping(value="user-data",produces="application/json;charset=UTF-8")
	public ResponseEntity userData(@RequestBody UserInfoRequest userInfoRequest, @RequestHeader("Authorization") String authorization) {
		logger.info("user-data外部请求----->params:{}",userInfoRequest);
		Map<String,Object> map = new HashMap<String,Object>();
		String authorizationKey = "Basic "+Base64Utils.getBase64(String.format("%s:%s", extProperties.getAuthorization().getUserName(), extProperties.getAuthorization().getPassword()));
		if(!authorizationKey.equals(authorization)){
			map.put("code",String.valueOf(HttpStatus.UNAUTHORIZED.value()));
			map.put("message",HttpStatus.UNAUTHORIZED.getReasonPhrase());
			return new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
		}
		if(StringUtils.isBlank(userInfoRequest.getUser_id()) && StringUtils.isBlank(userInfoRequest.getOrder_id())){
			map.put("code",HttpStatus.BAD_REQUEST.value());
			if(StringUtils.isBlank(userInfoRequest.getUser_id())){
				map.put("message","user_id is null or param name error,please check");
			}else{
				map.put("message","order_id is null or param name error,please check");
			}
			return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
		}
		JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(userInfoRequest));
		RiskRequest riskRequest = new RiskRequest(ApiMethodEnum.RISK_GET_USER_DATA.getAppId(),ApiMethodEnum.RISK_GET_USER_DATA.getMethod(),jsonObject);
		try{
			Object result  = outerProcessService.processRisk(riskRequest, null);
			if(result instanceof LinkedTreeMap){
				Object obj = ((LinkedTreeMap)result).get("user_info");
				if(obj == null){
					logger.info("user_info is null,param is {}",JSON.toJSONString(userInfoRequest));
					map.put("code",String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
					map.put("message",HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
					return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			map.put("code",HttpStatus.OK.value());
			map.put("message","success");
			map.put("body",result);
			return new ResponseEntity<>(map,HttpStatus.OK);
		}catch (ServiceException se){
			logger.error("user-data查询业务异常",se);
			map.put("code",HttpStatus.INTERNAL_SERVER_ERROR.value());
			map.put("message",HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
		}catch (Throwable t){
			logger.error("user-data查询处理异常",t);
			map.put("code",HttpStatus.INTERNAL_SERVER_ERROR.value());
			map.put("message",HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
			return new ResponseEntity<>(map,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}
