package com.zyx1011.mobilesafe002.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭资源操作工具类
 * 
 * @author zhongyuxin
 */
public class CloseableUtils {

	/**
	 * 接收Closeable及其子类对象(实现了Closeable的子类)
	 * 
	 * @param stream
	 * @throws IOException
	 */
	public static void close(Closeable stream) throws IOException {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw e; // 通知上层调用者可能抛出异常
			} finally {
				stream = null;
			}
		}
	}
}
