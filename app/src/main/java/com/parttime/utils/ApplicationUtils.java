package com.parttime.utils;

import com.parttime.constants.SharedPreferenceConstants;
import com.quark.jianzhidaren.ApplicationControl;

/**
 * 应用帮助类
 * Created by wyw on 2015/7/25.
 */
public class ApplicationUtils {
    public static final String DEF_CITY = "深圳";

    public static String getLoginName() {
        return SharePreferenceUtil.getInstance(ApplicationControl.getInstance()).loadStringSharedPreference(SharedPreferenceConstants.COMPANY_NAME);
    }

    public static int getLoginId() {
        return Integer.parseInt(SharePreferenceUtil.getInstance(ApplicationControl.getInstance()).loadStringSharedPreference(SharedPreferenceConstants.COMPANY_ID));
    }

    public static String getCity() {
        return SharePreferenceUtil.getInstance(ApplicationControl.getInstance()).loadStringSharedPreference(SharedPreferenceConstants.CITY, DEF_CITY);
    }
}
