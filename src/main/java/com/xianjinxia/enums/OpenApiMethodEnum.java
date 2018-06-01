package com.xianjinxia.enums;

/**
 * Created by wangwei on 2017/11/24.
 */
public enum OpenApiMethodEnum {
    ORDER_DISPATCH("200020","订单进件"),
    RELOAN_CHECK("200010","可申请&复贷校验"),
    GET_BANKCARD_LIST("200030","银行卡列表查询"),
    GET_BIND_CARD_URL("200040","请求第三方绑卡页面"),
    TRIAL_ORDER("200060","借款试算"),
    GET_CONTRACT("200070","获取合同"),
    ORDER_CONFIRM("200080","订单确认"),
    COMFIRM_REPAYMENT("200090","确认还款"),
    GET_ORDER_STATUS("200110","获取订单状态"),
    JF_UNION_LOGIN("600001","玖富联合登陆");


    private String code;
    private String description;

    OpenApiMethodEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
