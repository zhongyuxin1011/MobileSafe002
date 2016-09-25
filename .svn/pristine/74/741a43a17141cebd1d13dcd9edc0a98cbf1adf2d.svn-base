package com.zyx1011.mobilesafe002.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * String工具类,用于构建指定的字符串
 * 
 * @author zhongyuxin
 */
public class StringUtils {

	/**
	 * 传入字节输入流,返回字符串值
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static String getString(InputStream is) throws Exception {
		BufferedReader br = null;
		try {
			StringBuilder sb = new StringBuilder(); // 通过字符串缓冲区拼接字符串
			br = new BufferedReader(new InputStreamReader(is, "utf-8")); // 指定编码方式为UTF-8

			// 读取流数据
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString(); // 返回字符串值
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (br != null) {
				try {
					br.close(); // 关闭br,同时也会关闭is
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				} finally {
					br = null; // 强制赋值为NULL,以便垃圾回收器回收
				}
			}
		}
	}

}
