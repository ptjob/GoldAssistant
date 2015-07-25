package com.parttime.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by luhua on 15/7/24.
 */
public class ValidateUtils {

    public static boolean isEmail(String email){
        String regex = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}" ;
        return match( regex ,email );
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match( String regex ,String str ){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher( str );
        return matcher.matches();
    }
}
