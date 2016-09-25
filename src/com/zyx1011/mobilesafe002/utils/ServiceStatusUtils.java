package com.zyx1011.mobilesafe002.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;

/**
 * 操作服务的工具类
 * 
 * @author zhongyuxin
 */
public class ServiceStatusUtils {

	/**
	 * 判断指定的服务是否在运行
	 * 
	 * @param context
	 * @param clazz
	 * @return
	 */
	public static boolean isRunningService(Context context, Class<? extends Service> clazz) {
		// 获取ActivityManager实例
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取所有正在运行的服务
		List<RunningServiceInfo> list = am.getRunningServices(Integer.MAX_VALUE);

		if (list != null) {
			for (RunningServiceInfo serviceInfo : list) {
				ComponentName service = serviceInfo.service;
				
				if (clazz.getName().equals(service.getClassName())) {
					return true;
				}
			}
		}
		return false;
	}
}
