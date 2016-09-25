package com.zyx1011.mobilesafe002.entity;

import android.graphics.drawable.Drawable;

public class FlowInfo implements Comparable<FlowInfo> {

	public String packageName;
	public String name;
	public Drawable icon;
	public int uid;
	public long rcv;
	public long snd;

	@Override
	public String toString() {
		return "FlowInfo [packageName=" + packageName + ", name=" + name + ", icon=" + icon + ", uid=" + uid + ", rcv="
				+ rcv + ", snd=" + snd + "]";
	}

	@Override
	public int compareTo(FlowInfo another) {
		int a = (int) (another.rcv - this.rcv); // 以接收数为主
		int b = a == 0 ? (int) (another.snd - this.snd) : a; // 以发送数为辅
		return b;
	}

}