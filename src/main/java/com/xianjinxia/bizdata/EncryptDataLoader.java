package com.xianjinxia.bizdata;

import com.google.common.collect.Maps;
import com.xianjinxia.dto.MerchantEncryptConfigDto;
import com.xianjinxia.service.remote.LoanarkAppRemoteService;
import com.xianjinxia.utils.XRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created wangwei on 2017/11/25.
 */
@Component
public class EncryptDataLoader extends DataLoader<EncryptData> {


    Map<String,EncryptData> encryptDataMap = Maps.newConcurrentMap();

    @Autowired
    private LoanarkAppRemoteService loanarkAppRemoteService;

    public EncryptData getData(XRequest xRequest){
        return encryptDataMap.get(xRequest.getApp_id());
    }

    public void fetchData(XRequest xRequest){
        encryptDataMap.clear();

        List<EncryptData> encryptDataList = new ArrayList<>();
        List<MerchantEncryptConfigDto>  merchantEncryptConfigDtoList = loanarkAppRemoteService.getEncryptConfig();
        for(MerchantEncryptConfigDto merchantEncryptConfigDto : merchantEncryptConfigDtoList){
            encryptDataList.add(new EncryptData(merchantEncryptConfigDto.getMerchantCode(), merchantEncryptConfigDto.getSignType(),merchantEncryptConfigDto.getPublicKey(),merchantEncryptConfigDto.getPrivateKey()));
        }
        logger.info("加密解密数据从远程加载完成,dataSize:{},data:{}",encryptDataList.size(),encryptDataList);

        for(EncryptData encryptData : encryptDataList){
            encryptDataMap.put(encryptData.getApp_id(),encryptData);
        }
    }

}
