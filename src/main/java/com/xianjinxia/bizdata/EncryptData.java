package com.xianjinxia.bizdata;

/**
 * Created wangwei on 2017/11/25.
 */
public class EncryptData {

    public String app_id;
    public String sign_type; //目前支持RSA
    public String public_key;
    public String private_key;
    public String biz_enc = "0"; // 加密方式（0不加密，1加密:采用DES加密算法）
    public String des_key; //RSA加密后的密钥（biz_enc为1时为必传）

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

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public EncryptData(String app_id, String sign_type, String public_key, String private_key) {
        this.app_id = app_id;
        this.sign_type = sign_type;
        this.public_key = public_key;
        this.private_key = private_key;
    }

    @Override
    public String toString() {
        return "EncryptData{" +
                "app_id='" + app_id + '\'' +
                ", sign_type='" + sign_type + '\'' +
                ", public_key='" + public_key + '\'' +
                ", private_key='" + private_key + '\'' +
                ", biz_enc='" + biz_enc + '\'' +
                ", des_key='" + des_key + '\'' +
                '}';
    }
}
