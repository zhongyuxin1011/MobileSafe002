package com.zyx1011.mobilesafe002.entity;

import android.graphics.drawable.Drawable;

public class ProcessInfo {

	private Drawable icon;
	private String processName;
	private long usedMem;
	private boolean isSystem;
	private boolean isChecked;
	private boolean isGodProcess;
	private String apkName;

	public ProcessInfo() {
	}

	public ProcessInfo(Drawable icon, String processName, long usedMem, boolean isSystem, boolean isChecked,
			boolean isGodProcess, String apkName) {
		this.icon = icon;
		this.processName = processName;
		this.usedMem = usedMem;
		this.isSystem = isSystem;
		this.isChecked = isChecked;
		this.isGodProcess = isGodProcess;
		this.apkName = apkName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public long getUsedMem() {
		return usedMem;
	}

	public void setUsedMem(long usedMem) {
		this.usedMem = usedMem;
	}

	public boolean isSystem() {
		return isSystem;
	}

	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public boolean isGodProcess() {
		return isGodProcess;
	}

	public void setGodProcess(boolean isGodProcess) {
		this.isGodProcess = isGodProcess;
	}

	@Override
	public String toString() {
		return "ProcessInfo [icon=" + icon + ", processName=" + processName + ", usedMem=" + usedMem + ", isSystem="
				+ isSystem + ", isChecked=" + isChecked + ", isGodProcess=" + isGodProcess + ", apkName=" + apkName
				+ "]";
	}

}
