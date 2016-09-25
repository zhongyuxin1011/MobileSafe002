package com.zyx1011.mobilesafe002.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.zyx1011.mobilesafe002.entity.VersionInfo;

/**
 * JSON工具类,用于解析JSON字符串
 * 
 * @author zhongyuxin
 */
public class JSONUtils {

	/**
	 * 传入字符串,返回封装好的Bean类实例
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
	public static VersionInfo getVersionInfo(String json) throws JSONException {
		try {
			VersionInfo vi = new VersionInfo();

			JSONObject jo = new JSONObject(json); // 创建JSONObeject实例
			vi.setVersionCode(jo.getInt("versionCode")); // 获取对应的值并设置到Bean对象中
			vi.setVersionName(jo.getString("versionName"));
			vi.setdescribe(jo.getString("describe"));

			return vi;
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
