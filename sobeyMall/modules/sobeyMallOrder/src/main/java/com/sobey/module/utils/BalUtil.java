package com.sobey.module.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @Author WCY
 * @CreateTime 2020/4/23 9:57
 * 金额处理工具类
 */
public class BalUtil {
    private static final Logger log = LoggerFactory.getLogger(BalUtil.class);
    /**
     * 加密金额
     * @param bal
     * @return
     */
    public static String encodeBal(BigDecimal bal){

        if (null == bal){
            return "";
        }
        String balStr = bal.toString();
        try {
            byte[] base64 = Base64.encodeBase64(balStr.getBytes(StandardCharsets.UTF_8));
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(base64);
            return HexUtils.toHexString(md5Bytes).toUpperCase();
        } catch (Exception e) {
            log.error("金额加密出错",e);
        }
        return "";
    }

}
