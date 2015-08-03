package com.parttime.publish.vo;

import android.content.res.Resources;

import com.parttime.pojo.SalaryUnit;
import com.parttime.publish.LabelUtils;
import com.parttime.utils.TimeUtils;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;

import java.text.ParseException;
import java.util.Date;

/**
 * 兼职广场列表VO
 * Created by wyw on 2015/8/3.
 */
public class JobPlazaListVo {
    public int jobId;
    public String type;
    public int typeDrawableId;
    public String jobTitle;
    public boolean isSuper;
    public boolean isGuarantee;
    public boolean isTime;
    public String time;
    public String area;
    public String salary;
    public boolean isExpedited;

    public static class Convertor {
        public static String convertTime(String now, String publishTime) {
            try {
                return TimeUtils.getJobPlazaFormatTime(now, publishTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "";
        }

        public static String convertSalary(int salaryUnit, int salary) {
            SalaryUnit parse = SalaryUnit.parse(salaryUnit);
            return LabelUtils.getSalary(ApplicationControl.getInstance(), parse, salary);
        }

        public static int convertTypeDrawableId(String type) {
//            <string-array name="work_types">
//            <item>派发</item>
//            <item>促销</item>
//            <item>礼仪</item>
//            <item>家教</item>
//            <item>服务员</item>
//            <item>主持</item>
//            <item>安保人员</item>
//            <item>模特</item>
//            <item>话务员</item>
//            <item>翻译</item>
//            <item>工作人员</item>
//            <item>访谈</item>
//            <item>充场</item>
//            <item>演艺</item>
//            <item>其他</item>
//            </string-array>

            try {
                String[] workTypes = ApplicationControl.getInstance().getResources().getStringArray(R.array.work_types);
                if (type.equals(workTypes[0])) {
                    return R.drawable.tag_paifa;
                } else if (type.equals(workTypes[1])) {
                    return R.drawable.tag_cuxiao;
                } else if (type.equals(workTypes[2])) {
                    return R.drawable.tag_liyi;
                } else if (type.equals(workTypes[3])) {
                    return R.drawable.tag_jiajiao;
                } else if (type.equals(workTypes[4])) {
                    return R.drawable.tag_fuwuyuan;
                } else if (type.equals(workTypes[5])) {
                    return R.drawable.tag_zhuchi;
                } else if (type.equals(workTypes[6])) {
                    return R.drawable.tag_anbaorenyuan;
                } else if (type.equals(workTypes[7])) {
                    return R.drawable.tag_mote;
                } else if (type.equals(workTypes[8])) {
                    return R.drawable.tag_huawuyuan;
                } else if (type.equals(workTypes[9])) {
                    return R.drawable.tag_fanyi;
                } else if (type.equals(workTypes[10])) {
                    return R.drawable.tag_gongzuorenyuan;
                } else if (type.equals(workTypes[11])) {
                    return R.drawable.tag_fangtan;
                } else if (type.equals(workTypes[12])) {
                    return R.drawable.tag_chongchang;
                } else if (type.equals(workTypes[13])) {
                    return R.drawable.tag_yanyi;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return R.drawable.tag_qita;
        }
    }
}
