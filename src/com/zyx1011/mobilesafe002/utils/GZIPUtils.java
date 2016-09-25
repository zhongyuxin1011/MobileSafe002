package com.zyx1011.mobilesafe002.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP解压缩操作工具类
 * 
 * @author zhongyuxin
 */
public class GZIPUtils {

	/**
	 * GZIP压缩
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void gzip(InputStream in, OutputStream out) throws IOException {
		GZIPOutputStream gout = null;
		try {
			gout = new GZIPOutputStream(out);

			int len;
			byte[] b = new byte[1024 * 8];
			while ((len = in.read(b)) != -1) {
				gout.write(b, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			CloseableUtils.close(gout);
		}
	}

	/**
	 * GZIP压缩
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void gzip(File srcFile, File destFile) throws IOException {
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try {
			fin = new FileInputStream(srcFile);
			fout = new FileOutputStream(destFile);

			gzip(fin, fout);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			CloseableUtils.close(fout);
			CloseableUtils.close(fin);
		}
	}

	/**
	 * gzip解压
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public static void ungzip(InputStream in, OutputStream out) throws IOException {
		GZIPInputStream gin = null;
		try {
			gin = new GZIPInputStream(in);

			int len;
			byte[] b = new byte[1024 * 8];
			while ((len = gin.read(b)) != -1) {
				out.write(b, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			CloseableUtils.close(gin);
		}
	}

	/**
	 * gzip解压
	 * 
	 * @param srcFile
	 * @param destFile
	 * @throws IOException
	 */
	public static void ungzip(File srcFile, File destFile) throws IOException {
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try {
			fin = new FileInputStream(srcFile);
			fout = new FileOutputStream(destFile);

			ungzip(fin, fout);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			CloseableUtils.close(fout);
			CloseableUtils.close(fin);
		}
	}

}
