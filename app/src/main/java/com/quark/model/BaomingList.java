package com.quark.model;

import java.io.Serializable;

/**
 * 
 * @ClassName: BaomingList
 * @Description: 管理 详细 查看报名
 * @author howe
 * @date 2015-1-29 上午10:19:59
 * 
 */
public class BaomingList implements Serializable {

	private int user_id;// Int 用户ID
	private String name;// String 用户名
	private String picture_1;// String 用户头像
	private int Sex;// Int 性别：1-男，0-女，-1-未知
	private int age;// Int 年龄
	private int creditworthiness;// Int 信誉值：5为半个心，10为一个心
	private int earnest_money;// Int 诚意金：0-未交，1-已交
	private int certification;// Int 认证：0-未认证，1-已认证
	private String note;// String 兼容留言
	private int apply; // apply:申请状态：0-未查看，1-已确认，2-拒绝(未通过)，3-已查看

	public int getApply() {
		return apply;
	}

	public void setApply(int apply) {
		this.apply = apply;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
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

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "BaomingList [user_id=" + user_id + ", name=" + name
				+ ", picture_1=" + picture_1 + ", Sex=" + Sex + ", age=" + age
				+ ", creditworthiness=" + creditworthiness + ", earnest_money="
				+ earnest_money + ", certification=" + certification
				+ ", note=" + note + "]";
	}

}
