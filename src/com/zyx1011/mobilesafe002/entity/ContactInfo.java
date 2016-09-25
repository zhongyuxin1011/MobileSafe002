package com.zyx1011.mobilesafe002.entity;

public class ContactInfo {
	private int id;
	private String name;
	private String phone;

	public ContactInfo() {
	}

	public ContactInfo(int id, String name, String phone) {
		this.id = id;
		this.name = name;
		this.phone = phone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "ContactInfo [id=" + id + ", name=" + name + ", phone=" + phone + "]";
	}

}
