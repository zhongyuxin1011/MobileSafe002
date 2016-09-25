package com.zyx1011.mobilesafe002.engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zyx1011.mobilesafe002.entity.SmsInfo;
import com.zyx1011.mobilesafe002.utils.CloseableUtils;
import com.zyx1011.mobilesafe002.utils.SmsWriteOpUtil;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;

public class SmsProvider {

	public static final Uri SMS_URI = Uri.parse("content://sms");
	private static Gson mGson;
	private static int mProgress;
	private static String mPath;
	private static String mError;
	private static ContentResolver mResolver;

	public static void smsBackup(final Context context, final SmsListener listener) {

		new AsyncTask<Void, Integer, Boolean>() {

			private BufferedWriter mBw;

			protected void onPreExecute() {
				mGson = new Gson();
			};

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						mError = "存储卡不可用!";
						return false;
					} else {
						String path = Environment.getExternalStorageDirectory().getPath();

						File file = new File(path, "Backup");
						if (!file.exists()) {
							file.mkdir();
						}
						mPath = file.getAbsolutePath() + "/heima.bak";
					}

					mResolver = context.getContentResolver();
					Cursor cursor = mResolver.query(SMS_URI, new String[] { "_id", "type", "date", "address", "body" },
							null, null, "date desc");
					if (cursor != null) {
						int count = cursor.getCount();
						if (listener != null) {
							listener.onMax(count); // 通知总数
						}

						List<SmsInfo> infos = new ArrayList<>();
						while (cursor.moveToNext()) {
							SmsInfo info = new SmsInfo();

							info.id = cursor.getInt(0);
							info.type = cursor.getInt(1);
							info.date = cursor.getLong(2);
							info.address = cursor.getString(3);
							info.body = cursor.getString(4);

							infos.add(info);

							mProgress++;
							publishProgress(mProgress);
						}
						cursor.close();

						String json = mGson.toJson(infos);
						mBw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mPath)));
						mBw.write(json);
					}
				} catch (Exception e) {
					e.printStackTrace();
					mError = "文件未找到!";
					return false;
				} finally {
					try {
						CloseableUtils.close(mBw);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			}

			protected void onProgressUpdate(Integer[] values) {
				if (listener != null) {
					listener.onProgress(values[0]); // 通知进度
				}
			};

			protected void onPostExecute(Boolean result) {
				if (listener != null) {
					if (result) {
						listener.onSuccess(); // 通知成功
					} else {
						listener.onFail(mError); // 通知失败和原因
					}
				}
			};

		}.execute();
	}

	public static void smsRestore(final Context context, final SmsListener listener) {

		new AsyncTask<Void, Integer, Boolean>() {

			private BufferedReader mBr;

			protected void onPreExecute() {
				mGson = new Gson();
			};

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						mError = "存储卡不可用!";
						return false;
					} else {
						mPath = Environment.getExternalStorageDirectory().getPath() + "/Backup/heima.bak";
					}

					StringBuilder sb = new StringBuilder();
					mBr = new BufferedReader(new InputStreamReader(new FileInputStream(mPath)));
					String line;
					while ((line = mBr.readLine()) != null) {
						sb.append(line);
					}

					String json = sb.toString();
					List<SmsInfo> infos = mGson.fromJson(json, new TypeToken<List<SmsInfo>>() {
					}.getType());

					// Android 4.4以上版本短信还原的逻辑
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
						if (!SmsWriteOpUtil.isWriteEnabled(context)) {
							SmsWriteOpUtil.setWriteEnabled(context, true);
						}
					}

					if (infos != null && infos.size() > 0) {
						if (listener != null) {
							listener.onMax(infos.size());
						}

						mResolver = context.getContentResolver();

						for (SmsInfo info : infos) {
							if (!findInfo(info.date)) { // 不存在才还原,存在的跳过(时间相同)
								ContentValues values = new ContentValues();
								values.put("_id", info.id);
								values.put("date", info.date);
								values.put("type", info.type);
								values.put("address", info.address);
								values.put("body", info.body);
								mResolver.insert(SMS_URI, values);

								mProgress++;
								publishProgress(mProgress);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					mError = "文件未找到!";
					return false;
				} finally {
					try {
						CloseableUtils.close(mBr);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			}

			protected void onProgressUpdate(Integer[] values) {
				if (listener != null) {
					listener.onProgress(values[0]);
				}
			};

			protected void onPostExecute(Boolean result) {
				if (result) {
					if (listener != null) {
						listener.onSuccess();
					}
				} else {
					if (listener != null) {
						listener.onFail(mError);
					}
				}
			};

		}.execute();
	}

	/**
	 * 查询是否存在相同时间的短信
	 * 
	 * @param date
	 * @return
	 */
	protected static boolean findInfo(long date) {
		Cursor cursor = mResolver.query(SMS_URI, null, "date=?", new String[] { String.valueOf(date) }, null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				return true; // 存在
			}
			cursor.close();
		}
		return false; // 不存在
	}

	/**
	 * 定义接口,与UI交互
	 * 
	 * @author zhongyuxin
	 */
	public interface SmsListener {
		void onMax(int max); // 最大值

		void onProgress(int progress); // 进度

		void onSuccess(); // 成功

		void onFail(String errorDes); // 失败
	}

}
