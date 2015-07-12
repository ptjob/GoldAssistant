package com.quark.model;

import java.io.Serializable;

public class RosterCancel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int activity_id;//活动Id
	//Tips中字段说明
	private int user_id;//用户id
	private String title;//活动标题
	private String name;//用户名
	public int getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Roster [activity_id=" + activity_id + ", title=" + title
				+ ", user_id=" + user_id
				+ ", title=" + title + ", name=" + name + "]";
	}
	
}
