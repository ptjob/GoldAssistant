package com.parttime.pojo;

import java.io.Serializable;

/**
 * 兼职活动
 * Created by wyw on 2015/7/20.
 */
public class PartJob implements Serializable {
    public int id;
    public int companyId;
    public String companyName;
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
    public JobAuthType jobAuthType;
    public int viewCount;
    public int handCount;
    public boolean isStart;


    /* 更多要求里面的 */
    /**
     * 是否有健康证
     */
    public Boolean healthProve;
    /**
     *  用、隔开
     */
    public String language;
    /**
     * 身高
     */
    public Integer height;
    /**
     * 胸围
     */
    public Integer bust;
    /**
     * 腰围
     */
    public Integer beltline;
    /**
     * 臀围
     */
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
                ", companyName=" + companyName +
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

    public boolean isHasMeasurements() {
        if (bust != null && beltline != null && hipline != null) {
            return true;
        }
        return false;
    }

    public boolean isHasMoreRequire() {
        if (height != null || isHasMeasurements() || healthProve != null) {
            return true;
        }
        return false;
    }
}
