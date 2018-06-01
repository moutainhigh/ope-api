package com.xianjinxia.controller.remote;
import com.xianjinxia.request.FaceCertificationRequest;
import com.xianjinxia.response.BaseResponse;
import com.xianjinxia.response.UserIdCardHeadsResponse;
import com.xianjinxia.service.remote.UserAppRemoteService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenfei
 * @date 2017/12/21
 */
@RestController
@RequestMapping("/service")
public class UserAppController {

    private static final Logger logger  = LoggerFactory.getLogger(UserAppController.class);

    @Autowired
    private UserAppRemoteService userAppRemoteService;
    @PostMapping("/userJxl/getJxlToken")
    public BaseResponse<String> getJxlToken(@RequestBody String userJxlTokenReq) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        logger.info("内部请求----->params:{}",userJxlTokenReq);
        String result = userAppRemoteService.commitJxlPasswordRequestToJxl(userJxlTokenReq);
        logger.info("内部请求----->result:{}",result);
        baseResponse.setData(result);
        return baseResponse;
    }

    @PostMapping("/userJxl/authMobile")
    public BaseResponse<String> authMobile(@RequestBody String userJxlReq) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        logger.info("内部请求----->params:{}",userJxlReq);
        String result = userAppRemoteService.commitJxlCaptchaRequestToJxl(userJxlReq);
        logger.info("内部请求----->result:{}",result);
        baseResponse.setData(result);
        return baseResponse;
    }

    /**
     * @author xieyaling
     * @date 2017/12/22 19:26
     * @param request
     * @param file
     * @return
     */
    @PostMapping("/user/scannedIdCard")
    public BaseResponse<String> scannedIdCard(HttpServletRequest request, @RequestParam(value = "file") MultipartFile file) {
        BaseResponse<String > baseResponse = new BaseResponse<>();
        try {
            // 获取文件名字
            String originalFilename = file.getOriginalFilename();
            // 获取项目的真实路径
            String realPath = request.getSession().getServletContext().getRealPath("/");
            // 获取文件类型 jpg
            String ext = FilenameUtils.getExtension(originalFilename).toLowerCase();
            String filePath =getQuickPathname(ext);
            String key = filePath;
            File uploadedFile = new File(realPath +key);
            org.apache.commons.io.FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());
            String path = uploadedFile.getPath();
            baseResponse=userAppRemoteService.scannedIdCard(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baseResponse;
    }
    /**
     * zzc
     * 人脸识别
     * @param req
     * @return
     */
    @PostMapping("/user/faceCertification")
    public BaseResponse<String> faceCertification(@RequestBody FaceCertificationRequest req) {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        logger.info("内部请求----->params:{}",req.toString());
        String result = userAppRemoteService.faceCertification(req);
        logger.info("内部请求----->result:{}",result);
        baseResponse.setData(result);
        return baseResponse;
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
        return "user_files"+filename.toString();
    }
}
