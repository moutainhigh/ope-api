package com.xianjinxia.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

	@Autowired
	private HttpServletRequest request;
	/**
	 * 获取用户id
	 * @return
	 */
	protected Long getUserId() {
		Object userId = request.getAttribute("userId");
		if(userId == null) {
			return null;
			//return 768093299L;
		}
		return Long.valueOf(String.valueOf(userId));
	}

	protected String getMobile() {
		Object mobile = request.getParameter("mobilePhone");
		if(mobile == null) {
			return null;
		}
		return String.valueOf(mobile);
	}

	/**
	 * 获取http header的数据
	 * @return
	 */
	protected String getHeaderValue(String key) {
		return request.getHeader(key);
	}


}
