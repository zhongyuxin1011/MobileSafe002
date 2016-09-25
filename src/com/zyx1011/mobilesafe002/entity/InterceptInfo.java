package com.zyx1011.mobilesafe002.entity;

import java.io.Serializable;

public class InterceptInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String phone;
	private int type;

	public InterceptInfo() {
	}

	public InterceptInfo(int id, String phone, int type) {
		this.id = id;
		this.phone = phone;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "InterceptInfo [id=" + id + ", phone=" + phone + ", type=" + type + "]";
	}

}