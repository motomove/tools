package com.whaty.wecharts.util;

import java.util.Random;

/**
 * 随机数字符串生成工具
 */
public class RandomStringUtil {

    /**
     * 生成随机字符串
     * @return
     */
    public static String randomString(){
        return randomString(10);
    }

    /**
     * 生成随机字符串
     * @param len
     * @return
     */
    public static String randomString(int len) {
        String base  = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        System.out.println("生成的字符串为：" + sb.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
        randomString();
    }
}
