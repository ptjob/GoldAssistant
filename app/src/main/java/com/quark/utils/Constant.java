package com.quark.utils;

/**
 * Created by Administrator on 11/13 0013.
 */
public interface Constant {
    public static final String CODE = "code";


    //订单支付状态 0：未支付，1已支付
    public static final int ORDER_NO_PAY = 0;
    public static final int ORDER_PAY = 1;

    //默认排序按照接单量来排序
    public static final String SORT_DEFAUL = "0";

    //星级倒排排序 由高到低
    public static final String SORT_START_UP_DOWN = "1";

    //距离倒排排序
    public static final String SORT_DISTANCE_UP_DOWN = "2";

    //星级倒排且距离倒排
    public static final String SORT_START_DISTANCE_UP_DOWN = "3";

    //当前用户是否收藏了美容师 0:没有收藏 1:收藏过了
    public static final int COLLECT_NOT = 0;
    public static final int COLLECTED = 1;
}
