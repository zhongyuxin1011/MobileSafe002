package com.zyx1011.mobilesafe002.db;

import com.zyx1011.mobilesafe002.constants.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MDbHelper extends SQLiteOpenHelper {

	public MDbHelper(Context context) {
		super(context, Constants.DB.DB_NAME, null, Constants.DB.DB_VERSION);
	}

	/**
	 * 创建表结构
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Constants.DB.CREATE_TABLE_SQL);
	}

	/**
	 * 管理数据库版本
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion != newVersion) {
			db.execSQL(Constants.DB.DROP_TABLE_SQL);
		}
	}

}
