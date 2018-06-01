package com.xianjinxia.dto;

public class MerchantEncryptConfigDto extends BaseDto{
    private Long id;

    private String merchantCode;

    private String signType;

    private String signAbled;

    private String encryptType;

    private String encryptAbled;

    private String publicKey;

    private String privateKey;

    private String enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode == null ? null : merchantCode.trim();
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType == null ? null : signType.trim();
    }

    public String getSignAbled() {
        return signAbled;
    }

    public void setSignAbled(String signAbled) {
        this.signAbled = signAbled == null ? null : signAbled.trim();
    }

    public String getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(String encryptType) {
        this.encryptType = encryptType == null ? null : encryptType.trim();
    }

    public String getEncryptAbled() {
        return encryptAbled;
    }

    public void setEncryptAbled(String encryptAbled) {
        this.encryptAbled = encryptAbled == null ? null : encryptAbled.trim();
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey == null ? null : publicKey.trim();
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey == null ? null : privateKey.trim();
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled == null ? null : enabled.trim();
    }
}