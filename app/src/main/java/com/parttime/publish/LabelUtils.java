package com.parttime.publish;

import android.content.Context;

import com.parttime.pojo.SalaryUnit;
import com.qingmu.jianzhidaren.R;

/**
 * Created by wyw on 2015/8/3.
 */
public class LabelUtils {

    public static String getSalary(Context context, SalaryUnit unit, int salary) {
        return salary + " " + getSalaryUnit(context, unit);
    }

    public static String getSalaryUnit(Context context, SalaryUnit unit) {
        String salaryUnit = "";
        if (unit != null) {
            switch (unit) {
                case DAY:
                    salaryUnit = context.getString(R.string.publish_job_salary_unit_day);
                    break;
                case HOUR:
                    salaryUnit = context.getString(R.string.publish_job_salary_unit_hour);
                    break;
                case MONTH:
                    salaryUnit = context.getString(R.string.publish_job_salary_unit_month);
                    break;
                case TIMES:
                    salaryUnit = context.getString(R.string.publish_job_salary_unit_times);
                    break;
                case CASES:
                    salaryUnit = context.getString(R.string.publish_job_salary_unit_cases);
                    break;
            }
        }
        return salaryUnit;
    }
}
