package com.quark.model;

import java.io.Serializable;

/**
*
* @ClassName: MyAchievementModel
* @Description: 我的兼职成就
* @author howe
* @date 2015-2-4 下午4:59:55
*
*/
public class MyAchievementModel implements Serializable{
	private String  remark;//	String	评语
	private String comment;//	String	评价
	private String type;//	String	活动分类
	private String title;//	String	    活动标题
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
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
	
	@Override
	public String toString() {
		return "MyAchievementModel [remark=" + remark + ", comment=" + comment
				+ ", type=" + type + ", title=" + title + "]";
	}
}
