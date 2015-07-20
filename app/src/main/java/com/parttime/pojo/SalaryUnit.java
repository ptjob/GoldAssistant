package com.parttime.pojo;

/**
 * 薪酬类型
 * Created by wyw on 2015/7/20.
 */
public enum SalaryUnit {
    /**
     * 日薪
     */
    DAY,
    /**
     * 时薪
     */
    HOUR,
    /**
     * 月薪
     */
    MONTH,
    /**
     * 按次
     */
    TIMES,
    /**
     * 面议
     */
    FACE_TO_FACE;

    public static SalaryUnit parse(String name) {
        if (name.equals("日薪")) {
            return SalaryUnit.DAY;
        } else if (name.equals("时薪")) {
            return SalaryUnit.HOUR;
        } else if (name.equals("月薪")) {
            return SalaryUnit.MONTH;
        } else if (name.equals("按次")) {
            return SalaryUnit.TIMES;
        } else if (name.equals("面议")) {
            return SalaryUnit.FACE_TO_FACE;
        } else {
            return null;
        }
    }
}
