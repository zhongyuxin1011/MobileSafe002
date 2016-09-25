package com.zyx1011.mobilesafe002.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class Md5Utils {

	public static String getMd5(InputStream fis) {
		StringBuilder sb = new StringBuilder();

		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");

			byte[] b = new byte[1024 * 8];
			int len;
			while ((len = fis.read(b)) > 0) {
				digest.update(b, 0, len);
			}

			byte[] cs = digest.digest();
			for (byte c : cs) {
				String str = Integer.toHexString((int) (c & 0xff));

				if (str.length() == 1) {
					sb.append("0");
				}

				sb.append(str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				CloseableUtils.close(fis);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String getMd5(String srcStr) {
		StringBuilder sb = new StringBuilder();

		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");

			byte[] cs = digest.digest(srcStr.getBytes());
			for (byte c : cs) {
				String str = Integer.toHexString((int) (c & 0xff));

				if (str.length() == 1) {
					sb.append("0");
				}

				sb.append(str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
