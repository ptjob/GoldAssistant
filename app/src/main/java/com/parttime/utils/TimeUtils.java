package com.parttime.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wyw on 2015/7/25.
 */
public class TimeUtils {
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public static String daysPattern = "yyyy-MM-dd";
    public static String secondOnlyPattern = "mm:ss";
    public static String pattern = "yyyyMMddHHmmss";
    public static String pattern1 = "HH:mm";
    public static String pattern6 = "yyyy-MM-dd HH:mm:ss";
    public static String pattern7 = "yy-MM-dd HH:mm";
    public static String pattern8 = "yy-MM-dd";
    public static String attachnamePattern = "yyyyMMddHHmmss";
    public static String minutePattern = "HH:mm";
    public static String secondPattern = "HH:mm:ss";
    public static String millsSecondPattern = "HH:mm:ss SSS";

    public static String weekPattern = "EEEE";
    public static String pattern2 = "MM-dd";
    public static String pattern3 = "yyyy-MM-dd";
    public static String pattern4 = "EEEE HH:mm";
    public static String pattern5 = "MM-dd HH:mm";
    public static String patternVoteExpire = "M月d日 HH:mm";
    public static String patternVoteExpire_2 = "yyyy年M月d日 HH:mm";

    public static boolean before(String date1, String date2, String dateFormatStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatStr);
        return simpleDateFormat.parse(date1).before(simpleDateFormat.parse(date2));
    }

    public static boolean beforeToday(String date, String dateFormatStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatStr);
        return simpleDateFormat.parse(date).before(new Date());
    }
}
