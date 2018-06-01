package com.xianjinxia.request;

/**
 * Created by ZZC on 2017/12/23.
 */
public class FaceCertificationRequest {
    /* 身份证姓名 */
    private String idcardName;
    /* 身份证号码 */
    private String idcardNumber;
    /* 人脸图片地址 */
    private String faceImageAttach;

    public String getIdcardName() {
        return idcardName;
    }

    public void setIdcardName(String idcardName) {
        this.idcardName = idcardName;
    }

    public String getIdcardNumber() {
        return idcardNumber;
    }

    public void setIdcardNumber(String idcardNumber) {
        this.idcardNumber = idcardNumber;
    }

    public String getFaceImageAttach() {
        return faceImageAttach;
    }

    public void setFaceImageAttach(String faceImageAttach) {
        this.faceImageAttach = faceImageAttach;
    }

    @Override
    public String toString() {
        return "FaceCertificationRequest{" +
                ", idcardName='" + idcardName + '\'' +
                ", idcardNumber='" + idcardNumber + '\'' +
                ", faceImageAttach='" + faceImageAttach + '\'' +
                '}';
    }
}
