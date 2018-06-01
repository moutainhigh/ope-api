package com.xianjinxia.response;

public class UserIdCardHeadsResponse {
    private String realname;
    private String idNumber;
    private String gender;
    /**祖籍地址*/
    private String ancestralHomeAddress;

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAncestralHomeAddress() {
        return ancestralHomeAddress;
    }

    public void setAncestralHomeAddress(String ancestralHomeAddress) {
        this.ancestralHomeAddress = ancestralHomeAddress;
    }
}
