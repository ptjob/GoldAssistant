package com.quark.model;

public class AuthenticationResponse{
	   //真实姓名
	   public String name;
	   //身份证号
	   public String identity;
	   //正面照片
	   public String identity_front;
	   //用户正面图片 错误的单词
	   public String identity_font;
	   //背面照片
	   public String identity_verso;
	 //实名认证，0-未认证，1-已提交认证，2-认证通过,3-认证不通过
	   public int certification;
	   
	   public void setName(String name){
	     this.name = name;
	   }
	   public String getName(){
	     return this.name;
	   }
	   public void setIdentity(String identity){
	     this.identity = identity;
	   }
	   public String getIdentity(){
	     return this.identity;
	   }
	   
	   public String getIdentity_front() {
		return identity_front;
	}
	public void setIdentity_front(String identity_front) {
		this.identity_front = identity_front;
	}
	public void setIdentity_verso(String identity_verso){
	     this.identity_verso = identity_verso;
	   }
	   public String getIdentity_verso(){
	     return this.identity_verso;
	   }
	public int getCertification() {
		return certification;
	}
	public void setCertification(int certification) {
		this.certification = certification;
	}
	public String getIdentity_font() {
		return identity_font;
	}
	public void setIdentity_font(String identity_font) {
		this.identity_font = identity_font;
	}
	
	@Override
	public String toString() {
		return "AuthenticationResponse [name=" + name + ", identity="
				+ identity + ", identity_front=" + identity_front
				+ ", identity_font=" + identity_font + ", identity_verso="
				+ identity_verso + ", certification=" + certification + "]";
	}
	   
	}