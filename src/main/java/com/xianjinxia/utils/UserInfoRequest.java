package com.xianjinxia.utils;

public class UserInfoRequest {


    private String user_id;

    private String order_id;

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

    @Override
    public String toString() {
        return "UserInfoRequest{" +
                "user_id='" + user_id + '\'' +
                ", order_id='" + order_id + '\'' +
                '}';
    }
}
