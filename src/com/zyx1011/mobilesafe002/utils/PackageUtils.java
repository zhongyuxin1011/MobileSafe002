package com.zyx1011.mobilesafe002.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * Package工具类,用于获取版本名/版本号等信息
 * 
 * @author zhongyuxin
 */
public class PackageUtils {

	/**
	 * 传入Context实例,返回PackageInfo对象
	 * 
	 * @param context
	 * @return
	 * @throws NameNotFoundException
	 */
	public static PackageInfo getPackageInfo(Context context) throws NameNotFoundException {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0); // 0代表获取所有信息
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 传入Context实例,返回给实例中的版本号
	 * 
	 * @param context
	 * @return
	 * @throws NameNotFoundException
	 */
	public static int getVersionCode(Context context) throws NameNotFoundException {
		try {
			return getPackageInfo(context).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 传入Context实例,返回改实例中的版本名
	 * 
	 * @param context
	 * @return
	 * @throws NameNotFoundException
	 */
	public static String getVersionName(Context context) throws NameNotFoundException {
		try {
			return getPackageInfo(context).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			throw e;
		}
	}
}
