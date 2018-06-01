package com.xianjinxia.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密和解密过程中，密钥长度都必须是8的倍数
 */
public class DesECB {

	/**
	 * 加密原文：test
	 * 测试key：01234567890123456789012345678901
	 * 加密结果：7C5FB2FBCB47EAAC 
	 */
	public static void main(String[] args) throws Exception {
		String value = "dingc";
		String key = "4CD893A7";
		System.out.println("加密原文：" + value);
//		System.out.println("加密后：" + encryptDES(value, key, "UTF-8"));
		System.out.println("解密后：" + decryptData("3317a96ad41f7e3034e9d4002101876f756562844810a042a47b62366dcc5ea429abd60afc5aed06bc353a8e8cd89eae5adc532eb52c2d567ec00e6623e7ff24f186e7d7d2470c78", key));
	}

	/**
	 * 加密
	 * @param value 原文
	 * @param key   密码
	 * @param encode  编码
	 * @return  加密后的文本
	 */
	public static String encryptDES(String value, String key)
			throws Exception {
		SecretKeySpec localSecretKeySpec = new SecretKeySpec(key.substring(0, 8).getBytes("UTF-8"), "DES");
		Cipher localCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		localCipher.init(Cipher.ENCRYPT_MODE, localSecretKeySpec);
		return bytesToHexString(localCipher.doFinal(value.getBytes("UTF-8")));
	}
	
	/** 
     * A解密 
     * @param base64Data 
     * @return 
     * @throws Exception 
     */  
    public static String decryptData(String base64Data, String key) throws Exception{  
    	SecretKeySpec localSecretKeySpec = new SecretKeySpec(key.substring(0, 8).getBytes("UTF-8"), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");  
        cipher.init(Cipher.DECRYPT_MODE, localSecretKeySpec);  
        return new String(cipher.doFinal(HexString2Bytes(base64Data)));  
    }  

	/**
	 * 将二进制转换为字符串，并转为大写
	 * @param src 二进制文件
	 */
	public static String bytesToHexString(byte[] src) {
		if (src == null || src.length <= 0) {
			return null;
		}
		StringBuilder string = new StringBuilder();
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				string.append(0);
			}
			string.append(hv);
		}
		return string.toString().toUpperCase();
	}
	
	// 从十六进制字符串到字节数组转换 
	public static byte[] HexString2Bytes(String hexstr) {
	    byte[] b = new byte[hexstr.length() / 2];
	    int j = 0;
	    for (int i = 0; i < b.length; i++) {
	        char c0 = hexstr.charAt(j++);
	        char c1 = hexstr.charAt(j++);
	        b[i] = (byte) ((parse(c0) << 4) | parse(c1));
	    }
	    return b;
	}
	
	private static int parse(char c) {
	    if (c >= 'a') return (c - 'a' + 10) & 0x0f;
	    if (c >= 'A') return (c - 'A' + 10) & 0x0f;
	    return (c - '0') & 0x0f;
	}
}
