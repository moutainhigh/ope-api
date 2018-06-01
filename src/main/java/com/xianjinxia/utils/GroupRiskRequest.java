package com.xianjinxia.utils;

public class GroupRiskRequest {

    private String product_code;

    private String phase;

    private String trace_uid;

    private String project_code;

    private String user_id;

    private String order_id;

    private int result;

    private String head_code;

    private int intervals_in_day;

    private int credit_line;

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getTrace_uid() {
        return trace_uid;
    }

    public void setTrace_uid(String trace_uid) {
        this.trace_uid = trace_uid;
    }

    public String getProject_code() {
        return project_code;
    }

    public void setProject_code(String project_code) {
        this.project_code = project_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getHead_code() {
        return head_code;
    }

    public void setHead_code(String head_code) {
        this.head_code = head_code;
    }

    public int getIntervals_in_day() {
        return intervals_in_day;
    }

    public void setIntervals_in_day(int intervals_in_day) {
        this.intervals_in_day = intervals_in_day;
    }

    public int getCredit_line() {
        return credit_line;
    }

    public void setCredit_line(int credit_line) {
        this.credit_line = credit_line;
    }

    @Override
    public String toString() {
        return "RiskRequest{" +
                "product_code='" + product_code + '\'' +
                ", phase='" + phase + '\'' +
                ", trace_uid='" + trace_uid + '\'' +
                ", project_code='" + project_code + '\'' +
                ", user_id='" + user_id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", result=" + result +
                ", head_code='" + head_code + '\'' +
                ", intervals_in_day=" + intervals_in_day +
                ", credit_line=" + credit_line +
                '}';
    }
}
