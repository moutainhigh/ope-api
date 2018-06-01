package com.xianjinxia.bizdata;

import com.xianjinxia.exception.ServiceException;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.utils.XRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created wangwei on 2017/11/25.
 */
public abstract class DataLoader<T> {

    protected static final Logger logger  = LoggerFactory.getLogger(DataLoader.class);


    protected volatile boolean isInit = false;

    public T load(XRequest xRequest){
        if(!isInit){
            initData(xRequest);
        }
        T data = getData(xRequest);
        if(data == null){
            throw new ServiceException(BaseResponse.ResponseCode.URL_INVALID);
        }
        return data;
    }

    public abstract T getData(XRequest xRequest);

    public abstract void fetchData(XRequest xRequest);

    public synchronized boolean initData(XRequest xRequest){
        if(!isInit){
            fetchData(xRequest);
            isInit = true;
        }
        return true;
    }

}
