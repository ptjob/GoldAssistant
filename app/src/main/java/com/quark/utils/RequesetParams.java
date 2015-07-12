package com.quark.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.os.Bundle;

/**
 * Created by Administrator on 11/5 0005.
 */
public class RequesetParams {
    public static final String DEFAULT_CHARSET = "UTF-8";

    //get请求需要自己生成参数
    public static String buildParams(Bundle param) {
        return buildQuery(param, DEFAULT_CHARSET);
    }

    public static String buildQuery(Bundle param, String charset) {
        if (null == param || param.isEmpty()) {
            return null;
        }
        int size = param.size();

        if (EmptyUtil.isStringEmpty(charset)) {
            charset = DEFAULT_CHARSET;
        }

        boolean hasParam = false;
        StringBuilder query = new StringBuilder("?");
        for (String name : param.keySet()) {

            String value = param.getString(name);
            if (EmptyUtil.isStringNotEmpty(name) && EmptyUtil.isStringNotEmpty(value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }

                try {
                    query.append(name).append("=")
                            .append(URLEncoder.encode(value, charset));
                } catch (UnsupportedEncodingException e) {
                    Logger.e(
                            "buildQuery throws UnsupportedEncodingException!");
                    e.printStackTrace();
                }
            }
        }
        return query.toString();
    }

    /**
     * 登入参数
     *
     * @param telephone
     * @param password
     * @return
     */
    public static Bundle getLoginParams(String telephone, String password) {
        Bundle map = new Bundle();
        map.putString("telephone", telephone);
        map.putString("password", password);
        return map;
    }

    /**
     * 设置密码接口
     *
     * @param telephone
     * @param newPassword
     * @return
     */
    public static Bundle getSetPwdParams(String telephone, String newPassword) {
        Bundle map = new Bundle();
        map.putString("telephone", telephone);
        map.putString("newPassword", newPassword);
        return map;
    }

    /**
     * 获取常用地址
     */
    public static Bundle getCommonAddress(String userId) {
        Bundle map = new Bundle();
        map.putString("user_id", userId);
        return map;
    }

}
