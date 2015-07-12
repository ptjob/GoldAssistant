package com.quark.model;

import java.io.Serializable;

public class SignPersonList implements Serializable {
	/**
	 * @author 商家端---签到人员
	 * @Email Royal.k.peng@gmail.com
	 */
	private static final long serialVersionUID = -719887608260606653L;
	private int user_id;
	private int sex;
	private String name;
	private String telephone;
	private int age;
	private int creditworthiness;
	private int is_commented;
	private int sign;
	private String picture_1;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
	public String getPicture_1() {
		return picture_1;
	}
	public void setPicture_1(String picture_1) {
		this.picture_1 = picture_1;
	}
	@Override
	public String toString() {
		return "SignPersonList [user_id=" + user_id + ", sex=" + sex
				+ ", name=" + name + ", telephone=" + telephone + ", age="
				+ age + ", creditworthiness=" + creditworthiness
				+ ", is_commented=" + is_commented + ", sign=" + sign
				+ ", picture_1=" + picture_1 + "]";
	}

	
}
