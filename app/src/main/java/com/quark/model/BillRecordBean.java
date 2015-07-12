package com.quark.model;

import java.io.Serializable;

public class BillRecordBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String bank;// 开户银行
	private String bank_branch;// 开户支行
	private String acount_name;// 账户名
	private String account_num;// 开户帐号
	private int type;// 账单类型 1提现2充值
	private String title;// 账单标题
	private String money;// 金额
	private String bill_flow;// 账单流水号:QM+YYYYMMDD+00001
	private String out_flow;// 支付宝或者银行卡的流水号
	private int pay_type;// 支付方式：1-支付宝 2-银行卡 3-钱包余额
	private String post_time;// 账单生成日期
	private String remark;// 备注
	private int status;// 状态 0-删除 1-处理中 2-成功 3-失败

	public BillRecordBean() {
	}

	public BillRecordBean(String bank, String bank_branch, String acount_name,
			String account_num, int type, String title, String money,
			String bill_flow, String out_flow, int pay_type, String post_time,
			String remark, int status) {
		this.bank = bank;
		this.bank_branch = bank_branch;
		this.acount_name = acount_name;
		this.account_num = account_num;
		this.type = type;
		this.title = title;
		this.money = money;
		this.bill_flow = bill_flow;
		this.out_flow = out_flow;
		this.pay_type = pay_type;
		this.post_time = post_time;
		this.remark = remark;
		this.status = status;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBank_branch() {
		return bank_branch;
	}

	public void setBank_branch(String bank_branch) {
		this.bank_branch = bank_branch;
	}

	public String getAcount_name() {
		return acount_name;
	}

	public void setAcount_name(String acount_name) {
		this.acount_name = acount_name;
	}

	public String getAccount_num() {
		return account_num;
	}

	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getBill_flow() {
		return bill_flow;
	}

	public void setBill_flow(String bill_flow) {
		this.bill_flow = bill_flow;
	}

	public String getOut_flow() {
		return out_flow;
	}

	public void setOut_flow(String out_flow) {
		this.out_flow = out_flow;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public String getPost_time() {
		return post_time;
	}

	public void setPost_time(String post_time) {
		this.post_time = post_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
