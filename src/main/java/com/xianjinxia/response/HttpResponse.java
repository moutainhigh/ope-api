package com.xianjinxia.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

	private String msg;
	private int statusCode;
	private String code;

	private Map<String, String> rspHeaders = new HashMap<String, String>();

	@Override
	public String toString() {
		return "HttpResponse [msg=" + msg + ", statusCode=" + statusCode
				+ ", code=" + code + "]";
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public Map<String, String> getRspHeaders() {
		return rspHeaders;
	}

}
