package com.xianjinxia.controller.remote;

import com.xianjinxia.request.SoouuOrderApplyRequest;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.service.remote.SoouuRemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/soouu-api")
public class ShuYuAppController {

    private static final Logger logger = LoggerFactory.getLogger(ShuYuAppController.class);

    @Autowired
    private SoouuRemoteService soouuRemoteService;

    @PostMapping("/order/add")
    public BaseResponse orderApply(@RequestBody SoouuOrderApplyRequest orderApplyRequest) {
        BaseResponse baseResponse;
        logger.info("内部请求树鱼----->params:{}", orderApplyRequest.toString());
        baseResponse = soouuRemoteService.cardorderAdd(orderApplyRequest);
        return baseResponse;
    }

    @PostMapping("/order/get")
    public BaseResponse orderGet(@RequestBody Long customerOrderNo) {
        BaseResponse baseResponse;
        logger.info("内部请求树鱼----->params:{}", customerOrderNo);
        baseResponse = soouuRemoteService.cardorderGet(customerOrderNo);
        return baseResponse;
    }


}
