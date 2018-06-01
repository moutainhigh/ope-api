package com.xianjinxia.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;

/**
 * 外部基类
 * @author wangwei
 *
 */
public class XRequest  {

	private  String sign = "";//API请求的签名
	private  String sign_type = "RSA";//支持RSA
	private  String biz_enc = "0";//biz_data加密方式（0不加密，1加密:采用DES加密算法）
	private  String des_key;//RSA加密后的密钥（biz_enc为1时为必传）
	private  String version = "1.0";//API协议版本，默认值：1.0
	private  String format = "json";//响应格式。仅支持json
	private  String timestamp = String.valueOf(System.currentTimeMillis()/1000*1000);//10位时间戳，精确到秒
	private  String biz_data;
	private  String method;
	private  String app_id;


	public static XRequest build(String app_id, String method, String biz_data){
		XRequest xRequest = new XRequest();
		xRequest.app_id = app_id;
		xRequest.method = method;
		xRequest.biz_data = biz_data;
		return  xRequest;
	}

	public static XRequest build(String app_id, String method, String biz_data,String sign){
		XRequest xRequest = build(app_id, method, biz_data);
		if(sign != null){
			xRequest.setSign(sign);
		}
		return xRequest;
	}

	public XRequest() {
	}

	public static XRequest parse(String requestStr){
		return JSONObject.parseObject(requestStr,XRequest.class);
	}

	/**
	 * 请求参数检查
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean validate(){
		Preconditions.checkNotNull(method,"method can not be null!");
		Preconditions.checkNotNull(biz_data,"biz_data can not be null!");
		Preconditions.checkNotNull(sign,"sign can not be null!");
		Preconditions.checkNotNull(sign_type,"sign_type can not be null!");
		Preconditions.checkNotNull(version,"version can not be null!");
		Preconditions.checkNotNull(format,"format can not be null!");
		Preconditions.checkNotNull(timestamp,"timestamp can not be null!");
		Preconditions.checkNotNull(app_id,"appId can not be null!");
		return true;
	}
	
	/**
	 * 请求参数检查
	 * @return
	 */
	public boolean simpleValidate(){
		Preconditions.checkNotNull(method,"method can not be null!");
		Preconditions.checkNotNull(biz_data,"biz_data can not be null!");
		Preconditions.checkNotNull(app_id,"appId can not be null!");
		return true;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getBiz_enc() {
		return biz_enc;
	}

	public void setBiz_enc(String biz_enc) {
		this.biz_enc = biz_enc;
	}

	public String getDes_key() {
		return des_key;
	}

	public void setDes_key(String des_key) {
		this.des_key = des_key;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getBiz_data() {
		return biz_data;
	}

	public void setBiz_data(String biz_data) {
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
		return "XRequest{" +
				"sign='" + sign + '\'' +
				", sign_type='" + sign_type + '\'' +
				", biz_enc='" + biz_enc + '\'' +
				", des_key='" + des_key + '\'' +
				", version='" + version + '\'' +
				", format='" + format + '\'' +
				", timestamp='" + timestamp + '\'' +
				", biz_data='" + biz_data + '\'' +
				", method='" + method + '\'' +
				", app_id='" + app_id + '\'' +
				'}';
	}
}
