package com.quark.model;

import java.io.Serializable;

public class BrokerBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int company_id;// id
	private String company_name;// 名字
	private String avatar;// 头像
	private int fans;// 粉丝数

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getFans() {
		return fans;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}

}
