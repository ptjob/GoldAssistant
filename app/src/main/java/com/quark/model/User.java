package com.quark.model;

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * @author Royal
	 * @Email Royal.k.peng@gmail.com
	 */
	private static final long serialVersionUID = -719887608260606653L;
	private int id;
	private int count;
	private int point;
	private String avatar;
	private String telephone;
    private int voucherCount;
    private String recommend_code;
    private String address;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

    public int getVoucherCount() {
        return voucherCount;
    }

    public void setVoucherCount(int voucherCount) {
        this.voucherCount = voucherCount;
    }

    public String getRecommend_code() {
        return recommend_code;
    }

    public void setRecommend_code(String recommend_code) {
        this.recommend_code = recommend_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", count=" + count +
                ", point=" + point +
                ", avatar='" + avatar + '\'' +
                ", telephone='" + telephone + '\'' +
                ", voucherCount=" + voucherCount +
                ", recommend_code='" + recommend_code + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

}
