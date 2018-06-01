package com.xianjinxia.service.remote;

import com.alibaba.fastjson.JSON;
import com.xianjinxia.dto.MerchantEncryptConfigDto;
import com.xianjinxia.dto.MerchantRemoteCallConfigDto;
import com.xianjinxia.exception.ServiceException;
import com.xianjinxia.response.LoanarkAppBaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanarkAppRemoteService extends BaseRemoteService {
	
	private static final Logger logger = LoggerFactory.getLogger(LoanarkAppRemoteService.class);

	private static final String GET_REMOTE_CALL_CONFIG_URL = "/service/custom/strategy/remote-call-config";
	private static final String GET_ENCRYPT_CONFIG_URL = "/service/custom/strategy/encrypt-config";

	@Override
	protected String getServiceName() {
		return "LOANARK-APP";
	}


    //获取远程调用配置
    public List<MerchantRemoteCallConfigDto> getRemoteCallConfig(){
        String url = super.buildUrl(GET_REMOTE_CALL_CONFIG_URL);
        try {
            logger.info("查询远程调用配置，url:{}",url);
            LoanarkAppBaseResponse<List<MerchantRemoteCallConfigDto>> resp = myRestTemplate.httpGet(url, null, new ParameterizedTypeReference<LoanarkAppBaseResponse<List<MerchantRemoteCallConfigDto>>>() {
            });
            logger.info("查询远程调用配置，url:{},resp:{}",url,resp);
            if(resp == null){
                logger.error("返回的远程调用配置为空");
                throw new ServiceException("返回的远程调用配置为空");
            }
            if(!LoanarkAppBaseResponse.ResponseCode.SUCCESS.getCode().equals(resp.getCode())) {
                logger.warn("url:{},resp RetCode:{},RetMsg:{}",url,resp.getCode(),resp.getMsg());
                throw new ServiceException(resp.getCode(), resp.getMsg());
            }
            if(resp.getData() == null){
                logger.error("返回的远程调用配置为空");
                throw new ServiceException("返回的远程调用配置为空");
            }
            return resp.getData();
        }catch (ServiceException e) {
            throw e;
        }catch (Throwable e) {
            logger.error("获取远程调用配置失败",e);
            throw new ServiceException("获取远程调用配置失败");
        }

    }

    //获取加密配置
    public List<MerchantEncryptConfigDto> getEncryptConfig(){
        String url = super.buildUrl(GET_ENCRYPT_CONFIG_URL);
        try {
            logger.info("查询加密配置，url:{}",url);
            LoanarkAppBaseResponse<List<MerchantEncryptConfigDto>> resp = myRestTemplate.httpGet(url, null, new ParameterizedTypeReference<LoanarkAppBaseResponse<List<MerchantEncryptConfigDto>>>() {
            });
            logger.info("查询加密配置，url:{},resp:{}",url,resp);
            if(resp == null){
                logger.error("返回的加密配置为空");
                throw new ServiceException("返回的远程调用配置为空");
            }
            if(!LoanarkAppBaseResponse.ResponseCode.SUCCESS.getCode().equals(resp.getCode())) {
                logger.warn("url:{},resp RetCode:{},RetMsg:{}",url,resp.getCode(),resp.getMsg());
                throw new ServiceException(resp.getCode(), resp.getMsg());
            }
            if(resp.getData() == null){
                logger.error("返回的加密配置为空");
                throw new ServiceException("返回的加密配置为空");
            }
            return resp.getData();
        }catch (ServiceException e) {
            throw e;
        }catch (Throwable e) {
            logger.error("获取加密配置失败",e);
            throw new ServiceException("获取加密配置失败");
        }

    }
    
}
