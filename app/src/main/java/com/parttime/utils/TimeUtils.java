package com.parttime.utils;

import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wyw on 2015/7/25.
 */
public class TimeUtils {
    public static final String DATE_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YMD = "yyyy-MM-dd";
    public static final String DATE_FORMAT_MD = "MM-dd";
    public static final String pattern1 = "yyyy-MM-dd";

    public static boolean before(String date1, String date2, String dateFormatStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatStr);
        return simpleDateFormat.parse(date1).before(simpleDateFormat.parse(date2));
    }

    public static boolean beforeToday(String date, String dateFormatStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormatStr);
        return simpleDateFormat.parse(date).before(new Date());
    }

    public static long getTime(String time, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getMMdd(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_MD);
        return simpleDateFormat.format(date);
    }

    public static String getJobPlazaFormatTime(String nowStr, String publishTimeStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_YMDHMS);
        Date now = format.parse(nowStr);
        Date publishTime = format.parse(publishTimeStr);
        String result;
        long distanceMs = now.getTime() - publishTime.getTime();
        if (distanceMs < 60 * 60 * 1000) {
            // 1小时内
            int distanceMinutes = (int) (distanceMs / (1000 * 60));
            result = ApplicationControl.getInstance().getString(R.string.job_plaza_item_time_minutes_format, distanceMinutes);
        } else if (distanceMs < 24 * 60 * 60 * 1000) {
            // 24小时内
            int distanceHour = (int) (distanceMs / (1000 * 60 * 60));
            result = ApplicationControl.getInstance().getString(R.string.job_plaza_item_time_near_format, distanceHour);
        } else {
            // 24小时前
            int distanceDay = (int) (distanceMs / (1000 * 60 * 60 * 24));
            result = ApplicationControl.getInstance().getString(R.string.job_plaza_item_time_day_format, distanceDay);
        }

        return result;
    }
}
