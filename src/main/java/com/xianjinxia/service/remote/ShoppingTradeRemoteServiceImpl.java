package com.xianjinxia.service.remote;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.response.BaseResponse;

/**
 * 调用trade的商城订单的远程接口
 *
 * @author wenkk
 * @date 2018年04月18日
 */
@Service
public class ShoppingTradeRemoteServiceImpl extends BaseRemoteService implements ShoppingTradeRemoteService {

    Logger logger = Logger.getLogger(ShoppingTradeRemoteServiceImpl.class);

    private static final String VIRTUAL_CARD_LIST = "/service/virtualCardRecovery/getCardList";

    @Override
    protected String getServiceName() {
        return "TRADE-APP";
    }
    @Override
	public String getVirtualCardList(String data) {
    	String url = super.buildUrl(VIRTUAL_CARD_LIST);
    	String response = myRestTemplate.httpPost(url, data, new ParameterizedTypeReference<String>() {
		});
		return response;
	}
}
