package com.quark.model;

import java.io.Serializable;

public class Function implements Serializable {
	/**
	 * @author 商家端---我
	 */
	private static final long serialVersionUID = -719887608260606653L;
	private int money;
	private String name;
	private String avatar;
	private int status;
    private int company_status;
    private int point;
    private int followers;

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

    public int getCompany_status() {
        return company_status;
    }

    public void setCompany_status(int company_status) {
        this.company_status = company_status;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    @Override
	public String toString() {
		return "Function [money=" + money + ", name=" + name + ", avatar="
				+ avatar + ", status=" + status + "]";
	}

}
