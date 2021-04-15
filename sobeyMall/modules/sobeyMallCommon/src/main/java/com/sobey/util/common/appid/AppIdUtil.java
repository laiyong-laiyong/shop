package com.sobey.util.common.appid;

import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.util.common.uuid.UUIDUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Description TODO...
 * @Author WuChenYang
 * @CreateTime 2020/3/20 11:02
 */
@Component
public class AppIdUtil {

    @Value("${app-id.seed}")
    private String seed;

    /**
     * 生成appId 三个信息用 $ 做连接符userCode$productId$versionId，
     * 加密拼接后的字符串后 将16长度的字符串向量拼接直接放在最前端
     * 然后用 . 做连接符将用于解密的key拼接在最后得到appId
     * appId组成:[长度16的向量字符串]+[userCode$productId$versionId加密后的base64字符串]+[.]+[解密需要的key的base64字符串]
     * @param userCode
     * @param productId
     * @param versionId
     * @return
     */
    public String getAppId(String userCode, String productId, String versionId) {

        if (StringUtils.isBlank(userCode) || StringUtils.isBlank(productId)) {
            return null;
        }
        try {
            String data = userCode + "$" + productId + "$" + (StringUtils.isBlank(versionId) ? "" : versionId);
            KeyGenerator aes = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(seed.getBytes(StandardCharsets.UTF_8));
            //AES算法生成的密钥长度
            aes.init(128, secureRandom);
            SecretKey secretKey = aes.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            String key = Base64.encodeBase64String(keyBytes);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //CBC模式需要128位的向量
            String ivChars = getRandomChar(16);
            IvParameterSpec iv = new IvParameterSpec(ivChars.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String result = ivChars + Base64.encodeBase64String(bytes) + "." + key;
            return result;
        } catch (Exception e) {
            throw new AppException(ExceptionType.SYS_RUNTIME, e);
        }

    }

    /**
     * 生成md5的appId
     * @param userCode
     * @param productId
     * @param versionId
     * @return
     */
    public String getAppIdMD5(String userCode, String productId, String versionId){
        if (StringUtils.isBlank(userCode) || StringUtils.isBlank(productId)) {
            return null;
        }
        String data = userCode + "$" + productId + "$" + (StringUtils.isBlank(versionId) ? "" : versionId)+"$"+ UUIDUtils.uuid();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(data.getBytes(StandardCharsets.UTF_8));
            return byteToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new AppException(ExceptionType.SYS_RUNTIME, e);
        }
    }

    /**
     * AppId解码 截取字符串最前端16位得到向量字符串，
     * 用 . 分割截取后剩下的字符串得到长度2的字符串数组，
     * 然后用base64分别解码。
     * 第一个元素为解密内容字节数组，第二个为解密所需的key的字节数组
     *
     * @param data
     * @return
     */
    public String decodeAppId(String data) {
        if (StringUtils.isBlank(data)) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "AppId长度不符合要求", new RuntimeException());
        }

        try {
            if (data.length() < 16) {
                throw new AppException(ExceptionType.SYS_PARAMETER, "AppId长度不符合要求", new RuntimeException());
            }
            String iv = data.substring(0, 16);
            String[] split = data.substring(16).split("\\.");
            if (split.length != 2) {
                throw new AppException(ExceptionType.SYS_PARAMETER, "AppId格式错误", new RuntimeException());
            }
            byte[] appId = Base64.decodeBase64(split[0]);
            byte[] key = Base64.decodeBase64(split[1]);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
            byte[] bytes = cipher.doFinal(appId);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new AppException(ExceptionType.SYS_RUNTIME, e);
        }
    }

    /**
     * 获取指定长度的随机字符串
     *
     * @param length 随机字符串长度
     * @return randomChars
     */
    public static String getRandomChar(int length) {
        StringBuilder randomChars = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            //字母和数字中随机
            if (random.nextInt(2) % 2 == 0) {
                //输出是大写字母还是小写字母
                int letterIndex = random.nextInt(2) % 2 == 0 ? 65 : 97;
                randomChars.append((char) (random.nextInt(26) + letterIndex));
            } else {
                randomChars.append(String.valueOf(random.nextInt(10)));
            }
        }
        return randomChars.toString();
    }

    public static String byteToHex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (byte item : bytes) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

}
