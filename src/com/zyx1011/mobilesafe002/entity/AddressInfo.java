package com.zyx1011.mobilesafe002.entity;

/**
 * 归属地查询信息实体类
 * 
 * @author zhongyuxin
 */
public class AddressInfo {
	private int id;
	private String mobileprefix;
	private String area;
	private String city;
	private String cardtype;

	public AddressInfo() {
	}

	public AddressInfo(int id, String mobileprefix, String area, String city, String cardtype) {
		this.id = id;
		this.mobileprefix = mobileprefix;
		this.area = area;
		this.city = city;
		this.cardtype = cardtype;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobileprefix() {
		return mobileprefix;
	}

	public void setMobileprefix(String mobileprefix) {
		this.mobileprefix = mobileprefix;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	@Override
	public String toString() {
		return city + "\n" + cardtype; // 返回需要的字符串格式
	}

}
