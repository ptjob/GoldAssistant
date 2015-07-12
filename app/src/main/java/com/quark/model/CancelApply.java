package com.quark.model;

import java.io.Serializable;

/**
 * 取消兼职model
 * @author cluo
 *
 */
public class CancelApply implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String note;
	private int sex;
	private String name;
	private String picture_1;
	private int earnest_money;
	private int age;
	private int certification;
	private int creditworthiness;
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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
	public String getPicture_1() {
		return picture_1;
	}
	public void setPicture_1(String picture_1) {
		this.picture_1 = picture_1;
	}
	public int getEarnest_money() {
		return earnest_money;
	}
	public void setEarnest_money(int earnest_money) {
		this.earnest_money = earnest_money;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getCertification() {
		return certification;
	}
	public void setCertification(int certification) {
		this.certification = certification;
	}
	
	public int getCreditworthiness() {
		return creditworthiness;
	}
	public void setCreditworthiness(int creditworthiness) {
		this.creditworthiness = creditworthiness;
	}
	@Override
	public String toString() {
		return "CancelApply [note=" + note + ", sex=" + sex + ", name=" + name
				+ ", picture_1=" + picture_1 + ", earnest_money="
				+ earnest_money + ", age=" + age + ", certification="
				+ certification + ", creditworthiness=" + creditworthiness
				+ "]";
	}
	
}
