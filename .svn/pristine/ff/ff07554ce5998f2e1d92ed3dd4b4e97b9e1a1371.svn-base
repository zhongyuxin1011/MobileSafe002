package com.zyx1011.mobilesafe002.db;

import java.util.ArrayList;
import java.util.List;

import com.zyx1011.mobilesafe002.constants.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppLockDao {

	private AppDbHelper mHelper;
	private Context mContex;

	public AppLockDao(Context context) {
		mContex = context;
		mHelper = new AppDbHelper(context);
	}

	/**
	 * 新增
	 * 
	 * @param packageName
	 * @return
	 */
	public boolean add(String packageName) {
		SQLiteDatabase db = mHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("package_name", packageName);
		long insert = db.insert(Constants.DB.APP_TABLE_NAME, null, values);

		db.close();

		mContex.getContentResolver().notifyChange(Constants.APPLOCK_URI, null);

		return insert != -1;
	}

	/**
	 * 删除
	 * 
	 * @param packageName
	 * @return
	 */
	public boolean delete(String packageName) {
		SQLiteDatabase db = mHelper.getWritableDatabase();

		int delete = db.delete(Constants.DB.APP_TABLE_NAME, "package_name=?", new String[] { packageName });

		db.close();

		// 通知消息中心数据发生改变
		mContex.getContentResolver().notifyChange(Constants.APPLOCK_URI, null);

		return delete > 0;
	}

	/**
	 * 判断是否上锁
	 * 
	 * @param packageName
	 * @return
	 */
	public boolean isLock(String packageName) {
		boolean flag = false;
		SQLiteDatabase db = mHelper.getReadableDatabase();

		Cursor cursor = db.query(Constants.DB.APP_TABLE_NAME, null, "package_name=?", new String[] { packageName },
				null, null, null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				flag = true;
			}
			cursor.close();
		}

		db.close();

		return flag;
	}

	/**
	 * 查询所有上锁包名
	 * 
	 * @return
	 */
	public List<String> getAll() {
		List<String> info = new ArrayList<>();
		SQLiteDatabase db = mHelper.getReadableDatabase();

		Cursor cursor = db.query(Constants.DB.APP_TABLE_NAME, new String[] { "package_name" }, null, null, null, null,
				"id desc");
		if (cursor != null) {
			while (cursor.moveToNext()) {
				info.add(cursor.getString(0));
			}
			cursor.close();
		}
		db.close();

		return info;
	}

}
