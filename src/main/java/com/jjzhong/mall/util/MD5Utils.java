package com.jjzhong.mall.util;

import com.jjzhong.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 工具类
 */
public class MD5Utils {
    /**
     * 获取 MD5
     * @param src 源字符串
     * @return MD5
     */
    public static String getMD5Str(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return Base64.encodeBase64String(md5.digest((src + Constant.SALT).getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
