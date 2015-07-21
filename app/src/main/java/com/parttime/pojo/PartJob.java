package com.parttime.pojo;

import java.io.Serializable;

/**
 * 兼职活动
 * Created by wyw on 2015/7/20.
 */
public class PartJob implements Serializable {
    public int id;
    public int companyId;
    public String type;
    public String title;
    public String beginTime;
    public String endTime;
    public String city;
    public String area;
    public String address;
    public int salary;
    public SalaryUnit salaryUnit;
    public String payType;
    public boolean apartSex;
    public int headSum;
    public int maleNum;
    public int femaleNum;
    public String workRequire;
    public boolean isShowTel;

    /* 更多要求里面的 */
    public Boolean healthProve;
    /**
     *  用、隔开
     */
    public String language;
    public Integer height;
    public Integer bust;
    public Integer beltline;
    public Integer hipline;

    /* ---- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartJob partJob = (PartJob) o;

        if (companyId != partJob.companyId) return false;
        if (id != partJob.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + companyId;
        return result;
    }


    @Override
    public String toString() {
        return "PartJob{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", salary=" + salary +
                ", salaryUnit=" + salaryUnit +
                ", payType='" + payType + '\'' +
                ", apartSex=" + apartSex +
                ", headSum=" + headSum +
                ", maleNum=" + maleNum +
                ", femaleNum=" + femaleNum +
                ", workRequire='" + workRequire + '\'' +
                ", isShowTel=" + isShowTel +
                ", healthProve=" + healthProve +
                ", language='" + language + '\'' +
                ", height=" + height +
                ", bust=" + bust +
                ", beltline=" + beltline +
                ", hipline=" + hipline +
                '}';
    }
}
