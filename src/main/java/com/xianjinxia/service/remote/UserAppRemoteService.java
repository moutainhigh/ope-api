package com.xianjinxia.service.remote;
import com.alibaba.fastjson.JSONObject;
import com.xianjinxia.conf.ExtProperties;
import com.xianjinxia.request.FaceCertificationRequest;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.response.UserIdCardHeadsResponse;
import com.xianjinxia.utils.FaceHttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.lang3.RandomStringUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenfei
 * @date 2017/12/21
 */
@Service
public class UserAppRemoteService {
    private static final Logger log  = LoggerFactory.getLogger(UserAppRemoteService.class);
    private final static String CODE="00";
    private static final String GET_TOKEN = "https://www.juxinli.com/orgApi/rest/v3/applications/lqkjxx";
    private static final String COMMIT_URL = "https://www.juxinli.com/orgApi/rest/v2/messages/collect/req";
    private static final String FACE_USERIDCARD_HEADS="https://api.faceid.com/faceid/v1/ocridcard";
    private static final String FACE_API_FACE_URL = "https://api.megvii.com/faceid/v2/verify";
    @Autowired
    private RestTemplate restTemplateDefault;
    @Autowired
    private ExtProperties extProperties;
    public String commitJxlPasswordRequestToJxl(String userJxlTokenReq){
        return restTemplateDefault.postForObject(GET_TOKEN, JSONObject.parseObject(userJxlTokenReq), String.class);
    }

    public String commitJxlCaptchaRequestToJxl(String userJxlReq){
        return restTemplateDefault.postForObject(COMMIT_URL, JSONObject.parseObject(userJxlReq), String.class);
    }
    public BaseResponse<String> scannedIdCard(String path){
        BaseResponse<String> baseResponse=new BaseResponse <>();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("api_key", extProperties.getFace().getApiKey());
        params.put("api_secret",extProperties.getFace().getApiSecret());
        // 返回身份证照片合法性检查结果，值只取“0”或“1”。“1”：返回； “0”：不返回。默认“0”。
        params.put("legality", "1");
        Map<String, String> fileMap = new HashMap<String, String>();
        /*图片地址*/
        fileMap.put("image",path);
        String contentType = "";//image/png
        String ret = FaceHttpUtil.formUploadImage(FACE_USERIDCARD_HEADS, params, fileMap, contentType);
        Map<String, Object> checkResult=null;
        try {
            checkResult = JSONObject.parseObject(ret,Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkResult.containsKey("error")) {

        }
        boolean isIdCardImageFront = "front".equals(checkResult.get("side"));
        // 判断该图片是否为身份证件正面 如果是正面那么将识别出的姓名和身份证号信息保存至user-app系统
        if (isIdCardImageFront) {
            UserIdCardHeadsResponse response=new UserIdCardHeadsResponse();
            response.setAncestralHomeAddress(checkResult.get("address")+"");
            response.setIdNumber(checkResult.get("id_card_number") + "" );
            response.setRealname(checkResult.get("name") + "");
            response.setGender(checkResult.get("gender") + "");
            baseResponse.setData(JSONObject.toJSONString(response));
            baseResponse.setCode(CODE);
        }
        return  baseResponse;
    }
    /**
     * 人脸识别
     *
     * @param
     * @return
     */
    public String faceCertification(FaceCertificationRequest req) {
        Map<String, String> textMap = new HashMap<String, String>();
        //可以设置多个input的name，value
        textMap.put("api_key", extProperties.getFace().getApiKey());
        textMap.put("api_secret", extProperties.getFace().getApiSecret());
        // 确定本次比对为“有源比对”或“无源比对”。取值只为“1”或“0”
        textMap.put("comparison_type", "1");
        // 确定待比对图片的类型。取值只为“meglive”、“facetoken”、“raw_image”三者之一
        textMap.put("face_image_type", "raw_image");
        textMap.put("idcard_name", req.getIdcardName());
        textMap.put("idcard_number", req.getIdcardNumber());
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("image", req.getFaceImageAttach());
        log.info("faceCertification request params" + textMap.toString() + " imgae url：" + fileMap.toString());
        String ret = FaceHttpUtil.formUploadImageNew(FACE_API_FACE_URL, textMap, fileMap, "");
        log.info("faceCertification return info :" + ret);
        return ret;
    }


    public static String getQuickPathname(String extension) {
        StringBuilder filename = new StringBuilder();
        DateFormat df = new SimpleDateFormat("/yyyyMMdd/yyyyMMddHHmmss_");
        filename.append(df.format(new Date()));
        filename.append( RandomStringUtils.random(10, '0', 'Z', true, true)
                .toLowerCase());
        if (StringUtils.isNotBlank(extension)) {
            filename.append(".").append(extension.toLowerCase());
        }
        return "/user_files"+filename.toString();
    }



}
