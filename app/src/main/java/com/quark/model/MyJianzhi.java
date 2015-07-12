package com.quark.model;

import java.io.Serializable;

import org.json.JSONArray;

/**
 * 
 * @ClassName: MyJianzhi
 * @Description: 我的兼职列表
 * @author howe
 * @date 2015-1-22 下午8:18:39
 * 
 */
public class MyJianzhi implements Serializable {
	private int activity_id;// Int 活动ID
	private String title;// 活动标题
	private int head_count;// int 活动需要总人数
	private int confirmed_count;// int 活动已确认人数
	private int uncheck_count;// Int 未查看总数
	private String publish_time;// String 发布日期
	private int status;// Int 活动状态：1-待审核，2-审核通过，3-审核不通过，4-取消活动
	private int view_count;// 浏览次数
	private String require_info;// 工作要求、简介
	private JSONArray apply_list;// 报名人员头像及uid
	private String county;// 地区
	private int female_count;// 女性限制
	private int male_count;// 男性限制
	private String now;// 当前网络时间
	private int activity_status;// 是否是取缔活动状态

	public int getActivity_status() {
		return activity_status;
	}

	public void setActivity_status(int activity_status) {
		this.activity_status = activity_status;
	}

	public String getNow() {
		return now;
	}

	public void setNow(String now) {
		this.now = now;
	}

	public String getRequire_info() {
		return require_info;
	}

	public void setRequire_info(String require_info) {
		this.require_info = require_info;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public int getView_count() {
		return view_count;
	}

	public void setView_count(int view_count) {
		this.view_count = view_count;
	}

	public JSONArray getApply_list() {
		return apply_list;
	}

	public void setApply_list(JSONArray apply_list) {
		this.apply_list = apply_list;
	}

	public int getFemale_count() {
		return female_count;
	}

	public void setFemale_count(int female_count) {
		this.female_count = female_count;
	}

	public int getMale_count() {
		return male_count;
	}

	public void setMale_count(int male_count) {
		this.male_count = male_count;
	}

	public int getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getHead_count() {
		return head_count;
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

	public int getUncheck_count() {
		return uncheck_count;
	}

	public void setUncheck_count(int uncheck_count) {
		this.uncheck_count = uncheck_count;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "MyJianzhi [activity_id=" + activity_id + ", title=" + title
				+ ", head_count=" + head_count + ", confirmed_count="
				+ confirmed_count + ", uncheck_count=" + uncheck_count
				+ ", publish_time=" + publish_time + ", status=" + status + "]";
	}

}