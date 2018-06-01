package com.xianjinxia.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xianjinxia.conf.ExtProperties;
import com.xianjinxia.service.remote.ShoppingTradeRemoteService;
import com.xianjinxia.utils.DesECB;
import com.xianjinxia.utils.Md5Utils;

/**
 * @service 虚拟卡回收
 * @author wenkk
 * @date 2018/04/17
 */
@RequestMapping("/service/virtualCardRecovery")
@RestController
public class VirtualCardRecoveryController extends BaseController {
    private static Logger logger= LoggerFactory.getLogger(VirtualCardRecoveryController.class);
    @Autowired
    private ShoppingTradeRemoteService shoppingTradeRemoteService;
    @Autowired
	private ExtProperties extProperties;
    @PostMapping("/getCardList")
    @ResponseBody
    public String getCardList(String data,String sign) {
        logger.info("getCardList, request parameter: data:{},sign:{}", data, sign);
        String resultData = null;
        try {
            //验证签名
            if (Md5Utils.md5("data=" + data + extProperties.getSouKaConfig().getSecretKey().toLowerCase(), "utf-8").equals(sign)) {
                String result = shoppingTradeRemoteService.getVirtualCardList(DesECB.decryptData(data, extProperties.getSouKaConfig().getSecretKey().substring(0, 8)));
                resultData = DesECB.encryptDES(result, extProperties.getSouKaConfig().getSecretKey().substring(0, 8));
            }
        } catch (Exception e) {
            logger.error("/service/virtualCardRecovery/getCardList error", e);
        }
        return resultData;
    }
}
