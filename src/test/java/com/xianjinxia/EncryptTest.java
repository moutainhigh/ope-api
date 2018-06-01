package com.xianjinxia;

import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Created by lq on 2017/12/5.
 */
public class EncryptTest {

    @Test
    public void testBase64() throws  Exception{
        String str = "heool";

        String strBeEncode = new BASE64Encoder().encode(str.getBytes("UTF-8"));

        String strBeDecode = new String(new BASE64Decoder().decodeBuffer(strBeEncode),"UTF-8");

        System.out.println(strBeEncode);
        System.out.println(strBeDecode);



    }

}
