package com.xianjinxia.controller.remote;

import com.xianjinxia.controller.BaseController;
import com.xianjinxia.exception.ServiceException;
import com.xianjinxia.request.BaseInnerRequest;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.service.InnerProcessService;
import com.xianjinxia.utils.XRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by wangwei on 2017/11/24.
 */
@RestController
@RequestMapping("/")
public class InnerController extends BaseController {

    private static final Logger logger  = LoggerFactory.getLogger(InnerController.class);

    @Autowired
    private InnerProcessService innerProcessService;

    @PostMapping("inner-gateway")
    BaseResponse service(@RequestBody BaseInnerRequest baseRequest) {
        logger.info("内部请求----->params:{}",baseRequest);
        try{
            XRequest xRequest = XRequest.build(baseRequest.getApp_id(),baseRequest.getMethod(),baseRequest.getBiz_data().toJSONString());
            xRequest.validate();
            Object result  = innerProcessService.process(xRequest);
            return BaseResponse.ok(result);
        }catch (NullPointerException npe){
            logger.error("内部请求参数检查错误",npe);
            return BaseResponse.reject();
        }catch (ServiceException se){
            logger.warn("业务异常",se);
            return new BaseResponse(se.getCode(),se.getMsg());
        }catch (Throwable t){
            logger.error("对外调用异常",t);
            return BaseResponse.sysError();
        }

    }

}
