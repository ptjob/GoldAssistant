package com.quark.model;

import java.io.Serializable;

/**
*
* @ClassName: HuanxingUserInfo
* @Description: 环信点击头像查看信息
* @author howe
* @date 2015-2-6 上午10:21:01
*
*/
public class HuanxingUserInfo implements Serializable {
	private  String avatar;//	String	环信用户头像
	private int sex;//	Int	-1-未知，0-女，1-男
	private  String name;//	String	姓名
	private  int creditworthiness;//	Int	信誉值：步长为：10为一个心，5为半个心
	private  int earnest_money;//	Int	诚意金,0-未交，1-已交
	private  int certification;//	Int	实名认证，0-未认证，1-已提交认证，2-认证通过,3-认证不通过
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCreditworthiness() {
		return creditworthiness;
	}
	public void setCreditworthiness(int creditworthiness) {
		this.creditworthiness = creditworthiness;
	}
	public int getEarnest_money() {
		return earnest_money;
	}
	public void setEarnest_money(int earnest_money) {
		this.earnest_money = earnest_money;
	}
	public int getCertification() {
		return certification;
	}
	public void setCertification(int certification) {
		this.certification = certification;
	}
	
	@Override
	public String toString() {
		return "HuanxingUserInfo [avatar=" + avatar + ", sex=" + sex
				+ ", name=" + name + ", creditworthiness=" + creditworthiness
				+ ", earnest_money=" + earnest_money + ", certification="
				+ certification + "]";
	}

}
