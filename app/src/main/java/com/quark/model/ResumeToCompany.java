package com.quark.model;

import java.io.Serializable;

/**
*
* @ClassName: ResumeToCompany
* @Description: 商家 查看申请人详细信息
* @author howe
* @date 2015-1-29 下午9:11:14
*
*/
public class ResumeToCompany  implements Serializable{
		//用户id
	   public int user_id;
	   //正面近脸
	   public String picture_1;
	   //正面半身
	   public String picture_2;
	   //正面全身
	   public String picture_3;
	   //任意个照第1张
	   public String picture_4;
	   //任意个照第2张
	   public String picture_5;
	   //任意个照第3张
	   public String picture_6;
	   //姓名
	   public String name;
	   //性别:0-女，1-男
	   public int sex;
	   //生日
	   public String birthdate;
	   //身高
	   public int height;
	   //毕业于
	   public String graduate;
	   //学历
	   public String education;
//	   public String require_bbh;
	   //是否有健康证：0-否，1-是
	   public int health_record;
	   //衣服码
	   public String cloth_weight;
	   //鞋码
	   public int shoe_weight;
	   //擅长语言
	   public String language;
	   //经历简述
	   public String summary;
	   String bbh;//三围
	   
	   int head_count;//	Int	总共需要人数
	   int checked_count;//	Int	已经确认人数
	   String note;//	String	兼职留言
	   int apply;//	Int	申请状态：0-未查看，1-已确认，2-拒绝(未通过)，3-已查看

	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public void setPicture_1(String picture_1){
	     this.picture_1 = picture_1;
	   }
	   public String getPicture_1(){
	     return this.picture_1;
	   }
	   public void setPicture_2(String picture_2){
	     this.picture_2 = picture_2;
	   }
	   public String getPicture_2(){
	     return this.picture_2;
	   }
	   public void setPicture_3(String picture_3){
	     this.picture_3 = picture_3;
	   }
	   public String getPicture_3(){
	     return this.picture_3;
	   }
	   public void setPicture_4(String picture_4){
	     this.picture_4 = picture_4;
	   }
	   public String getPicture_4(){
	     return this.picture_4;
	   }
	   public void setPicture_5(String picture_5){
	     this.picture_5 = picture_5;
	   }
	   public String getPicture_5(){
	     return this.picture_5;
	   }
	   public void setPicture_6(String picture_6){
	     this.picture_6 = picture_6;
	   }
	   public String getPicture_6(){
	     return this.picture_6;
	   }
	   public void setName(String name){
	     this.name = name;
	   }
	   public String getName(){
	     return this.name;
	   }
	   public void setSex(int sex){
	     this.sex = sex;
	   }
	   public int getSex(){
	     return this.sex;
	   }
	   public void setBirthdate(String birthdate){
	     this.birthdate = birthdate;
	   }
	   public String getBirthdate(){
	     return this.birthdate;
	   }
	   public void setHeight(int height){
	     this.height = height;
	   }
	   public int getHeight(){
	     return this.height;
	   }
	   public void setGraduate(String graduate){
	     this.graduate = graduate;
	   }
	   public String getGraduate(){
	     return this.graduate;
	   }
	   public void setEducation(String education){
	     this.education = education;
	   }
	   public String getEducation(){
	     return this.education;
	   }
	   
	   
//	   public String getRequire_bbh() {
//		return require_bbh;
//	}
//	public void setRequire_bbh(String require_bbh) {
//		this.require_bbh = require_bbh;
//	}
	public void setHealth_record(int health_record){
	     this.health_record = health_record;
	   }
	   public int getHealth_record(){
	     return this.health_record;
	   }
	   public String getCloth_weight() {
		return cloth_weight;
	}
	public void setCloth_weight(String cloth_weight) {
		this.cloth_weight = cloth_weight;
	}
	public void setShoe_weight(int shoe_weight){
	     this.shoe_weight = shoe_weight;
	   }
	   public int getShoe_weight(){
	     return this.shoe_weight;
	   }
	   public void setLanguage(String language){
	     this.language = language;
	   }
	   public String getLanguage(){
	     return this.language;
	   }
	   public void setSummary(String summary){
	     this.summary = summary;
	   }
	   public String getSummary(){
	     return this.summary;
	   }
	public String getBbh() {
		return bbh;
	}
	public void setBbh(String bbh) {
		this.bbh = bbh;
	}
	public int getHead_count() {
		return head_count;
	}
	public void setHead_count(int head_count) {
		this.head_count = head_count;
	}
	public int getChecked_count() {
		return checked_count;
	}
	public void setChecked_count(int checked_count) {
		this.checked_count = checked_count;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getApply() {
		return apply;
	}
	public void setApply(int apply) {
		this.apply = apply;
	}
	@Override
	public String toString() {
		return "ResumeToCompany [user_id=" + user_id + ", picture_1="
				+ picture_1 + ", picture_2=" + picture_2 + ", picture_3="
				+ picture_3 + ", picture_4=" + picture_4 + ", picture_5="
				+ picture_5 + ", picture_6=" + picture_6 + ", name=" + name
				+ ", sex=" + sex + ", birthdate=" + birthdate + ", height="
				+ height + ", graduate=" + graduate + ", education="
				+ education + ", health_record=" + health_record
				+ ", cloth_weight=" + cloth_weight + ", shoe_weight="
				+ shoe_weight + ", language=" + language + ", summary="
				+ summary + ", bbh=" + bbh + ", head_count=" + head_count
				+ ", checked_count=" + checked_count + ", note=" + note
				+ ", apply=" + apply + "]";
	}
	
  }