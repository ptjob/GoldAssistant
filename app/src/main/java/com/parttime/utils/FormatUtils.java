package com.parttime.utils;

/**
 * 格式化工具
 */
public class FormatUtils {
    public static String formatStr(String str) {
        return str.trim();
    }

    public static int parseToInt(String str) {
        return Integer.parseInt(formatStr(str));
    }
}
