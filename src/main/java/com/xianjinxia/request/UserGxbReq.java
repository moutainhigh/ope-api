package com.xianjinxia.request;

/**
 * @author linsy
 */
public class UserGxbReq extends BaseInnerRequest{
 
	/**
	 * 是	外部系统用户授权唯一性标志，由接入方token授权时指定	898954878787878754
	 * */
	private  String sequenceNo;	
	
	/**
	 * 是	爬虫系统一次授权唯一标识	0000000000000NPDlsuFGRzqJubN3Qaw
	 * */
	private  String token;	
	
	/**
	 * 是	授权状态1/0	1
	 * */
	private  String authStatus;		
	
	/**
	 * 	是	身份证	3303948394838
	 * */
	private  String idcard;	
	
	/**
	 * 	是	姓名	张三
	 * */
	private  String name;	
	
	/**
	 * 是	手机	13748594383
	 * */
	private  String phone;	
	
	/**
	 * 是	授权结果内容JsonString。具体格式参看各授权项的字段说明	{"ecommerceBaseInfo":{},"ecommerceBindedBankCards":[]}
	 * */
	private  String authJson;		
	
	/**
	 * 	是	授权项	ecommerce
	 * */
	private  String authItem;

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAuthJson() {
		return authJson;
	}

	public void setAuthJson(String authJson) {
		this.authJson = authJson;
	}

	public String getAuthItem() {
		return authItem;
	}

	public void setAuthItem(String authItem) {
		this.authItem = authItem;
	}	
	
	
}
