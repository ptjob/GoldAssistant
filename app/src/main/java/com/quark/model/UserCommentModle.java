package com.quark.model;

import java.io.Serializable;

public class UserCommentModle implements Serializable {
	/**
	 * @author 我的简历预览 兼职评语
	 * 
	 */
	private String remark;
	private String comment;
	private String title;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "UserCommentModle [remark=" + remark + ", comment=" + comment
				+ ", title=" + title + "]";
	}

}
