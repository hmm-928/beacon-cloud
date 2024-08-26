package com.mashibing.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验手机号格式的合法性正则
 * @author zjw
 * @description
 */
public class PhoneFormatCheckUtil {

    /**
     * 国内手机号的正则表达式
     */
    private final static Pattern CHINA_PATTERN = Pattern.compile("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$");

    /**
     * 根据正则校验手机号是否合法
     * @param number
     * @return
     */
    public static boolean isChinaPhone(String number){
        Matcher matcher = CHINA_PATTERN.matcher(number);
        return matcher.matches();
    }

}
