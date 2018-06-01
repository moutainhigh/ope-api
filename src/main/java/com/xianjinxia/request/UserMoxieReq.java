package com.xianjinxia.request;

/**
 * @author chenfei
 * @date 2017/12/25
 */
public class UserMoxieReq extends BaseInnerRequest{
    /** 任务ID */
    private String user_id;

    /** 第三方的用户的ID, 用于标示第三方用户的唯一ID */
    private String task_id;

    /** 事件类型，“task.submit  tast  bill report” */
    private String type;

    /** 用户账号的映射ID 邮箱使用email_id */
    private String mapping_id;

    /** 用户账号 邮箱使用email  */
    private String account;

    /** 登录结果 true:成功,false:失败 */
    private Boolean result;

    /** 失败原因 */
    private String message;

    /** 邮箱 */
    private String email;

    /** 邮箱的唯一标识符 */
    private String email_id;

    /** UNIX timestamp(毫秒)时间戳 */
    private Long timestamp;

    /** 邮箱的账单数量 */
    private Integer bill_count;
    
    private String area_code;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getMapping_id() {
        return mapping_id;
    }

    public void setMapping_id(String mapping_id) {
        this.mapping_id = mapping_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getBill_count() {
        return bill_count;
    }

    public void setBill_count(Integer bill_count) {
        this.bill_count = bill_count;
    }

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
    
    
}
