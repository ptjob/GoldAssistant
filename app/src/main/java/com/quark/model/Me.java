package com.quark.model;

import java.io.Serializable;

public class Me implements Serializable {
	/**
	 * @author 用户端---我
	 * @Email Royal.k.peng@gmail.com
	 */
	private static final long serialVersionUID = -719887608260606653L;
	private int comment_count;// 对应兼职成就小红点
	private int activity_count;// 对应我的兼职小红点
	private int sophistication;
	private int sex;// -1未知 0女 其它 男
	private String name;
	private String picture_1;
	private int age;
	private int earnest_money;// 诚意金
	private int certification;
	private int creditworthiness;
	private String money;// 存款
	private String telephone;// 手机号码

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public int getActivity_count() {
		return activity_count;
	}

	public void setActivity_count(int activity_count) {
		this.activity_count = activity_count;
	}

	public int getSophistication() {
		return sophistication;
	}

	public void setSophistication(int sophistication) {
		this.sophistication = sophistication;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
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

	public int getCreditworthiness() {
		return creditworthiness;
	}

	public void setCreditworthiness(int creditworthiness) {
		this.creditworthiness = creditworthiness;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Me [comment_count=" + comment_count + ", activity_count="
				+ activity_count + ", sophistication=" + sophistication
				+ ", sex=" + sex + ", name=" + name + ", picture_1="
				+ picture_1 + ", age=" + age + ", earnest_money="
				+ earnest_money + ", certification=" + certification
				+ ", creditworthiness=" + creditworthiness + "]";
	}

}
