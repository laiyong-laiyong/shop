package com.sobey.util.business;


import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sobey.util.common.string.StrKit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * hive调用工具
 * Created by Rukiy on 2017-12-26
 */
public class HiveHeaderKit {


    /**
     * 免登陆调用头
     * @param site
     * @param system
     * @return
     */
    public static Map<String, String> get(String site,String system) {
        Map header = new HashMap();
        header.put("sobeyhive-http-system", system);
        header.put("sobeyhive-http-site", site);
        return header;
    }

    /**
     * 获取token头
     * @param token
     * @param site
     * @param system
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> getHeader(String token, String site,String system) {
        Map headers = new HashMap();
        if(StrKit.isNotEmpty(token)){
            headers.put("sobeyhive-http-token", token);
        }
        if(StrKit.isNotEmpty(system)){
        	headers.put("sobeyhive-http-system", system);
        }
        if(StrKit.isNotEmpty(site)){
        	headers.put("sobeyhive-http-site", site);
        }
        return headers;
    }

    /**
     * 构造签名头
     *
     * @param site
     * @param systemCode
     * @param systemSecretKey
     * @param userCode
     * @param requestMethod
     * @param contentType
     * @return
     */
    public static Map<String, String> getSignature(String site, String systemCode, String systemSecretKey, String userCode, String requestMethod, String contentType) {
        Map<String, String> headers = new HashMap<>();
        SigVO sigVO = getSignature(systemSecretKey, requestMethod, contentType);
        headers.put("sobeyhive-http-site", site);
        headers.put("sobeyhive-http-system", systemCode);
        headers.put("sobeyhive-http-date", sigVO.getDateStr());
        String authorization = "SobeyHive " + systemCode + ":" + sigVO.getSig();
        headers.put("sobeyhive-http-authorization", authorization.trim());
        headers.put("Content-Type", contentType);
        if (StrKit.isNotEmpty(userCode)) {
            headers.put("current-user-code", userCode);
        }
        return headers;
    }


    /**
     * 获取sig
     *
     * @return
     */
    private static SigVO getSignature(String systemSecretKey, String requestMethod, String contentType) {
        String Content_MD5 = "";
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
        String dateStr = dateFormat.format(new Date());
        String signature = buildSignature(requestMethod, Content_MD5, contentType, dateStr, systemSecretKey);
        SigVO sigVO = new SigVO();
        sigVO.setDateStr(dateStr);
        sigVO.setSig(signature);
        return sigVO;
    }

    /**
     * 通过请求的信息和用户秘钥，构造一个签名
     *
     * @param requestMethod
     * @param contentMD5
     * @param contentType
     * @param dateStr
     * @param systemSecretKey
     * @return
     */
    private static String buildSignature(String requestMethod, String contentMD5, String contentType, String dateStr, String systemSecretKey) {
        String stringToSign = new StringBuilder(requestMethod).append("\n")
                .append(contentMD5)
                .append("\n")
                .append(contentType)
                .append("\n")
                .append(dateStr).toString();
        String signature = null;
        try {
            signature = (new BASE64Encoder()).encode(HmacSHA1Encrypt(systemSecretKey, stringToSign)) + "\r\n";
        } catch (Exception e) {
        }
        return signature;
    }

    private static class SigVO {

        private String sig;

        private String dateStr;

        public String getSig() {
            return sig;
        }

        public void setSig(String sig) {
            this.sig = sig;
        }

        public String getDateStr() {
            return dateStr;
        }

        public void setDateStr(String dateStr) {
            this.dateStr = dateStr;
        }
    }


    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";

    /**
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return
     * @throws Exception
     */
    private static byte[] HmacSHA1Encrypt(String encryptKey, String encryptText) throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        //生成一个指定 Mac 算法 的 Mac 对象
        Mac mac = Mac.getInstance(MAC_NAME);
        //用给定密钥初始化 Mac 对象
        mac.init(secretKey);

        byte[] text = encryptText.getBytes(ENCODING);
        //完成 Mac 操作
        return mac.doFinal(text);
    }
}
