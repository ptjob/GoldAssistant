package com.parttime.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wyw on 2015/7/25.
 */
public class TimeUtils {
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";

    public static boolean before(String date1, String date2, String dateFormatStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatStr);
        return simpleDateFormat.parse(date1).before(simpleDateFormat.parse(date2));
    }

    public static boolean beforeToday(String date, String dateFormatStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatStr);
        return simpleDateFormat.parse(date).before(new Date());
    }
}
