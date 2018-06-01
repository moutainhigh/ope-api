package com.xianjinxia.utils;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Created by wangwei on 2017/11/25.
 * 暂时不可用,使用RSAEncrypt
 */
public class RSABase {

    private static final String ALGORITHM = "RSA";

    //private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static final String SIGN_ALGORITHMS = "MD5withRSA";

    private static final String DEFAULT_CHARSET = "UTF-8";

    /** RSA最大加密明文大小 */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** RSA最大解密密文大小 */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static final String TRANSFORMATION = "RSA";

    public static String sign(String content, String privateKey) {
        try {
            PrivateKey priKey = getPrivateKey(privateKey);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            byte[] signed = signature.sign();
            return byteArrayToString(Base64.getEncoder().encode(signed));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解密
     *
     * @param content
     *            密文
     * @param private_key
     *            私钥
     * @return 解密后的字符串
     */
    public static String decryptByPrivateKey(String content, String private_key) throws Exception {
        return decrypByKey(content, getPrivateKey(private_key));
    }

    /**
     * 解密
     *
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    private static String decrypByKey(String content, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key);
        InputStream ins = new ByteArrayInputStream(Base64.getDecoder().decode(content));
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
        return new String(writer.toByteArray(), DEFAULT_CHARSET);
    }

    /**
     * 得到私钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    protected static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64.getEncoder().encode(key.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 从文件中加载私钥
     *
     * @throws Exception
     */
    protected static PrivateKey getPrivateKey(InputStream in) throws Exception {
        return getPrivateKey(readFileKey(in));
    }


    /**
     * 从文件中加载私钥
     *
     * @throws Exception
     */
    protected static PublicKey getPublickKey(InputStream in) throws Exception {
        return getPublickKey(readFileKey(in));
    }


    /**
     * 读取文件中的秘钥信息
     * @param in
     * @return
     * @throws Exception
     */
    private static String readFileKey(InputStream in) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
                sb.append('\r');
            }
        }
        return sb.toString();
    }


    /**
     * 字节数据转字符串专用集合
     */
    private static final char[] HEX_CHAR= {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

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


    /**
     * 根据制定的可以进行加密
     *
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static String encyptByKey(String content, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] data = content.getBytes(DEFAULT_CHARSET);
        int inputlen = data.length;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // rsa加密的字节大小最多是117,分段加密
        while (inputlen - offset > 0) {
            if (inputlen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputlen - offset);
            }
            bos.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptData = bos.toByteArray();
        bos.close();
        return byteArrayToString(Base64.getEncoder().encode(encryptData));
    }

    /**
     *
     * @param content
     *            原始数据
     * @param publicKey
     *            密钥字符串（经过base64编码）
     * @return 加密后的数据
     * @throws Exception
     */
    public static String encyptByPublicKey(String content, String publicKey) throws Exception {
        return encyptByKey(content, getPublickKey(publicKey));
    }

    /**
     *
     * @param content
     *            原始数据
     * @param privateKey
     *            密钥字符串（经过base64编码）
     * @return 加密后的数据
     * @throws Exception
     */
    public static String encyptByPrivateKey(String content, String privateKey) throws Exception {
        return encyptByKey(content, getPrivateKey(privateKey));
    }

    /**
     * 得到公钥
     *
     * @param publicKey
     *            密钥字符串（经过base64编码）
     * @return
     * @throws Exception
     */
    private static PublicKey getPublickKey(String publicKey) throws Exception {
        byte[] buffer = Base64.getDecoder().decode(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
        return keyFactory.generatePublic(keySpec);
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
            PublicKey pubKey = getPublickKey(public_key);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));
            return signature.verify(Base64.getDecoder().decode(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 随机生成密钥对
     */
    public void genKeyPair(){
        /**
         * 私钥
         */
        RSAPrivateKey privateKey;

        /**
         * 公钥
         */
        RSAPublicKey publicKey;
        KeyPairGenerator keyPairGen= null;
        try {
            keyPairGen= KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair= keyPairGen.generateKeyPair();
        privateKey= (RSAPrivateKey) keyPair.getPrivate();
        publicKey= (RSAPublicKey) keyPair.getPublic();
    }




    public static void main(String[] args) {

         String PUBLICK_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClexlMC+y6aqjPOM4dIF3xbu3o"
                + "OnA41wG9l2GsomyaUILJR9/ppFVfabSU3acTdSkzq451PbTtKOo+kP8Ih/2qhV5N"
                + "Z2h1bptM28W2rSKPKcDt5o754L+GsXbWo39zd7uS5yZFKhLZw7YVi3fJ6ty5XMMs" + "7ERx/hMY/bYqyTAabQIDAQAB";
         String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKV7GUwL7LpqqM84"
                + "zh0gXfFu7eg6cDjXAb2XYayibJpQgslH3+mkVV9ptJTdpxN1KTOrjnU9tO0o6j6Q"
                + "/wiH/aqFXk1naHVum0zbxbatIo8pwO3mjvngv4axdtajf3N3u5LnJkUqEtnDthWL"
                + "d8nq3LlcwyzsRHH+Exj9tirJMBptAgMBAAECgYBPEqdf40LXQSgw/N/goxrAx1T/"
                + "Zw1A29yFD9UofOSQSHB2ZdXk+xWgZg5YJCI19gIeIpgMBdRyjt5/zyFOnkzjbeXF"
                + "Jx0i8i+8AKUsU+NencswTiPy9lHQKTLIbFEifDxl2IWaCaQ2kn+3z+jfxbcm9znT"
                + "vsFlu1kcaY/lNLZVsQJBANDsIt1yyEK8Wi68TdXRGuxl1OD8c9nZkLVTHax+1JWo"
                + "QyjI2rF4H3Do+S9QwofV26SDgPKsZwKNbTuHxlnWWTsCQQDKxQAWpP3HoHFAG2LQ"
                + "2KTJwBZtfM1sQjeN64hQ8cj89o1XRQjF2oWO0Ve+ibeDzMmGaLeky9WDfmnp6G05"
                + "2+B3AkBv+6JUgInG+557HoO57/M2cv6/+ZE/W9as2ng1VWYtMZuN6NsP9QslQjsO"
                + "mYMru/2XGMWtTauJOrUqC4TN8o6xAkEAw7l86F7CiJsaNiM5MxarmgLMo0rAjysr"
                + "rNYZcuiwdV0X+ZEtcq4IFV+FLuqINbTLIe6atXv70T2IxrwQErVm6QJAWfjVujrj"
                + "8yjGWK/gZDK5b/eSuDTg5lMJneGEzZm8uXRVUQqka5dP+j8vFr9IuEYb/N4CN5Je" + "E7x8NgrbgXVlbA==";


        // 加解密测试
        String content = "2rwssfssdafsfj";
        String encrypt = null;
        try {
            encrypt = encyptByPublicKey(content,PUBLICK_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(encrypt);
        try {
            System.out.println(decryptByPrivateKey(encrypt,PRIVATE_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 签名测试
        String sign = sign(content, PRIVATE_KEY);
        System.out.println(sign);
        System.out.println(verify(content, sign, PUBLICK_KEY));
    }



}
