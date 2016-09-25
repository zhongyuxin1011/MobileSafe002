package com.zyx1011.mobilesafe002.entity;

import android.graphics.drawable.Drawable;

public class CacheInfo {
	public String name;
	public String packageName;
	public Drawable icon;
	public long cacheSize;

	@Override
	public String toString() {
		return "CacheInfo [name=" + name + ", packageName=" + packageName + ", icon=" + icon + ", cacheSize="
				+ cacheSize + "]";
	}

}
