package com.xianjinxia.controller.callback;

import com.xianjinxia.controller.BaseController;
import com.xianjinxia.exception.ServiceException;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.service.OuterProcessService;
import com.xianjinxia.utils.XRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangwei on 2017/11/25.
 */
@RestController
@RequestMapping("/")
public class OuterController extends BaseController {

    private static final Logger logger  = LoggerFactory.getLogger(OuterController.class);

    @Autowired
    private OuterProcessService outerProcessService;

    @PostMapping("gateway")
    BaseResponse service(@RequestBody String requestStr) {
        logger.info("外部请求----->params:{}",requestStr);
        XRequest xRequest = null;
        try{
            xRequest = XRequest.parse(requestStr);
            xRequest.validate();
            Object result  = outerProcessService.process(xRequest);
            return BaseResponse.ok(result);
        }catch (NullPointerException npe){
            logger.error("外部请求参数检查错误",npe);
            return BaseResponse.reject();
        }catch (ServiceException se){
            logger.warn("业务异常",se);
            return new BaseResponse(se.getCode(),se.getMsg());
        }catch (Throwable t){
            logger.error("处理异常",t);
            return BaseResponse.sysError();
        }
    }

}
