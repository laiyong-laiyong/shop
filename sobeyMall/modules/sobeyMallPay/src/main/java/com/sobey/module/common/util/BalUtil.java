package com.sobey.module.common.util;

import com.sobey.module.balance.entity.RechargeBalanceParam;
import com.sobey.module.common.response.ResultInfo;
import com.sobey.module.exception.SignErrorException;
import com.sobey.util.common.regex.PatternUtil;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @Author WCY
 * @CreateTime 2020/4/23 9:57
 * 余额相关工具类
 */
@Component
public class BalUtil {
    private static final Logger log = LoggerFactory.getLogger(BalUtil.class);

    @Value("${key-pair.private}")
    private String privateKey;


    /**
     * 加密金额
     *
     * @param bal
     * @return
     */
    public static String encodeBal(BigDecimal bal) {

        if (null == bal) {
            return "";
        }
        String balStr = bal.toString();
        try {
            byte[] base64 = Base64.encodeBase64(balStr.getBytes(StandardCharsets.UTF_8));
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(base64);
            return HexUtils.toHexString(md5Bytes).toUpperCase();
        } catch (Exception e) {
            log.error("金额加密出错", e);
        }
        return "";
    }

    /**
     * 校验余额充值的参数
     *
     * @param param
     * @return
     */
    public ResultInfo validateRechargeBalanceParam(RechargeBalanceParam param) {
        ResultInfo result = new ResultInfo();
        if (null == param) {
            result.setRt_code("FAIL");
            result.setRt_msg("参数为空");
            return result;
        }
        Field[] fields = param.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                if (null == annotation) {
                    continue;
                }
                if (annotation.required()) {
                    Object val = field.get(param);
                    if (null == val || StringUtils.isBlank(val.toString())) {
                        result.setRt_code("FAIL");
                        result.setRt_msg("参数" + field.getName() + "为空");
                        return result;
                    }
                }
            }
            //校验金额是否大于0并且是否是保留两位小数
            BigDecimal amount = param.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                result.setRt_code("FAIL");
                result.setRt_msg("充值金额必须大于0");
                return result;
            }
            if (!PatternUtil.regNumber(amount.toString())) {
                result.setRt_code("FAIL");
                result.setRt_msg("金额格式不符合要求");
                return result;
            }
            //校验签名是否正确
            String sign = param.getSign();
            String decrypt = RSAUtil.decryptByPri(sign, privateKey);
            BigDecimal decryptAmount = new BigDecimal(decrypt);
            if (amount.compareTo(decryptAmount) != 0){
                result.setRt_code("FAIL");
                result.setRt_msg("加密金额与充值金额不符");
                return result;
            }
            result.setRt_code("SUCCESS");
            result.setRt_msg("");
        } catch (SignErrorException e) {
            log.info("解密失败:", e);
            result.setRt_code("FAIL");
            result.setRt_msg(e.getMessage());
        } catch (Exception e) {
            log.info("校验充值余额参数时发生异常:", e);
            result.setRt_code("FAIL");
            result.setRt_msg("解密失败,请检查公钥或加密算法是否正确");
        }
        return result;
    }

}
