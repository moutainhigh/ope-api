package com.xianjinxia.service;

import com.xianjinxia.bizdata.EncryptData;
import com.xianjinxia.bizdata.UrlData;
import com.xianjinxia.bizdata.UrlData.UrlDataWrap;
import com.xianjinxia.exception.ServiceException;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.service.remote.UrlContent;
import com.xianjinxia.utils.RSAEncrypt;
import com.xianjinxia.utils.RiskRequest;
import com.xianjinxia.utils.SignUtils;
import com.xianjinxia.utils.XRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class OuterProcessService  extends ProcessService{

    private static final Logger logger  = LoggerFactory.getLogger(OuterProcessService.class);

    public Object signEncryptAndDispatch(XRequest xRequest, UrlData.UrlDataWrap  urlDataWrap, EncryptData encryptData){
        String sign = xRequest.getSign();
        // 解签
        if(RSAEncrypt.verify(SignUtils.CreateLinkString_X2(xRequest),sign,encryptData.getPublic_key())){
            // 发送给内部服务系统
            UrlContent urlContent = new UrlContent();
            urlContent.setUrl(urlDataWrap.getUrl());
            urlContent.setUrlMethod(urlDataWrap.getUrl_method());
            return remoteService.doOuter2InnerService(urlContent, xRequest);
        }else{
            logger.warn("验签失败, params: {}",xRequest);
            throw new ServiceException(BaseResponse.ResponseCode.SIGN_INVALID);
        }
    }

	@Override
	public Object dispatchRisk(RiskRequest riskRequest, UrlContent urlContent,
                               HttpHeaders headers) {
		 // 发送给内部服务系统
        return remoteService.riskOuter2InnerService(urlContent,riskRequest);
	}

}
