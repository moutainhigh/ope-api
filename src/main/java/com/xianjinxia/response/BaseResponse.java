package com.xianjinxia.response;

public class BaseResponse<T> {

	private String code;
	private String msg;
	private T data;

	public BaseResponse() {
		this.code = ResponseCode.SUCCESS.code;
		this.msg = ResponseCode.SUCCESS.description;
	}


	public enum ResponseCode{

		SUCCESS("200","success"),

		SIGN_INVALID("300","验签失败"),

		URL_INVALID("304","服务路径不存在"),

		ENCRYPT_INVALID("305","签名配置缺失"),

		REJECT("400","拒绝不需要重推"),

		SYS_ERROR_NEED_RETRY("500","服务异常，需要重推送"),
		
		NO_SUCCESS("-200","服务异常码非成功状态");

		private String code;
		private String description;

		ResponseCode(String code, String description) {
			this.code = code;
			this.description = description;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}

	public static BaseResponse reject(){
		return new BaseResponse(ResponseCode.REJECT);
	}

	public static BaseResponse sysError(){
		return new BaseResponse(ResponseCode.SYS_ERROR_NEED_RETRY);
	}

	public boolean success(){
		return this.getCode().equals(ResponseCode.SUCCESS.getCode());
	}

	public BaseResponse(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public BaseResponse(String code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}


	public static <T> BaseResponse<T> ok(T data){
		return new BaseResponse<>(data);
	}

	public BaseResponse(T data) {
		this.code = ResponseCode.SUCCESS.code;
		this.msg = ResponseCode.SUCCESS.description;
		this.data =data;
	}

	public BaseResponse(ResponseCode code){
		this.code =code.getCode();
		this.msg =code.getDescription();
	}

	@Override
	public String toString() {
		return "BaseResponse{" +
				"code='" + code + '\'' +
				", msg='" + msg + '\'' +
				", data=" + data +
				'}';
	}
}
