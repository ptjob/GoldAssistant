package com.quark.model;

import java.io.Serializable;

import org.json.JSONObject;

public class QianDaoListBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;// 姓名
	private int sign_count;// 个人签到次数
	private JSONObject signMap;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSign_count() {
		return sign_count;
	}

	public void setSign_count(int sign_count) {
		this.sign_count = sign_count;
	}

	public JSONObject getSignMap() {
		return signMap;
	}

	public void setSignMap(JSONObject signMap) {
		this.signMap = signMap;
	}

	@Override
	public String toString() {
		return name + ":" + sign_count + ":" + signMap;
	}

}
