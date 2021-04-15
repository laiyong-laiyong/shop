package com.sobey.util.common.uuid;

import java.security.SecureRandom;

/**
 * @Description 随机数相关工具类
 * @Author WuChenYang
 * @Since 2020/1/22 15:11
 */
public class RandomUtils {

    /**
     * 随机生成指定长度的字符串
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
