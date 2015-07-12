package com.quark.model;

public class LoginResponse{
	   //商家id
	   public int user_id;
	   //商家Token
	   public String token;
	   //环信商家名
	   public String IM_USERID;
	   //环信商家密码
	   public String IM_PASSWORD;
	   //环信商家昵称
	   public String IM_NIKENAME;
	   //环信商家头像
	   public String IM_AVATAR;
	   
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getIM_USERID() {
		return IM_USERID;
	}
	public void setIM_USERID(String iM_USERID) {
		IM_USERID = iM_USERID;
	}
	public String getIM_PASSWORD() {
		return IM_PASSWORD;
	}
	public void setIM_PASSWORD(String iM_PASSWORD) {
		IM_PASSWORD = iM_PASSWORD;
	}
	public String getIM_NIKENAME() {
		return IM_NIKENAME;
	}
	public void setIM_NIKENAME(String iM_NIKENAME) {
		IM_NIKENAME = iM_NIKENAME;
	}
	public String getIM_AVATAR() {
		return IM_AVATAR;
	}
	public void setIM_AVATAR(String iM_AVATAR) {
		IM_AVATAR = iM_AVATAR;
	}

	   
	}