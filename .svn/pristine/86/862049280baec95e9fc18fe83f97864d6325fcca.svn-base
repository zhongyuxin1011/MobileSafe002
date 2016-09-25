package com.zyx1011.mobilesafe002.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumDao {

	private String mPath;

	public CommonNumDao(Context context) {
		mPath = context.getFilesDir().getAbsolutePath() + "/commonnum.db";
	}

	public int getGroupCount() {
		int count = -1;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READONLY);

		String sql = "select count(0) from classlist";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
			cursor.close();
		}
		db.close();

		return count;
	}

	public int getChildrenCount(int groupPosition) {
		int count = -1;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READONLY);

		String sql = "select count(0) from table" + (groupPosition + 1);
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				count = cursor.getInt(0);
			}
			cursor.close();
		}
		db.close();

		return count;
	}

	public String getGroupText(int groupPosition) {
		String text = null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READONLY);

		Cursor cursor = db.query("classlist", new String[] { "name" }, "idx=?",
				new String[] { String.valueOf(groupPosition + 1) }, null, null, null);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				text = cursor.getString(0);
			}
			cursor.close();
		}
		db.close();

		return text;
	}

	public String[] getChildText(int groupPosition, int childPosition) {
		String[] texts = null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READONLY);

		Cursor cursor = db.rawQuery("select number,name from table" + (groupPosition + 1) + " where _id=?",
				new String[] { String.valueOf(childPosition + 1) });
		if (cursor != null) {
			texts = new String[2];
			if (cursor.moveToNext()) {
				texts[0] = cursor.getString(0);
				texts[1] = cursor.getString(1);
			}
			cursor.close();
		}
		db.close();

		return texts;
	}

}
