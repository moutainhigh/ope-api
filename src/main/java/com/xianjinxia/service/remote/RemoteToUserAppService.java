package com.xianjinxia.service.remote;

import com.xianjinxia.request.UserGxbReq;
import com.xianjinxia.request.UserMoxieReq;
import com.xianjinxia.response.BaseResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

/**
 * @author chenfei
 * @date 2017/12/25
 */
@Service
public class RemoteToUserAppService extends BaseRemoteService{

    private static final String JD_REQUEST = "/service/userJd/callback";
    private static final String ALIPAY_REQUEST = "/service/userAlipay/callback";
    private static final String EMAIL_REQURST ="/service/userEmail/callback";
    private static final String GONGJJ_REQURST ="/service/userGjj/callback";

    /**
     * 京东回调 user-app
     * @param userMoxieReq
     * @return
     */
    public BaseResponse callbackJdRequestToUserApp(UserMoxieReq userMoxieReq){
        String url = super.buildUrl(JD_REQUEST);
        return myRestTemplate.httpPost(url, userMoxieReq, new ParameterizedTypeReference<BaseResponse>() {
        });
    }
    /**
     * 支付宝回调 user-app
     * @param userMoxieReq
     * @return
     */
/*    public BaseResponse callbackAlipayRequestToUserApp(UserMoxieReq userMoxieReq){
        String url = super.buildUrl(ALIPAY_REQUEST);
        return myRestTemplate.httpPost(url, userMoxieReq, new ParameterizedTypeReference<BaseResponse>() {
        });
    }*/
    
    /**
     * 支付宝回调 user-app
     * @param userGxbReq
     * @return
     */
    public BaseResponse callbackAlipayRequestToUserApp(UserGxbReq userGxbReq){
        String url = super.buildUrl(ALIPAY_REQUEST);
        return myRestTemplate.httpPost(url, userGxbReq, new ParameterizedTypeReference<BaseResponse>() {
        });
    }
    
    /**
     * 邮箱回调 user-app
     * @param userMoxieReq
     * @return
     */
    public BaseResponse callbackEmailRequestToUserApp(UserMoxieReq userMoxieReq){
        String url = super.buildUrl(EMAIL_REQURST);
        return myRestTemplate.httpPost(url, userMoxieReq, new ParameterizedTypeReference<BaseResponse>() {
        });
    }
    
    /**
     * 公积金回调 user-app
     * @param userMoxieReq
     * @return
     */
    public BaseResponse callbackGjjRequestToUserApp(UserMoxieReq userMoxieReq){
        String url = super.buildUrl(GONGJJ_REQURST);
        return myRestTemplate.httpPost(url, userMoxieReq, new ParameterizedTypeReference<BaseResponse>() {
        });
    }

    @Override
    protected String getServiceName() {
        return "USER-APP";
    }
}
