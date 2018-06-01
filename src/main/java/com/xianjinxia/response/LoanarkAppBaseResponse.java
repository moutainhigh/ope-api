package com.xianjinxia.response;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class LoanarkAppBaseResponse<T> {

	// 返回状态吗
	@ApiModelProperty(example = "00", value = "返回状态码")
	private String code;
	// 返回说明
	@ApiModelProperty(example = "成功", value = "返回说明")
	private String msg;
	// 自定义返回数据
	@ApiModelProperty( value = "自定义返回数据")
	private T data;

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

	public LoanarkAppBaseResponse(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public LoanarkAppBaseResponse() {
	}


	public enum ResponseCode{

		SUCCESS("00","success");


		@ApiModelProperty(example = "00", value = "返回码数据")
		private String code;
		@ApiModelProperty(example = "成功", value = "相应信息")
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

}
