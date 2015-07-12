package com.quark.model;

import java.io.Serializable;

public class Roster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// facebookList中字段说明
	private int activity_id;// 活动Id
	private int head_count;// 总需人数
	private int confirmed_count;// 已确认人数
	private String title;// 活动标题

	// Tips中字段说明
	private int id;// id
	private String name;// 用户名
	private int user_id;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}

	public int getHead_count() {
		return head_count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHead_count(int head_count) {
		this.head_count = head_count;
	}

	public int getConfirmed_count() {
		return confirmed_count;
	}

	public void setConfirmed_count(int confirmed_count) {
		this.confirmed_count = confirmed_count;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Roster [activity_id=" + activity_id + ", head_count="
				+ head_count + ", confirmed_count=" + confirmed_count
				+ ", title=" + title + ", id=" + id + ", name=" + name
				+ ", user_id=" + user_id + "]";
	}


}
