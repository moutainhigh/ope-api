package com.xianjinxia.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class GateWayInterceptor implements HandlerInterceptor{
	private static Logger logger = LoggerFactory.getLogger(GateWayInterceptor.class);
	@Autowired
	private EurekaInstanceConfigBean eurekaInstanceConfigBean;

	private static List<String> whites = new ArrayList<>();

	static {
		whites.add("/");
		whites.add("/gateway");
		whites.add("/service/userJxl/getJxlToken");
		whites.add("/service/userJxl/authMobile");
		whites.add("/service/user/scannedIdCard");
		whites.add("/service/userJd/callback");
		whites.add("/service/userAlipay/callback");
		whites.add("/service/userEmail/callback");
		whites.add("/service/userGjj/callback");
		whites.add("/risk-gateway");
		whites.add("/loan-inner-gateway");
		whites.add("/user-data");
		whites.add("/risk-callback");
		whites.add("/service/virtualCardRecovery/getCardList");
	}


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (whites.contains(request.getServletPath())) {
			return true;
		} else if (eurekaInstanceConfigBean.getIpAddress().equals(request.getServerName())) {
			return true;
		}
		logger.info("拦截外部非法请求:" + request.getServletPath());
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
