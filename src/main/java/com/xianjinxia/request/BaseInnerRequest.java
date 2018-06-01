package com.xianjinxia.request;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;

/**
 * 内部请求基类
 * @author wangwei
 *
 */
public class BaseInnerRequest{
	protected  JSONObject biz_data;
	protected  String method;
	protected  String app_id;

	public JSONObject getBiz_data() {
		return biz_data;
	}

	public void setBiz_data(JSONObject biz_data) {
		this.biz_data = biz_data;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	@Override
	public String toString() {
		return "BaseInnerRequest{" +
				"biz_data=" + biz_data +
				", method='" + method + '\'' +
				", app_id='" + app_id + '\'' +
				'}';
	}

	/**
	 * 请求参数检查
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean validate(){
		Preconditions.checkNotNull(method,"method can not be null!");
		Preconditions.checkNotNull(biz_data,"biz_data can not be null!");
		Preconditions.checkNotNull(app_id,"appId can not be null!");
		return true;
	}

}
