package com.zyx1011.mobilesafe002.db;

import com.zyx1011.mobilesafe002.constants.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppDbHelper extends SQLiteOpenHelper {

	public AppDbHelper(Context context) {
		super(context, Constants.DB.APP_DB_NAME, null, Constants.DB.APP_DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Constants.DB.APP_CREATE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion != oldVersion) {
			db.execSQL(Constants.DB.APP_DROP_TABLE_SQL);
			db.execSQL(Constants.DB.APP_CREATE_TABLE_SQL);
		}
	}

}
