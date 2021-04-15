package com.sobey.module.common.util;

import com.sobey.module.exception.SignErrorException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.Cipher;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WCY
 * @createTime 2020/7/10 9:54
 * 公私钥相关工具类
 */
public class RSAUtil {

    /**
     * 随机生成公钥私钥对
     * @return
     */
    public static Map<String,String> generateKeyPair() throws NoSuchAlgorithmException {

        KeyPairGenerator rsa = KeyPairGenerator.getInstance("RSA");
        rsa.initialize(1024,new SecureRandom());
        KeyPair keyPair = rsa.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();//得到私钥
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();//得到公钥
        String publicStr = Base64.encodeBase64String(publicKey.getEncoded());
        String privateStr = Base64.encodeBase64String(privateKey.getEncoded());
        Map<String,String> keyMap = new HashMap<>();
        keyMap.put("public",publicStr);
        keyMap.put("private",privateStr);
        return keyMap;
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String encryptByPub(String data, String publicKey) throws Exception{
        if (StringUtils.isBlank(publicKey)){
            throw new RuntimeException("public key is blank");
        }
        if (StringUtils.isBlank(data)){
            throw new RuntimeException("data is blank");
        }
        byte[] decoded = Base64.decodeBase64(publicKey);
        //设置5分钟过期时间
        Date expireDate = DateUtils.addMinutes(new Date(), 5);
        long millionSeconds = expireDate.getTime();
        data = data+"$"+millionSeconds;
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,pubKey);
        return Base64.encodeBase64String(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPri(@NotNull String data,@NotNull String privateKey) throws SignErrorException{
        if (StringUtils.isBlank(privateKey)){
            throw new RuntimeException("private key is blank");
        }
        if (StringUtils.isBlank(data)){
            throw new RuntimeException("data is blank");
        }
        try {
            byte[] bytes = Base64.decodeBase64(data);
            byte[] privateBytes = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateBytes));
            //rsa解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE,priKey);
            byte[] doFinal = cipher.doFinal(bytes);
            String result = new String(doFinal);
            //判断是否过期
            if (!result.contains("$")){
                throw new IllegalArgumentException("数据格式错误");
            }
            String[] split = result.split("\\$");
            long expireTime = Long.parseLong(split[1]);
            if (expireTime<System.currentTimeMillis()){
                throw new RuntimeException("已过期");
            }
            return split[0];
        } catch (Exception e) {
            throw new SignErrorException(e.getMessage());
        }
    }
}
