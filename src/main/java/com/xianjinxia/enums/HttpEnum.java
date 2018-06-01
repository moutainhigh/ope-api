package com.xianjinxia.enums;

public enum HttpEnum {

	SUCCESS("00","success"),

	ERROR("05","service error");
	
	private String code;
	private String description;

	HttpEnum(String code, String description) {
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
