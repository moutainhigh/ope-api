package com.xianjinxia.service.remote;

import com.alibaba.fastjson.JSONObject;
import com.xianjinxia.conf.ExtProperties;
import com.xianjinxia.conf.SoouuApiClient;
import com.xianjinxia.request.SoouuOrderApplyRequest;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.utils.Md5Utils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenfei
 * @date 2017/12/21
 */
@Service
public class SoouuRemoteService {
    private static final Logger logger = LoggerFactory.getLogger(SoouuRemoteService.class);
    @Autowired
    private SoouuApiClient soouuApiClient;
    @Autowired
    private ExtProperties extProperties;


    public BaseResponse cardorderAdd(SoouuOrderApplyRequest soouuOrderApplyRequest) {
        logger.info("-------------------start call soouu api:kamenwang.order.cardorder.add---------------------");
        logger.info("call soouu request:" + soouuOrderApplyRequest.toString());
        StringBuffer url = new StringBuffer();
        url.append(extProperties.getSoouu().getUrl());
        url.append("?");
        StringBuffer parames = new StringBuffer();
        parames.append("buynum=");
        parames.append(soouuOrderApplyRequest.getBuyNum());
        parames.append("&customerid=");
        parames.append(extProperties.getSoouu().getCustomerId());
        parames.append("&customerorderno=");
        parames.append(soouuOrderApplyRequest.getCustomerOrderNo());
        parames.append("&format=json&method=kamenwang.order.cardorder.add&productid=");
        parames.append(soouuOrderApplyRequest.getProductId());
        parames.append("&timestamp=");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        parames.append(format.format(new Date()));
        parames.append("&v=1.0");
        String sign = Md5Utils.md5(parames.toString() + extProperties.getSoouu().getSign());
        logger.info("call 树鱼 api 参数:{}, 未加密sign：{}, 加密sign：{}", parames.toString(), parames.toString() + extProperties.getSoouu().getSign(), sign);
        parames.append("&sign=");
        parames.append(sign);

        url.append(parames);
        logger.info("最终请求树鱼url：{}", url.toString());
        String response = soouuApiClient.post(url.toString().replace(" ", "+"), null, null);
        logger.info("call soouu api:kamenwang.order.cardorder.add result:{}", response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        BaseResponse result;
        if (StringUtils.isEmpty(jsonObject.getString("MessageCode"))) {
//            SoouuSuccessResponse successResponse = JSON.parseObject(response, SoouuSuccessResponse.class);
            result = new BaseResponse(response);
        } else {
//            SoouuFailResponse failResponse = JSON.parseObject(response, SoouuFailResponse.class);
            result = new BaseResponse(BaseResponse.ResponseCode.NO_SUCCESS.getCode(), BaseResponse.ResponseCode.NO_SUCCESS.getDescription(), response);
        }
        return result;
    }
    public BaseResponse cardorderGet(Long customerOrderNo) {
        logger.info("-------------------start call soouu api:kamenwang.order..cardorder.get---------------------");
        logger.info("call soouu request:" + customerOrderNo);
        StringBuffer url = new StringBuffer();
        url.append(extProperties.getSoouu().getUrl());
        url.append("?");
        StringBuffer parames = new StringBuffer();
        parames.append("customerid=");
        parames.append(extProperties.getSoouu().getCustomerId());
        parames.append("&customerorderno=");
        parames.append(customerOrderNo);
        parames.append("&format=json&method=kamenwang.order.cardorder.get");
        parames.append("&timestamp=");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        parames.append(format.format(new Date()));
        parames.append("&v=1.0");
        String sign = Md5Utils.md5(parames.toString() + extProperties.getSoouu().getSign());
        logger.info("call 树鱼 kamenwang.order..cardorder.get api, 参数:{}, 未加密sign：{}, 加密sign：{}", parames.toString(), parames.toString() + extProperties.getSoouu().getSign(), sign);
        parames.append("&sign=");
        parames.append(sign);

        url.append(parames);
        logger.info("最终请求树鱼url：{}", url.toString());
        String response = soouuApiClient.post(url.toString().replace(" ", "+"), null, null);

        JSONObject jsonObject = JSONObject.parseObject(response);
        logger.info("call soouu api:kamenwang.order.cardorder.get result:{}", response);
        BaseResponse result;
        if (StringUtils.isEmpty(jsonObject.getString("MessageCode"))) {
//            SoouuSuccessResponse successResponse = JSON.parseObject(response, SoouuSuccessResponse.class);
            result = new BaseResponse(response);
        } else {
//            SoouuFailResponse failResponse = JSON.parseObject(response, SoouuFailResponse.class);
            result = new BaseResponse(BaseResponse.ResponseCode.NO_SUCCESS.getCode(), BaseResponse.ResponseCode.NO_SUCCESS.getDescription(), response);
        }
        return result;
    }

    public static void main(String args[]) {
        SoouuApiClient soouuApiClient = new SoouuApiClient();
        StringBuffer url = new StringBuffer();
        url.append("http://test.ccapi.soouu.cn/Interface/Method");
        url.append("?");
        String parBase = "buynum=4&customerid=803683&customerorderno=t555550003&format=json&method=kamenwang.order.cardorder.add&productid=1204406&timestamp=2016-02-18 14:08:26&v=1.0";
        String sign = parBase + "CC11F561EBF14204089A5C64DE61C8DF";
        sign = Md5Utils.md5(sign);
        url.append(parBase);
        url.append("&sign=");
        url.append(sign);
        System.out.println("sign:" + sign);
        System.out.println("url:" + url.toString());
        String response = soouuApiClient.post(url.toString().replace(" ", "+"), null, null);
        JSONObject jsonObject = JSONObject.parseObject(response);
        BaseResponse result;
        if (StringUtils.isEmpty(jsonObject.getString("MessageCode"))) {
//            SoouuSuccessResponse successResponse = JSON.parseObject(response, SoouuSuccessResponse.class);
            result = new BaseResponse(response);
        } else {
//            SoouuFailResponse failResponse = JSON.parseObject(response, SoouuFailResponse.class);
            result = new BaseResponse(BaseResponse.ResponseCode.NO_SUCCESS.getCode(), BaseResponse.ResponseCode.NO_SUCCESS.getDescription(), response);
        }
//        System.out.println(result.toString());




    }


}
