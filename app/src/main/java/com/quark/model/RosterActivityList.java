package com.quark.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 花名册--活动人员列表
 * 
 * @author C罗
 * 
 */
public class RosterActivityList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int female;
	private int male;
	private List<RosterUser> rosterUserList = new ArrayList<RosterUser>();

	public int getFemale() {
		return female;
	}

	public void setFemale(int female) {
		this.female = female;
	}

	public int getMale() {
		return male;
	}

	public void setMale(int male) {
		this.male = male;
	}

	public List<RosterUser> getRosterUserList() {
		return rosterUserList;
	}

	public void setRosterUserList(List<RosterUser> rosterUserList) {
		this.rosterUserList = rosterUserList;
	}

	/*
	 * public static RosterActivityList getRosterList(String data){
	 * RosterActivityList rosterList = new RosterActivityList(); try {
	 * JSONObject js = new JSONObject(data); JSONObject jss =
	 * js.getJSONObject("ActivityFaceBook"); JSONArray jsss =
	 * jss.getJSONArray("list"); if(jsss.length()>0){ for(int
	 * i=0;i<jsss.length();i++){ RosterUser rosterUser = new RosterUser();
	 * JSONObject userObject = (JSONObject) jsss.opt(i);
	 * rosterUser.setIs_commented(userObject.getInt("is_commented"));
	 * rosterUser.setUser_id(userObject.getInt("user_id"));
	 * rosterUser.setAge(userObject.getInt("age"));
	 * rosterUser.setCreditworthiness(userObject.getInt("creditworthiness"));
	 * rosterUser.setName(userObject.getString("name"));
	 * rosterUser.setSex(userObject.getInt("sex"));
	 * rosterUser.setTelephone(userObject.getString("telephone"));
	 * rosterUser.setPicture_1(userObject.getString("picture_1"));
	 * rosterList.getRosterUserList().add(rosterUser); } }else{ return
	 * rosterList; } return rosterList; } catch (JSONException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); return rosterList; }
	 */
	// }
	@Override
	public String toString() {
		return "RosterActivityList [female=" + female + ", male=" + male
				+ ", rosterUserList=" + rosterUserList + "]";
	}

}
