package com.quark.model;

import java.io.Serializable;

public class JiedanBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;// 活动类型
	private String title;// 活动标题
	private String create_time;// 发布时间
	private String contacts;// 商家电话
	private String content;// 兼职描述

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
