package com.xianjinxia.bizdata;

import com.google.common.collect.ArrayListMultimap;
import com.xianjinxia.conf.ExtProperties;
import com.xianjinxia.dto.MerchantRemoteCallConfigDto;
import com.xianjinxia.enums.ApiMethodEnum;
import com.xianjinxia.service.remote.LoanarkAppRemoteService;
import com.xianjinxia.service.remote.UrlContent;
import com.xianjinxia.utils.RiskRequest;
import com.xianjinxia.utils.XRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created wangwei on 2017/11/25.
 */
@Component
public class UrlDataLoader extends DataLoader<UrlData.UrlDataWrap>{


    ArrayListMultimap<String,UrlData.UrlDataWrap> urlDataMap = ArrayListMultimap.create();

    @Autowired
    private LoanarkAppRemoteService loanarkAppRemoteService;

    @Autowired
    private ExtProperties extProperties;

    public UrlData.UrlDataWrap getData(XRequest xRequest){
        Collection<UrlData.UrlDataWrap> urlDataDtoList = urlDataMap.get(xRequest.getApp_id());
        if(CollectionUtils.isEmpty(urlDataDtoList)){
            urlDataDtoList = urlDataMap.values();
        }
        for(UrlData.UrlDataWrap urlDataWrap : urlDataDtoList){
            if(xRequest.getMethod().equalsIgnoreCase(urlDataWrap.getMethod())){
                return urlDataWrap;
            }
        }
        return null;
    }

    public void fetchData(XRequest xRequest){
        urlDataMap.clear();

        List<UrlData.UrlDataDto> urlDataDtoList = new ArrayList<>();
        List<MerchantRemoteCallConfigDto> merchantRemoteCallConfigDtoList = loanarkAppRemoteService.getRemoteCallConfig();
        for(MerchantRemoteCallConfigDto data : merchantRemoteCallConfigDtoList){
            urlDataDtoList.add(new  UrlData.UrlDataDto(data.getMethodUrl(),data.getMethodType(),data.getMerchantCode(),data.getMethodCode()));
        }
        logger.info("路由配置从远程加载完成,dataSize:{},data:{}",urlDataDtoList.size(),urlDataDtoList);

        for(UrlData.UrlDataDto urlDataDto : urlDataDtoList){
            urlDataMap.put(urlDataDto.getApp_id(),new UrlData.UrlDataWrap(urlDataDto.getUrl(),urlDataDto.getUrl_method(),urlDataDto.getMethod()));
        }
    }

    public UrlContent getRiskUrlConfig(RiskRequest riskRequest){
        ApiMethodEnum apiMethodEnum = ApiMethodEnum.getByAppIdAndMethod(riskRequest.getApp_id(),riskRequest.getMethod());
        logger.info("api method enum:{}", apiMethodEnum);
        if(apiMethodEnum == null){
            return null;
        }
        UrlContent urlContent = new UrlContent();
        if(StringUtils.equals(riskRequest.getApp_id(),ApiMethodEnum.PUSH_LOAN_ORDER_TO_RISK.getAppId())||StringUtils.equals(riskRequest.getApp_id(),ApiMethodEnum.PUSH_XEJD_TO_RISK.getAppId())){
            urlContent.setUrl(extProperties.getRiskAddress().getPushUrl()+apiMethodEnum.getUrl());
        }else{
            urlContent.setUrl(apiMethodEnum.getUrl());
        }
        urlContent.setUrlMethod(apiMethodEnum.getRequestMethod());
        return urlContent;
    }

}
