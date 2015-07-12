package com.quark.model;

import java.io.Serializable;

/**
 * 
 * @ClassName: PublishJianLi
 * @Description: 发布兼职
 * @author howe
 * @date 2015-1-21 下午3:09:15
 * 
 */
public class PublishJianzhi implements Serializable {
	private int activity_id;
	private int company_id;// Int 商家ID
	private int pay_money; // 支付费用：0-表示免费发布。
	private String title;// String 标题。默认Empty String
	private String start_time;// String 开始时间。默认'2015-01-01'
	private String end_time;// String 结束时间。默认'2015-01-01'
	private String time_tag;// String 时间标签。默认Empty String
	private String city;// String 城市：不带“市”。默认Empty String
	private String county;// String 区域：不带“区”。默认Empty String
	private String address;// String 具体位置。默认Empty String
	private int pay;// Int 薪酬。默认0
	private int pay_type;// Int 工资薪酬：0-日薪，1-时新。默认-1
	private String pay_form;// String 结算方式：不限、日结、周结、月结、完工结
	private int head_count;// Int 总人数.默认0
	private int male_count;// Int 男人数。默认-1
	private int female_count;// Int 女人数.默认0
	private int apart_sex;// Int 是否区分男女.默认-1 0不区分 1区分
	private int require_health_record; // 是否要求有健康证：0-不要求，1-要求.-1未知
	private String require_info;// String 工作要求及内容：10-200字。默认Empty String
	private int require_height;// Int 更多兼职要求：身高cm。默认-1
	private int require_bust; // Int 更多兼职要求：胸围。默认-1
	private int require_beltline;// Int 更多兼职要求：腰围。默认-1
	private int require_hipline;// Int 更多兼职要求：臀围。默认-1
	private String require_cloth_weight;// Int 衣服码：S．M．L．XL．XXL．XXXL.默认-1
	private int require_shoe_weigth;// Int 鞋码：33-45间.默认-1
	// private int require_shoe_weigth;// Int 鞋码：33-45间.默认-1
	private String type;// 兼职类型：全部，派发，促销，家教，服务员，礼仪，安保人员，模特，主持，翻译，其它
	private String require_language;//
	private String publish_time;//
	private String name;
	private int status; // 1-待审核，2-审核通过，3-审核不通过，4-取消活动
	private int confirmed_count; // 已确定人数
	private int uncheck_count; // 已确定人数
	private int view_count;// 浏览次数

	public int getView_count() {
		return view_count;
	}

	public void setView_count(int view_count) {
		this.view_count = view_count;
	}

	public int getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(int activity_id) {
		this.activity_id = activity_id;
	}

	public int getConfirmed_count() {
		return confirmed_count;
	}

	public void setConfirmed_count(int confirmed_count) {
		this.confirmed_count = confirmed_count;
	}

	public int getUncheck_count() {
		return uncheck_count;
	}

	public void setUncheck_count(int uncheck_count) {
		this.uncheck_count = uncheck_count;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCompany_id() {
		return company_id;
	}

	public void setCompany_id(int company_id) {
		this.company_id = company_id;
	}

	public int getPay_money() {
		return pay_money;
	}

	public void setPay_money(int pay_money) {
		this.pay_money = pay_money;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getTime_tag() {
		return time_tag;
	}

	public void setTime_tag(String time_tag) {
		this.time_tag = time_tag;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getPay_form() {
		return pay_form;
	}

	public void setPay_form(String pay_form) {
		this.pay_form = pay_form;
	}

	public int getHead_count() {
		return head_count;
	}

	public void setHead_count(int head_count) {
		this.head_count = head_count;
	}

	public int getMale_count() {
		return male_count;
	}

	public void setMale_count(int male_count) {
		this.male_count = male_count;
	}

	public int getFemale_count() {
		return female_count;
	}

	public void setFemale_count(int female_count) {
		this.female_count = female_count;
	}

	public int getApart_sex() {
		return apart_sex;
	}

	public void setApart_sex(int apart_sex) {
		this.apart_sex = apart_sex;
	}

	public int getRequire_health_record() {
		return require_health_record;
	}

	public void setRequire_health_record(int require_health_record) {
		this.require_health_record = require_health_record;
	}

	public String getRequire_info() {
		return require_info;
	}

	public void setRequire_info(String require_info) {
		this.require_info = require_info;
	}

	public int getRequire_height() {
		return require_height;
	}

	public void setRequire_height(int require_height) {
		this.require_height = require_height;
	}

	public int getRequire_bust() {
		return require_bust;
	}

	public void setRequire_bust(int require_bust) {
		this.require_bust = require_bust;
	}

	public int getRequire_beltline() {
		return require_beltline;
	}

	public void setRequire_beltline(int require_beltline) {
		this.require_beltline = require_beltline;
	}

	public int getRequire_hipline() {
		return require_hipline;
	}

	public void setRequire_hipline(int require_hipline) {
		this.require_hipline = require_hipline;
	}

	public String getRequire_cloth_weight() {
		return require_cloth_weight;
	}

	public void setRequire_cloth_weight(String require_cloth_weight) {
		this.require_cloth_weight = require_cloth_weight;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRequire_language() {
		return require_language;
	}

	public void setRequire_language(String require_language) {
		this.require_language = require_language;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public int getRequire_shoe_weigth() {
		return require_shoe_weigth;
	}

	public void setRequire_shoe_weigth(int require_shoe_weigth) {
		this.require_shoe_weigth = require_shoe_weigth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PublishJianzhi [activity_id=" + activity_id + ", company_id="
				+ company_id + ", pay_money=" + pay_money + ", title=" + title
				+ ", start_time=" + start_time + ", end_time=" + end_time
				+ ", time_tag=" + time_tag + ", city=" + city + ", county="
				+ county + ", address=" + address + ", pay=" + pay
				+ ", pay_type=" + pay_type + ", pay_form=" + pay_form
				+ ", head_count=" + head_count + ", male_count=" + male_count
				+ ", female_count=" + female_count + ", apart_sex=" + apart_sex
				+ ", require_health_record=" + require_health_record
				+ ", require_info=" + require_info + ", require_height="
				+ require_height + ", require_bust=" + require_bust
				+ ", require_beltline=" + require_beltline
				+ ", require_hipline=" + require_hipline
				+ ", require_cloth_weight=" + require_cloth_weight
				+ ", require_shoe_weigth=" + require_shoe_weigth + ", type="
				+ type + ", require_language=" + require_language
				+ ", publish_time=" + publish_time + ", name=" + name
				+ ", status=" + status + ", confirmed_count=" + confirmed_count
				+ ", uncheck_count=" + uncheck_count + "]";
	}

}
