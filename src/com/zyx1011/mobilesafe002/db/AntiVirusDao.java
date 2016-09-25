package com.zyx1011.mobilesafe002.db;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntiVirusDao {

	private String mPath;

	public AntiVirusDao(Context context) {
		mPath = new File(context.getFilesDir(), "antivirus.db").getAbsolutePath();
	}

	public boolean isVirus(String md5) {
		boolean flag = false;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READONLY);

		Cursor cursor = db.rawQuery("select _id from datable where md5=?", new String[] { md5 });
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				flag = true;
			}
			cursor.close();
		}
		db.close();

		return flag;
	}

	public boolean add(String md5) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);

		ContentValues values = new ContentValues();
		values.put("md5", md5);
		values.put("type", 6);
		values.put("name", "Android.Adware.AirAD.a");
		values.put("desc", "恶意后台扣费,病毒木马程序");
		long insert = db.insert("datable", null, values);

		db.close();

		return insert > -1;
	}

}
