package com.quark.model;

import java.io.Serializable;

import org.json.JSONObject;

public class MyJianZhiModle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int activity_id;// Int 活动ID
	private String title;// String; 活动标题
	private String county;// String 活动区域
	private String start_time;// String 活动开始时间
	private int left_count;// Int 还差人数
	private int days;// Int 兼职天数
	private int pay;// Int 薪酬
	// carson
	private int pay_type;// Int 日薪(0),时薪(1)
	private String type;// Int 活动分类
	private String publish_time;//
	private int source;
	private int guarantee;// 保证金
	private int superJob;// 超级
	private int distance;// 距离
	private int apply; // 申请状态：0-未查看，1-已确认，2-拒绝(未通过)，3-已查看【未查看和已查看状态属于：待确定】

	// 签到记录显示
	private int all_count;// 发起签到次数
	private JSONObject signMap;

	public int getApply() {
		return apply;
	}

	public void setApply(int apply) {
		this.apply = apply;
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

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public int getLeft_count() {
		return left_count;
	}

	public void setLeft_count(int left_count) {
		this.left_count = left_count;
	}

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public int getPay() {
		return pay;
	}

	public void setPay(int pay) {
		this.pay = pay;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getGuarantee() {
		return guarantee;
	}

	public void setGuarantee(int guarantee) {
		this.guarantee = guarantee;
	}

	public int getSuperJob() {
		return superJob;
	}

	public void setSuperJob(int superJob) {
		this.superJob = superJob;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getAll_count() {
		return all_count;
	}

	public void setAll_count(int all_count) {
		this.all_count = all_count;
	}

	public JSONObject getSignMap() {
		return signMap;
	}

	public void setSignMap(JSONObject signMap) {
		this.signMap = signMap;
	}

}
