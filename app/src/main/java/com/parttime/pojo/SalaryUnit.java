package com.parttime.pojo;

import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;

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
     * 按单
     */
    CASES,
    /**
     * 面议
     */
    FACE_TO_FACE;

    public static SalaryUnit parse(String name) {
        String[] salaryUnitArray = ApplicationControl.getInstance().getResources().getStringArray(R.array.salary_unit);
        if (name.equals(salaryUnitArray[0])) {
            return SalaryUnit.DAY;
        } else if (name.equals(salaryUnitArray[1])) {
            return SalaryUnit.HOUR;
        } else if (name.equals(salaryUnitArray[2])) {
            return SalaryUnit.MONTH;
        } else if (name.equals(salaryUnitArray[3])) {
            return SalaryUnit.TIMES;
        } else if (name.equals(salaryUnitArray[4])) {
            return SalaryUnit.CASES;
        } else if (name.equals(salaryUnitArray[5])) {
            return SalaryUnit.FACE_TO_FACE;
        } else {
            return null;
        }
    }

    public static SalaryUnit parse(int value) {
        switch (value) {
            case 0:
                return DAY;
            case 1:
                return HOUR;
            case 2:
                return MONTH;
            case 3:
                return TIMES;
            case 4:
                return CASES;
            case 5:
                return FACE_TO_FACE;
            default:
                return DAY;
        }
    }
}
