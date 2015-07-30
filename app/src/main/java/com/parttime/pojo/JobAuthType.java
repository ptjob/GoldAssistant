package com.parttime.pojo;

/**
 * Created by wyw on 2015/7/31.
 */
public enum JobAuthType {
    // 删除，下架
    DELETE,
    // 未审核
    READY_TO_PASS,
    // 审核通过
    PASS,
    // 审核不通过
    FAIL_TO_PASS;

    public static JobAuthType parse(int value) {
        switch (value) {
            case 0:
                return DELETE;
            case 1:
                return READY_TO_PASS;
            case 2:
                return PASS;
            case 3:
                return FAIL_TO_PASS;
            default:
                return DELETE;
        }
    }
}
