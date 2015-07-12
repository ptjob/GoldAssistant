package com.quark.model;

import java.io.Serializable;

/**
*
* @ClassName: RosterModel
* @Description: 花名册 签到model
* @author howe
* @date 2015-2-7 上午9:04:59
*
*/
public class RosterModel implements Serializable {

	private int user_id;//	Int	用户ID
	private int sex;//	Int	0-女，1-男，-1-未知
	private String telephone;//	String	     电话号码
	private int creditworthiness;//	Int	信誉值:步长为5,半个心
	private int is_commented;//	Int	0-未评论，1-已评论
	private int sign;//	Int	0-未签到，1-签到
	private String name;//	String	姓名
	private String picture_1;//	String	头像
	private int age;//	Int	年龄
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public int getCreditworthiness() {
		return creditworthiness;
	}
	public void setCreditworthiness(int creditworthiness) {
		this.creditworthiness = creditworthiness;
	}
	public int getIs_commented() {
		return is_commented;
	}
	public void setIs_commented(int is_commented) {
		this.is_commented = is_commented;
	}
	public int getSign() {
		return sign;
	}
	public void setSign(int sign) {
		this.sign = sign;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPicture_1() {
		return picture_1;
	}
	public void setPicture_1(String picture_1) {
		this.picture_1 = picture_1;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "RosterModel [user_id=" + user_id + ", sex=" + sex
				+ ", telephone=" + telephone + ", creditworthiness="
				+ creditworthiness + ", is_commented=" + is_commented
				+ ", sign=" + sign + ", name=" + name + ", picture_1="
				+ picture_1 + ", age=" + age + "]";
	}

}
