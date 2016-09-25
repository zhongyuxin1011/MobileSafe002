package com.zyx1011.mobilesafe002.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 操作文件存储的工具类
 * 
 * @author zhongyuxin
 */
public class PreferenceUtils {

	/**
	 * 获取SharedPreferences对象
	 * 
	 * @param context
	 * @return
	 */
	private static SharedPreferences getSp(Context context) {
		return context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	/**
	 * 存储布尔值到文件
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		getSp(context).edit().putBoolean(key, value).commit();
	}

	/**
	 * 返回存储的布尔值
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		return getSp(context).getBoolean(key, defValue);
	}

	/**
	 * 返回对应键的布尔值
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Context context, String key) {
		return getBoolean(context, key, true); // 默认值改为true,默认开启
	}

	/**
	 * 存储字符串键值对
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putString(Context context, String key, String value) {
		getSp(context).edit().putString(key, value).commit();
	}

	/**
	 * 获取指定键的字符串值,如果没有返回默认值
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		return getSp(context).getString(key, defValue);
	}

	/**
	 * 获取指定键的字符串值
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getString(Context context, String key) {
		return getString(context, key, "");
	}

	/**
	 * 存储整型值
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putInt(Context context, String key, int value) {
		getSp(context).edit().putInt(key, value).commit();
	}

	/**
	 * 获取带默认值的整型值
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		return getSp(context).getInt(key, defValue);
	}

	/**
	 * 获取指定键的整型值
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static int getInt(Context context, String key) {
		return getInt(context, key, -1);
	}

}
