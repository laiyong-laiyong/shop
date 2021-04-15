package com.sobey.module.wxpay.utils;

import com.github.wxpay.sdk.WXPayUtil;
import com.sobey.module.wxpay.common.WXPayConstant;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

/**
 * @Description 签名相关工具类
 * @Author WuChenYang
 * @Since 2020/1/14 11:56
 */
public class SignUtil {

    /**
     * 使用给定的参数连接符拼接参数
     *
     * @param param
     * @return
     */
    public static String spliceParam(@NotNull List<Map.Entry<String, String>> param, @NotNull String symbol) {

        if (param == null || param.size() == 0 || StringUtils.isEmpty(symbol)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : param) {
            if (StringUtils.isBlank(entry.getValue())){
                continue;
            }
            builder.append(symbol).append(entry.getKey().trim()).append("=").append(entry.getValue().trim());
        }
        //去掉第一个连接符
        return builder.toString().substring(1);

    }

    /**
     * 签名
     * @param data 数据
     * @param key 密钥
     * @param alg 算法 HMAC-SHA256或MD5
     * @return
     */
    public static String sign(String data,String key,String alg) throws Exception{
        if (StringUtils.isEmpty(alg)){
            return null;
        }
        if (WXPayConstant.HMAC_SHA256.equals(alg)){
            return hmacSHA256(data.trim(), key.trim());
        }
        if (WXPayConstant.MD5.equals(alg)){
            return md5(data.trim(), key.trim());
        }
        return null;
    }

    /**
     * hmac-sha256加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String hmacSHA256(@NotNull String data, @NotNull String key) throws Exception{

        if (StringUtils.isEmpty(data) || StringUtils.isEmpty(key)) {
            return null;
        }
        String sign = WXPayUtil.HMACSHA256(data, key);
        return sign.toUpperCase();

    }

    /**
     * md5加密
     *
     * @param data
     * @param key
     * @return
     */
    public static String md5(String data, String key) throws Exception{

        if (StringUtils.isEmpty(data)) {
            return null;
        }
        String keyData = data + (StringUtils.isEmpty(key) ? "" : "&key=" + key);
        String sign = WXPayUtil.MD5(keyData).toUpperCase();
        return sign;

    }

    /**
     * 字节数组转字符串
     *
     * @param bytes
     * @return
     */
    public static String byteArrToStr(byte[] bytes) {

        if (null == bytes || bytes.length == 0) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        String temp = "";
        for (byte b : bytes) {
            temp = Integer.toHexString(b & 255 | 256).substring(1,3);
            buffer.append(temp.toUpperCase());
        }
        return buffer.toString();

    }


    /**
     * 随机生成字符串
     *
     * @param length 随机字符串的长度
     * @return 随机字符串
     */
    public static String getRandomChars(int length) {

        String randomChars = "";
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            //字母和数字中随机
            if (random.nextInt(2) % 2 == 0) {
                //输出是大写字母还是小写字母
                int letterIndex = random.nextInt(2) % 2 == 0 ? 65 : 97;
                randomChars += (char) (random.nextInt(26) + letterIndex);
            } else {
                randomChars += String.valueOf(random.nextInt(10));
            }
        }
        return randomChars;

    }

}
