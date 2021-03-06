package com.zyx1011.mobilesafe002.entity;

public class VersionInfo {
	private int versionCode; // 版本号
	private String versionName; // 版本名
	private String describe; // 描述

	/**
	 * 无参构造
	 */
	public VersionInfo() {
	}

	/**
	 * 有参构造
	 * 
	 * @param versionCode
	 * @param versionName
	 * @param describe
	 */
	public VersionInfo(int versionCode, String versionName, String describe) {
		this.versionCode = versionCode;
		this.versionName = versionName;
		this.describe = describe;
	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	public int getVersionCode() {
		return versionCode;
	}

	/**
	 * 设置版本号
	 * 
	 * @param versionCode
	 */
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	/**
	 * 获取版本名
	 * 
	 * @return
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * 设置版本名
	 * 
	 * @param versionName
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * 获取描述
	 * 
	 * @return
	 */
	public String getdescribe() {
		return describe;
	}

	/**
	 * 设置描述
	 * 
	 * @param describe
	 */
	public void setdescribe(String describe) {
		this.describe = describe;
	}

	@Override
	public String toString() {
		return "VersionInfo [versionCode=" + versionCode + ", versionName=" + versionName + ", describe=" + describe
				+ "]";
	}

}
