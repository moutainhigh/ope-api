package com.xianjinxia.utils;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import com.xianjinxia.utils.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;

/**
 *
 * Created by wangwei on 2017/11/25.
 *
 * 参考：http://blog.csdn.net/chaijunkun/article/details/7275632
 *
 */

public class RSAEncrypt {

    private static final String SIGN_ALGORITHMS = "MD5withRSA";
    private static final String DEFAULT_CHARSET = "UTF-8";

    /** RSA最大加密明文大小 */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** RSA最大解密密文大小 */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final String TRANSFORMATION = "RSA";

//    public static final String DEFAULT_PUBLIC_KEY=
//            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChDzcjw/rWgFwnxunbKp7/4e8w" + "\r" +
//                    "/UmXx2jk6qEEn69t6N2R1i/LmcyDT1xr/T2AHGOiXNQ5V8W4iCaaeNawi7aJaRht" + "\r" +
//                    "Vx1uOH/2U378fscEESEG8XDqll0GCfB1/TjKI2aitVSzXOtRs8kYgGU78f7VmDNg" + "\r" +
//                    "XIlk3gdhnzh+uoEQywIDAQAB" + "\r";
//
//    public static final String DEFAULT_PRIVATE_KEY=
//            "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKEPNyPD+taAXCfG" + "\r" +
//                    "6dsqnv/h7zD9SZfHaOTqoQSfr23o3ZHWL8uZzINPXGv9PYAcY6Jc1DlXxbiIJpp4" + "\r" +
//                    "1rCLtolpGG1XHW44f/ZTfvx+xwQRIQbxcOqWXQYJ8HX9OMojZqK1VLNc61GzyRiA" + "\r" +
//                    "ZTvx/tWYM2BciWTeB2GfOH66gRDLAgMBAAECgYBp4qTvoJKynuT3SbDJY/XwaEtm" + "\r" +
//                    "u768SF9P0GlXrtwYuDWjAVue0VhBI9WxMWZTaVafkcP8hxX4QZqPh84td0zjcq3j" + "\r" +
//                    "DLOegAFJkIorGzq5FyK7ydBoU1TLjFV459c8dTZMTu+LgsOTD11/V/Jr4NJxIudo" + "\r" +
//                    "MBQ3c4cHmOoYv4uzkQJBANR+7Fc3e6oZgqTOesqPSPqljbsdF9E4x4eDFuOecCkJ" + "\r" +
//                    "DvVLOOoAzvtHfAiUp+H3fk4hXRpALiNBEHiIdhIuX2UCQQDCCHiPHFd4gC58yyCM" + "\r" +
//                    "6Leqkmoa+6YpfRb3oxykLBXcWx7DtbX+ayKy5OQmnkEG+MW8XB8wAdiUl0/tb6cQ" + "\r" +
//                    "FaRvAkBhvP94Hk0DMDinFVHlWYJ3xy4pongSA8vCyMj+aSGtvjzjFnZXK4gIjBjA" + "\r" +
//                    "2Z9ekDfIOBBawqp2DLdGuX2VXz8BAkByMuIh+KBSv76cnEDwLhfLQJlKgEnvqTvX" + "\r" +
//                    "TB0TUw8avlaBAXW34/5sI+NUB1hmbgyTK/T/IFcEPXpBWLGO+e3pAkAGWLpnH0Zh" + "\r" +
//                    "Fae7oAqkMAd3xCNY6ec180tAe57hZ6kS+SYLKwb4gGzYaCxc22vMtYksXHtUeamo" + "\r" +
//                    "1NMLzI2ZfUoX" + "\r";

    public static final String DEFAULT_PUBLIC_KEY=
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDKd8k97fgh5lCeCcB7L2eRzr81TQ5R2tHtc482WBaYd+Q5VHljVQgHbpqkA2wUmNWgt3cM2iRRlGNVqmCXx8V5/pmtUB1MCb1Jn9e2oukymn11iGxtgS00cq8Rix+4VShxJGbisH/uYrgAF51yaowb+j5YQEaIYvV0dnHCymaEdwIDAQAB";

    public static final String DEFAULT_PRIVATE_KEY="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK6rD8UnZPXIXiYr06HzqLBEqq/cJbcaePtXbaGsHr13BwENSQ/8sS2El011zYk/kFMRP8O021QmuHgOMV2E4xhrAq5eCeolWavoKC9cvxkgaIQbuEZr4a0hvhTxXTBQZGUY7Ybw5dzH+0kPraIsaUh0RPdFhjSFrFBpihSS2O4XAgMBAAECgYAQoMpUi4aJ+3QzEBMKpOaXOUJCkaOphcSksNMIBBm9EbrB9+DPu8+mNKwuvJs/iKSVs9utJueGYFVJYJiEfaUcwlqRUIlyicoCTJHYx1e1suiPk1leOdHEbTuKMMknp3RIc84Emq2kxQ4KLqruRQ8MEtJIVlRqCw+c5Uz8MYeNsQJBAPHzKHyFTpch+4nFZ3McjNnQXNvjYCTBwT6+thiNrivGsEKh78ugn6FEH3xEhOMs8ntDBvPa0Tk1xvTHYN0T7BkCQQC4z7DBW8cKPEfq/031BZkX7hiQQdED9IpP5f7G8ogmV4uOGa2vSuyJCVcXsZ7yxI6vQhDKmgQVnfh+KW0nj/GvAkAvgoTvtqmRCc4/5nCC7RdHah/h2cs2TImqzX4qYh/SXsibvPq+bIMMArmACGBjz56pz6ac2dn9tu6jgEcoAlJ5AkEAsjfejOgTTiSJee2PYMxwMOpzF5HQnQ7R3nC9u16gutDLjFHo8tS+uvud2AR6ckPoEMwV4zqY27vFqOwMDg26kwJBALK/xKNcukKiMsxwZbSiCly/kDziZoZ77YauapcwHMB8ZG7IWKRIqtWZO3Ql/uj0i7p/+p8vZn1yL/5Djy7wihA=";
//    /**
//     * 私钥
//     */
//    private RSAPrivateKey privateKey;
//
//    /**
//     * 公钥
//     */
//    private RSAPublicKey publicKey;

    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

//
//    /**
//     * 获取私钥
//     * @return 当前的私钥对象
//     */
//    public RSAPrivateKey getPrivateKey() {
//        return privateKey;
//    }
//
//    /**
//     * 获取公钥
//     * @return 当前的公钥对象
//     */
//    public RSAPublicKey getPublicKey() {
//        return publicKey;
//    }

    /**
     * 随机生成密钥对
     */
    public static List<String> genKeyPair(){
        KeyPairGenerator keyPairGen= null;
        try {
            keyPairGen= KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair= keyPairGen.generateKeyPair();

        /**
         * 私钥
         */
        RSAPrivateKey privateKey;

        /**
         * 公钥
         */
        RSAPublicKey publicKey;


        privateKey= (RSAPrivateKey) keyPair.getPrivate();
        publicKey= (RSAPublicKey) keyPair.getPublic();


        String pk = new String(Base64.encode(privateKey.getEncoded()));
        String ck = new String(Base64.encode(publicKey.getEncoded()));

        System.out.println("generate privateKey------------------>");
        System.out.println(pk);
        System.out.println("generate publicKey------------------>");
        System.out.println(ck);
        return Arrays.asList(pk,ck);

    }

    /**
     * 从文件中输入流中加载公钥
     * @param in 公钥输入流
     * @throws Exception 加载公钥时产生的异常
     */
    public static void loadPublicKey(InputStream in) throws Exception{
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPublicKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException e) {
            throw new Exception("公钥输入流为空");
        }
    }


    /**
     * 从字符串中加载公钥
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKey(String publicKeyStr) throws Exception{
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(publicKeyStr);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec= new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
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

    /**
     * 从文件中加载私钥
     * @return 是否成功
     * @throws Exception
     */
    public static void  loadPrivateKey(InputStream in) throws Exception{
        try {
            BufferedReader br= new BufferedReader(new InputStreamReader(in));
            String readLine= null;
            StringBuilder sb= new StringBuilder();
            while((readLine= br.readLine())!=null){
                if(readLine.charAt(0)=='-'){
                    continue;
                }else{
                    sb.append(readLine);
                    sb.append('\r');
                }
            }
            loadPrivateKey(sb.toString());
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        }
    }

    public static RSAPrivateKey loadPrivateKey(String privateKeyStr) throws Exception{
        try {
            BASE64Decoder base64Decoder= new BASE64Decoder();
            byte[] buffer= base64Decoder.decodeBuffer(privateKeyStr);
            PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory= KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
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

    /**
     * 加密过程
     * @param publicKey 公钥
     * @param plainTextData 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception{
        if(publicKey== null){
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//            byte[] output= cipher.doFinal(plainTextData);
//            return output;


            int inputlen = plainTextData.length;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            // rsa加密的字节大小最多是117,分段加密
            while (inputlen - offset > 0) {
                if (inputlen - offset > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(plainTextData, offset, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(plainTextData, offset, inputlen - offset);
                }
                bos.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }

            byte[] encryptData = bos.toByteArray();
            bos.close();
            return encryptData;

        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }catch (InvalidKeyException e) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 解密过程
     * @param privateKey 私钥
     * @param cipherData 密文数据
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static  byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception{
        if (privateKey== null){
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher= null;
        try {
            cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
//            byte[] output= cipher.doFinal(cipherData);
//            return output;

            InputStream ins = new ByteArrayInputStream(cipherData);
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            // rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
            byte[] buf = new byte[MAX_DECRYPT_BLOCK];
            int bufl;

            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;

                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    for (int i = 0; i < bufl; i++) {
                        block[i] = buf[i];
                    }
                }
                writer.write(cipher.doFinal(block));
            }
            return writer.toByteArray();

        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }catch (InvalidKeyException e) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException e) {
            throw new Exception("密文数据已损坏");
        }
    }


    /**
     * 字节数据转十六进制字符串
     * @param data 输入数据
     * @return 十六进制内容
     */
    public static String byteArrayToString(byte[] data){
        StringBuilder stringBuilder= new StringBuilder();
        for (int i=0; i<data.length; i++){
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);
            //取出字节的低四位 作为索引得到相应的十六进制标识符
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);
            if (i<data.length-1){
                stringBuilder.append(' ');
            }
        }
        return stringBuilder.toString();
    }

    public static String sign(String content, String privateKey) {
        try {
            PrivateKey priKey = loadPrivateKey(privateKey);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            byte[] signed = signature.sign();
            return new String(Base64.encode(signed));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * RSA验签名检查
     *
     * @param content
     *            待签名数据
     * @param sign
     *            签名值
     * @param public_key
     *            公钥
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, String public_key) {
        try {
            PublicKey pubKey = loadPublicKey(public_key);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            return signature.verify(Base64.decode(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void main(String[] args){
        //RSAEncrypt rsaEncrypt= new RSAEncrypt();
        //rsaEncrypt.genKeyPair();

        List<String> keys = genKeyPair();
        //System.exit(0);

        //加载公钥
        RSAPublicKey publicKey = null;
        try {
            publicKey = loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY);
            System.out.println("加载公钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载公钥失败");
        }

        //加载私钥
        RSAPrivateKey privateKey = null;
        try {
            privateKey = loadPrivateKey(RSAEncrypt.DEFAULT_PRIVATE_KEY);
            System.out.println("加载私钥成功");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println("加载私钥失败");
        }

        //测试字符串
        //String encryptStr= "Test String chaijunkun";
        StringBuilder teststrBuilder = new StringBuilder("app_id=kjqb&biz_data={\"order_no\":\"lo20171129154702489890\",\"approval_time\":1511949125,\"remark\":\"\",\"status\":\"150\"}&biz_enc=0&format=json&method=100020&timestamp=1511949125&version=1.0");
        for(int i = 0 ; i < 1000 ; i++){
            teststrBuilder.append(i);

        }

//        try {
//            //System.out.println((new String(rsaEncrypt.getPublicKey().getEncoded(), "utf-8")));
//            //加密
//            byte[] cipher = encrypt(publicKey, teststrBuilder.toString().getBytes());
//            //解密
//            byte[] plainText = decrypt(privateKey, cipher);
//            System.out.println("密文长度:"+ cipher.length);
//            System.out.println(RSAEncrypt.byteArrayToString(cipher));
//            System.out.println("明文长度:"+ plainText.length);
//            System.out.println(RSAEncrypt.byteArrayToString(plainText));
//            System.out.println(new String(plainText));
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }

        System.out.println("sign-------------------->");
        String sign = sign(teststrBuilder.toString(),keys.get(0));
        System.out.println(sign);

        System.out.println("verify-------------------->");
        System.out.println(verify(teststrBuilder.toString(),sign,keys.get(1)));
    }
}