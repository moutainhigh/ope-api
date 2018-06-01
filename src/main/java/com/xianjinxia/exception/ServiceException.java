package com.xianjinxia.exception;

import com.xianjinxia.response.BaseResponse;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String code;

	private String msg;
	
	public ServiceException(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ServiceException{" +
				"code='" + code + '\'' +
				", msg='" + msg + '\'' +
				'}';
	}

	public ServiceException(BaseResponse.ResponseCode bp){
		this.code = bp.getCode();
		this.msg = bp.getDescription();
	}

	public ServiceException(String code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}
}
