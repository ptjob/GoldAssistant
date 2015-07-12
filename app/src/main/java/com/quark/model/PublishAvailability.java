package com.quark.model;

/**
*
* @ClassName: PublishAvailability
* @Description: 	是否可以发布兼职  判断
* 说明： 1.余额不足：可以根据money值进行判断。
* 2.资料还未进行实名认证：可以根据total_count进行判断，若tota_count>1且certification=0.(商家注册可以在未实名认证条件下发布一条信息)。
* 3.到指定日期，未做人员评论：根据tobe_comment_activity_id ！=-1判断。
* 4.每日一条免费信息：根据free_count判断。
* 5：免费信息已发布完：free_count=0判断。
* @author howe
* @date 2015-1-20 下午8:37:46
*
*/
public class PublishAvailability {
	private int charge_count;//	Int	账户金额可用条数
	private int free_count;//	Int	账户免费可用的条数
	private int money;//	Int	账户金额
	private int total_count;//	Int	已经发布总条数
	private int certification;//	Int	是否认证通过，0-否，1-是
	private int tobe_comment_activity_id;//	Int	待评论活动ID。若ID为-1则表示无待评论活动
	private String tobe_comment_activity_title;//	String	待评论活动标题
	
	public int getCharge_count() {
		return charge_count;
	}
	public void setCharge_count(int charge_count) {
		this.charge_count = charge_count;
	}
	public int getFree_count() {
		return free_count;
	}
	public void setFree_count(int free_count) {
		this.free_count = free_count;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	public int getCertification() {
		return certification;
	}
	public void setCertification(int certification) {
		this.certification = certification;
	}
	public int getTobe_comment_activity_id() {
		return tobe_comment_activity_id;
	}
	public void setTobe_comment_activity_id(int tobe_comment_activity_id) {
		this.tobe_comment_activity_id = tobe_comment_activity_id;
	}
	public String getTobe_comment_activity_title() {
		return tobe_comment_activity_title;
	}
	public void setTobe_comment_activity_title(String tobe_comment_activity_title) {
		this.tobe_comment_activity_title = tobe_comment_activity_title;
	}

}
