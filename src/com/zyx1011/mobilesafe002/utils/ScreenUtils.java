package com.zyx1011.mobilesafe002.utils;

import android.content.Context;

/**
 * 屏幕工具类
 * 
 * @author zhongyuxin
 */
public class ScreenUtils {

	/**
	 * 传入dp值,转换成px值,用于屏幕适配
	 * 
	 * @param dp
	 * @return
	 */
	public static int dp2px(Context context, int dp) {
		float density = context.getResources().getDisplayMetrics().density;
		int px = (int) (dp * density + 0.5f);
		return px;
	}
	
}
