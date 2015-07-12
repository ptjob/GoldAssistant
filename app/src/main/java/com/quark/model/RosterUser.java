package com.quark.model;

import java.io.Serializable;

public class RosterUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int user_id;
	private int sex;
	private String name;
	private String telephone;
	private int age;
	private int creditworthiness;
	private int is_commented;
	private String picture_1;
	private String comment;
	private int status;// 评价列表新增字段 0-取消 1-正常 2-放飞机
	private int ableComment;// 是否可以被评论，活动开始前不让评价

	public int getAbleComment() {
		return ableComment;
	}

	public void setAbleComment(int ableComment) {
		this.ableComment = ableComment;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

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

	public String getPicture_1() {
		return picture_1;
	}

	public void setPicture_1(String picture_1) {
		this.picture_1 = picture_1;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "RosterUser [user_id=" + user_id + ", sex=" + sex + ", name="
				+ name + ", telephone=" + telephone + ", age=" + age
				+ ", creditworthiness=" + creditworthiness + ", is_commented="
				+ is_commented + ", picture_1=" + picture_1 + "]";
	}

}
