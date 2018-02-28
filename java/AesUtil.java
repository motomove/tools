package com.whaty.platform.common.util;

import org.apache.commons.codec.binary.Base64;

import com.whaty.platform.util.Const;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

/**
 *
 * AES加密工具
 * Created by RyanYin on 17-4-5.
 */
public class AesUtil {

    public static final String UNICODE_KEY = "whaty.key_4862#$";
    public static final String ENCODE = "utf-8";

    public static void main(String[] args) {
        //String content = "{\"phone\":\"1590142d4874\",\"sign\":\"ruike12345\",\"num\":\"1\",\"order\":\"ruike20170411y6b38VC1PK\",\"code\":\"1\"}";

       // String content = "9oNEgMddmLuireu0idXwKCOuTb449V6wHFtpBI2e8XwEo5ZzzEYhH+Wpe7m8I5j2LIA0ejNxjCWxrdwfqG3Hlg9GEN7d3Lfg1dXWOHlNxFV5JB8ysrvm/KCylkuJ44fwSKVU+kwPnAeXL8474LSbZnfVL1Ghd4wflOVdF3hdG9vw2dqp9CC5sW3Q+nAnpgS/0IOMAau7St1NjXTrrS60WPsr117su18qRdsHVd39yjXaPJ8eToWLsPktApEOkXEvNuNr4qXAewEhYFnpoNLYoiTjq+5FRY0RgQyy+M6gCoNCCLWKFvlYfE+x3k44fFJkC81BLz/19jFa5Ltzqhh2wrdx8z8PUq88YlIeFetqoag=";
//        String content = "oP6ZHgRojGSoqsZ9/0itTcrbHXNQyM4pODRFvZiOVrmqppe7JpmD0EiQs/35juh3xvaIYP110MGghbhkmvPPiTp7sV8id8mlQuQgujhN05HNONpvpJdQ9E54zFtFnEUrWqQzI2UqdVKmhD+A/fapa5ayNrI0UnrJ5zk+mEmHYIYWpf2g5ZXNyOjvVtYeGLITml3BpejCbZIUqDZHN9ewzh5f3e4zhEOmjSyDBQFBKgbmA+fxUXExEYrydEY15huSPSP8qrGI28O4i2hdiqYgvCT81vuaZ7OGm7Q3qkcRuH0=";
//        System.out.println("content = " + content);
        try {
//            String e = aesEncrypt(content);
//            System.out.println("Encrypt(content) = " + e);
//            String d = aesDecrypt(content);
//            System.out.println(d);
           
//            String password ="发斯蒂芬课件是付款后";
//          //加密  
//            System.out.println("加密前：" + content);  
//            byte[] encryptResult = encrypt(content, password);  
//            String encryptResultStr = parseByte2HexStr(encryptResult);  
//            System.out.println("加密后：" + encryptResultStr);  
            String content="{\"type\":\"local\",\"siteCode\":\""+"res25"+"\",\"createDate\":\""+Const.getStringDate(new Date(),"yyyy-MM-dd")+"\"}";
            byte[] res =   AesUtil.encrypt(content, "res25");
            String aesStr =AesUtil.parseByte2HexStr(res); //加密后的字符串
            System.out.println("加密后："+aesStr);
            
            //解密  
            String enStr = "9529F86284C7B9E43F6026C53EDC95347CF416F62BF5C1C5391D61675993ACB49AD00A02AE753D16F0B481D22664C88139D8A8F4417F99A83BE7EF96CD8655CF";
           
            byte[] decryptFrom = parseHexStr2Byte(aesStr);  
            byte[] decryptResult = decrypt(decryptFrom,"res25");  
            System.out.println("解密后：" + new String(decryptResult));
            
            
//            String aese = encryptByKey(json,"fjksdfdfdsdfsddddd");
//            System.out.println(aese);
//            String aesd = decrypt(aese,"fjksdfdfdsdfsddddd");
//            System.out.println(aesd);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * AES 加密
     */
    public static String aesEncrypt(String content ) throws Exception {
        byte[] raw = UNICODE_KEY.getBytes(ENCODE);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(content.getBytes(ENCODE));
        return URLEncoder.encode(Base64.encodeBase64String(encrypted), ENCODE);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }
 
    /**
     * AES 解密
     * @param sSrc
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String sSrc) throws Exception {
        try {
            byte[] raw = UNICODE_KEY.getBytes(ENCODE);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.decodeBase64(URLDecoder.decode(sSrc, ENCODE));//先解码再用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,ENCODE);
                return originalString;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    
    
    
    /** 
     * 加密 
     *  
     * @param content 需要加密的内容 
     * @param password  加密密码 
     * @return 
     */  
    public static byte[] encrypt(String content, String password) {  
            try {             
                    KeyGenerator kgen = KeyGenerator.getInstance("AES");  
                    kgen.init(128, new SecureRandom(password.getBytes("utf-8")));  
                    SecretKey secretKey = kgen.generateKey();  
                    byte[] enCodeFormat = secretKey.getEncoded();  
                    SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
                    Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
                    byte[] byteContent = content.getBytes("utf-8");  
                    cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
                    byte[] result = cipher.doFinal(byteContent);  
                    return result; // 加密  
            } catch (NoSuchAlgorithmException e) {  
                    e.printStackTrace();  
            } catch (NoSuchPaddingException e) {  
                    e.printStackTrace();  
            } catch (InvalidKeyException e) {  
                    e.printStackTrace();  
            } catch (UnsupportedEncodingException e) {  
                    e.printStackTrace();  
            } catch (IllegalBlockSizeException e) {  
                    e.printStackTrace();  
            } catch (BadPaddingException e) {  
                    e.printStackTrace();  
            }  
            return null;  
    }  
    
    
    /**解密 
     * @param content  待解密内容 
     * @param password 解密密钥 
     * @return 
     */  
    public static byte[] decrypt(byte[] content, String password) {  
            try {  
                     KeyGenerator kgen = KeyGenerator.getInstance("AES");  
                     kgen.init(128, new SecureRandom(password.getBytes("utf-8")));  
                     SecretKey secretKey = kgen.generateKey();  
                     byte[] enCodeFormat = secretKey.getEncoded();  
                     SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
                     Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
                    cipher.init(Cipher.DECRYPT_MODE, key);// 初始化  
                    byte[] result = cipher.doFinal(content);  
                    return result; // 加密  
            } catch (NoSuchAlgorithmException e) {  
                    e.printStackTrace();  
            } catch (NoSuchPaddingException e) {  
                    e.printStackTrace();  
            } catch (InvalidKeyException e) {  
                    e.printStackTrace();  
            } catch (IllegalBlockSizeException e) {  
                    e.printStackTrace();  
            } catch (BadPaddingException e) {  
                    e.printStackTrace();  
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }  
            return null;  
    }  
    
    /**将二进制转换成16进制 
     * @param buf 
     * @return 
     */  
    public static String parseByte2HexStr(byte buf[]) {  
            StringBuffer sb = new StringBuffer();  
            for (int i = 0; i < buf.length; i++) {  
                    String hex = Integer.toHexString(buf[i] & 0xFF);  
                    if (hex.length() == 1) {  
                            hex = '0' + hex;  
                    }  
                    sb.append(hex.toUpperCase());  
            }  
            return sb.toString();  
    }   
    /**将16进制转换为二进制 
     * @param hexStr 
     * @return 
     */  
    public static byte[] parseHexStr2Byte(String hexStr) {  
            if (hexStr.length() < 1)  
                    return null;  
            byte[] result = new byte[hexStr.length()/2];  
            for (int i = 0;i< hexStr.length()/2; i++) {  
                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
                    result[i] = (byte) (high * 16 + low);  
            }  
            return result;  
    }
}
