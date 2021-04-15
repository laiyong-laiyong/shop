package com.sobey.module.common.util;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.github.wxpay.sdk.WXPayUtil;
import com.sobey.exception.AppException;
import com.sobey.exception.ExceptionType;
import com.sobey.module.common.response.ResultInfo;
import com.sobey.module.voucher.entity.CreateVouParam;
import com.sobey.util.common.regex.PatternUtil;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;

/**
 * @author wcy
 * 代金券相关工具类
 */
@Component
public class VoucherUtil {

    private static final Logger log = LoggerFactory.getLogger(VoucherUtil.class);

    @Value("${voucher.seed}")
    private String seed;

    /**
     * 根据代金券金额生成代金券兑换码
     *
     * @param amount
     * @return
     */
    public String getVoucherCode(double amount) {
        try {
            if (amount <= 0) {
                throw new AppException(ExceptionType.SYS_PARAMETER, "代金券金额必须大于0", new RuntimeException());
            }
            String data = amount + "$";
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
            String code = ivChars + Base64.encodeBase64String(bytes) + "." + key;
            //将最后得到的内容在进行base64加密返回
            return Base64.encodeBase64String(code.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new AppException(ExceptionType.SYS_RUNTIME, "生成代金券时发生异常", e);
        }
    }

    /**
     * 根据创建人userCode 和 金额生成md5码的代金券
     * @param amount
     * @param userCode
     * @return
     */
    public String getVouCodeMd5(BigDecimal amount,String userCode){
        try {
            String data = userCode+"$"+amount.toString()+"$"+ IdWorker.getIdStr();
            String encode_1 = WXPayUtil.MD5(data);
            //二次加密
            return WXPayUtil.MD5(encode_1);
        } catch (Exception e) {
            throw new AppException(ExceptionType.SYS_RUNTIME, "生成代金券时发生异常", e);
        }

    }

    /**
     * 解析代金券兑换码
     *
     * @param code
     * @return
     */
    public BigDecimal decodeVoucherCode(String code) {

        if (StringUtils.isBlank(code)) {
            throw new AppException(ExceptionType.SYS_PARAMETER, "代金券兑换码不能为空", new RuntimeException());
        }
        try {
            String data = new String(Base64.decodeBase64(code), StandardCharsets.UTF_8);
            String iv = data.substring(0, 16);
            String[] split = data.substring(16).split("\\.");
            byte[] amountBytes = Base64.decodeBase64(split[0]);
            byte[] key = Base64.decodeBase64(split[1]);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv.getBytes("UTF-8")));
            byte[] bytes = cipher.doFinal(amountBytes);
            String amountStr = new String(bytes, StandardCharsets.UTF_8);
            double amount = Double.parseDouble(amountStr.substring(0, amountStr.length() - 1));
            return BigDecimal.valueOf(amount);
        } catch (Exception e) {
            throw new AppException(ExceptionType.SYS_RUNTIME, "代金券兑换码解析异常", e);
        }

    }

    /**
     * 获取指定长度的随机字符串
     *
     * @param length
     * @return
     */
    private static String getRandomChar(int length) {
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

    /**
     * 校验创建代金券的参数
     * @param list
     * @return
     */
    public static ResultInfo validateCreateVouParam(List<CreateVouParam> list){
        ResultInfo resultInfo = new ResultInfo();
        if (null == list || list.size() == 0){
            resultInfo.setRt_code("FAIL");
            resultInfo.setRt_msg("参数为空");
            return resultInfo;
        }
        try {
            for (CreateVouParam param : list) {
                Field[] fields = param.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                    if (null == annotation){
                        continue;
                    }
                    if (annotation.required()){
                        Object val = field.get(param);
                        if (null == val || StringUtils.isBlank(val.toString())){
                            resultInfo.setRt_code("FAIL");
                            resultInfo.setRt_msg("第"+(list.indexOf(param)+1)+"个对象的参数"+field.getName()+"为空");
                            return resultInfo;
                        }
                        //校验金额是否大于0并且是否是保留两位小数
                        if (field.getType().getTypeName().equals(BigDecimal.class.getTypeName())){
                            BigDecimal amount = new BigDecimal(val.toString());
                            if (amount.compareTo(BigDecimal.ZERO) <= 0){
                                resultInfo.setRt_code("FAIL");
                                resultInfo.setRt_msg("金额必须大于0");
                                return resultInfo;
                            }
                            if (!PatternUtil.regNumber(val.toString())){
                                resultInfo.setRt_code("FAIL");
                                resultInfo.setRt_msg("金额格式不符合要求");
                                return resultInfo;
                            }
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.info("校验创建代金券的入参发生异常:"+e);
            resultInfo.setRt_code("FAIL");
            resultInfo.setRt_msg("系统异常");
            return resultInfo;
        }
        resultInfo.setRt_code("SUCCESS");
        resultInfo.setRt_msg("");
        return resultInfo;

    }

}
