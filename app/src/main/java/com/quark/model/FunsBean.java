package com.quark.model;

import java.io.Serializable;

public class FunsBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int user_id;// Int 用户ID
	private String user_name;// String 用户名
	private String picture_1;// String 用户头像
	private int sex;// Int 性别：1-男，0-女，-1-未知
	private int age;// Int 年龄
	private int creditworthiness;// Int 信誉值：5为半个心，10为一个心
	private int earnest_money;// Int 诚意金：0-未交，1-已交
	private int certification;// Int 认证：0-未认证，1-已认证

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPicture_1() {
		return picture_1;
	}

	public void setPicture_1(String picture_1) {
		this.picture_1 = picture_1;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
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

}
