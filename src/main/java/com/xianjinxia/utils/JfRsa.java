package com.xianjinxia.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by wangwei on 2018/1/25.
 * 玖富提供的rsa加密解密工具类
 */
public class JfRsa {

    public static PublicKey getPublicKey(String public_key) throws Exception{
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(public_key);
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);

            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (IOException e) {
            throw new Exception("公钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    public static String encrypt(PublicKey publicKey, byte[] mobilebyte) throws Exception {
        String base64String = null;
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(mobilebyte);
          //  base64String = new BASE64Encoder().encodeBuffer(output);
            base64String = org.apache.commons.codec.binary.Base64.encodeBase64String(output);

        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
        return base64String;//为最终加密的手机号
    }

    public static PrivateKey getPrivateKey(String private_key) throws Exception{
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(private_key);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);

            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (IOException e) {
            throw new Exception("私钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    public static String decrypt(PrivateKey privateKey, String  mobile) throws Exception{

        byte[] buffer= new BASE64Decoder().decodeBuffer(mobile);

        String base64String=null;
        if(privateKey== null){
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output= cipher.doFinal(buffer);
            base64String = new String(output);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }catch (InvalidKeyException e) {
            throw new Exception("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
        return base64String; //最后获得的手机号
    }


    public static Map<String,String> collectEncrypt(String publicKey, Map<String,String> dataMap) throws Exception{
        PublicKey pk = getPublicKey(publicKey);
        for(Map.Entry<String, String >  entry : dataMap.entrySet()){
            String encryptedValue = encrypt(pk,entry.getValue().getBytes("UTF-8"));
            entry.setValue(encryptedValue);
        }
        return dataMap;
    }




    public static void main(String[] args) {

//        String PUBLICK_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNWKMQKEVy5z4xrDlPPPHAPVZzANfnC5+fG69xDwVyKg4L+F7WJeIuURsLynAVCHLtdaHBK7iYE56/1uZ6ppkwVmusDc6PgRi0HqaUtUH+aR43nbDBebvLhVDcnSDlnlQXBt/+MkequZ4GeuuQfi8gkb4yZ7Gi85AmAoRsJmdOrQIDAQAB";
//        String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI1YoxAoRXLnPjGsOU888cA9VnMA1+cLn58br3EPBXIqDgv4XtYl4i5RGwvKcBUIcu11ocEruJgTnr/W5nqmmTBWa6wNzo+BGLQeppS1Qf5pHjedsMF5u8uFUNydIOWeVBcG3/4yR6q5ngZ665B+LyCRvjJnsaLzkCYChGwmZ06tAgMBAAECgYANM48fTjOWVMS5TPWZUf7eTpxJZ3XCzZIeAS70p0uB6y2erpbk92rVmpulEUZAoKZjSU5PRW8bPRHhpwelwvOhrOGbzcklZsmrMLCO1PVXCLUcxxwQ7oljTroVxHP3SJvTcc/h6bMetLQ6fbUiSxtGHHNmV6vr//DiOz8GlprMnQJBAOhWx6mCa586p1ew7DaKuhhdY6UJz9VkbrvEEGvSP9xE1CGPYH840Zk69VhAz2KpoR9QxuidU6zBtaCGIrMjeLsCQQCbvZ7IDESsqlDESx+AlzsHWOs3Oei1WZXpvMo/CVDKgNnZ13cynhNkxU0yPa3mjK6tSwzDiGy50z3QJRsVcHO3AkBD7cNYewouNr7gc5jMZYj9QKi2gwTh1ghOG4fVjlYfd3wyB7Jl393gGA3NbL11kjTKbBqLvhvNwaDxuuYATBmrAkAxS6velkOGg8Jc34pxIW1Fvhd1u5a1EE4rVd6OZVx1PKO4GENaej6ZPvpYVL2epBkGMSMxIRWJHBwMrjIMbzOxAkA3Ge5ViFeRj+gHO2HuVnOjBYnPlGrtu+Wfbgb2RPk84m3Q+fGpKBbGcrLK56Mjt2BRbZy1Jf0KPxzqEmsU4Zgw";
           String PUBLICK_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCuTUWRfuElv4J2h3V9JaoRrZH8Pu5/ot7ezS2dRxDOMxQ5IEhUGXEzrjcrUhv8Sv5+76JS8RVfkzzSsp7kEc4QfDVs8sNZfortbu2IT8uFDTL9elN/1pdYS9XbezuMGU5erBOD6ck1SGPZNDjqTnfsSudhG10V0zbNFmvL68QblwIDAQAB";
        String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK5NRZF+4SW/gnaHdX0lqhGtkfw+7n+i3t7NLZ1HEM4zFDkgSFQZcTOuNytSG/xK/n7volLxFV+TPNKynuQRzhB8NWzyw1l+iu1u7YhPy4UNMv16U3/Wl1hL1dt7O4wZTl6sE4PpyTVIY9k0OOpOd+xK52EbXRXTNs0Wa8vrxBuXAgMBAAECgYBtX4eb+OYG7dlLz0xmL1A8r5G/my3FYcpAPLe2zXU6Lsst3ZgFTcG/ED8GLAvrkJOJy40sRqOGybmM7RDPRGzs1/O9XiUBvokG1TaDrVE4v3DQrUXt0faciTQe1zXqPHrTvikR8bG666n2f3UmXmCzSqbDL90cxtqEn09GA7sloQJBAPBUh1f+1rOsQxA6yUsEe+Ti1+hVNnxMjaNceJrxNViOKAulza22BsBei+4Tz6e2DT9t7+rLT7DjWbxX5Yu3IEUCQQC5qqGD8bSlTYm8kGDSZglHIUI+ECVMOIYu9OQ2ZBOoF8lO5mDI1p7SsPN5hDY+bkujmjlyTeqpvPs4r+RdTfArAkEAjFUPYraPsAKadlOdToyju5cp86QuWI5q9kT8t5y5RahcgdCjNRfhAVsz4iT3UDrhqXQMb9GgCu32bB/3DYo08QJAUKIIDzNzjq7D+L6DLhzePqP1DtsYN6f8rnvweKax2rip1rg6pD6BL2Nl+govPKHN+7lI5ZltJlZKjG3nMZZdjQJASzOZiko+IM0nJy6YgD5iquaKm5dGApSwhxPVyGNJqdODgA1VClynLOdKB7dVjui5shIusgPdNptPUIU1ZePC9Q==";
                //phoneNo=13774236428&requestSourceIp=116.62.157.252&realName=%E7%8E%8B%E4%BC%9F&IdCardNo=36220319880409041X

            //    phoneNo=13774236428&requestSourceIp=116.62.157.252&realName=%E7%8E%8B%E4%BC%9F&IdCardNo=36220319880409041X

        List<String> contents = Arrays.asList("谭胜利");
//        List<String> contents = Arrays.asList("13774236428","221.133.231.66","王伟","36220319880409041X");
        PublicKey pk = null;
        try {
            pk = getPublicKey(PUBLICK_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(String c : contents){
            // 加解密测试
            String encrypt = null;
            try {

                encrypt = encrypt(pk,c.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("加密前原文:" + c );
            System.out.println("加密后密文: " + encrypt);
            try {
                PrivateKey  rk = getPrivateKey(PRIVATE_KEY);
                 System.out.println("解密后原文: " + decrypt(rk,encrypt));
             } catch (Exception e) {
                    e.printStackTrace();
            }
            System.out.println();
        }

    }



}
