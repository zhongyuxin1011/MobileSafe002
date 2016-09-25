package com.zyx1011.mobilesafe002.db;

import java.util.ArrayList;
import java.util.List;

import com.zyx1011.mobilesafe002.constants.Constants;
import com.zyx1011.mobilesafe002.entity.InterceptInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 操作数据库实现类
 * 
 * @author zhongyuxin
 */
public class MDbDao {

	private MDbHelper mHelper;

	public MDbDao(Context context) {
		mHelper = new MDbHelper(context);
	}

	/**
	 * 往数据库增加一条记录
	 * 
	 * @param phone 电话号码
	 * @param type 拦截类型代码
	 * @return 真或假
	 */
	public boolean add(String phone, int type) {
		SQLiteDatabase db = mHelper.getWritableDatabase(); // 获取数据库连接

		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("type", type);

		long insert = db.insert(Constants.DB.TABLE_NAME, null, values);
		db.close(); // 关闭数据库

		return insert > 0 ? true : false;
	}

	/**
	 * 通过phone删除数据库中的一条记录
	 * 
	 * @param phone 电话号码
	 * @return 真或假
	 */
	public boolean delete(String phone) {
		SQLiteDatabase db = mHelper.getWritableDatabase();

		int delete = db.delete(Constants.DB.TABLE_NAME, "phone=?", new String[] { phone });
		db.close();

		return delete > 0 ? true : false;
	}

	/**
	 * 通过phone修改数据库中的一条记录
	 * 
	 * @param phone 电话号码
	 * @param type 拦截类型代码
	 * @return 真或假
	 */
	public boolean modify(String phone, int type) {
		SQLiteDatabase db = mHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("type", type);

		int update = db.update(Constants.DB.TABLE_NAME, values, "phone=?", new String[] { phone });
		db.close();

		return update > 0 ? true : false;
	}

	/**
	 * 通过phone查询数据库的记录
	 * 
	 * @param phone 电话号码
	 * @return 拦截类型代码
	 */
	public int findTypeByPhone(String phone) {
		int type = -1;
		SQLiteDatabase db = mHelper.getReadableDatabase();

		Cursor query = db.query(Constants.DB.TABLE_NAME, new String[] { "type" }, "phone=?", new String[] { phone },
				null, null, null);
		if (query != null) {
			while (query.moveToNext()) {
				type = query.getInt(query.getColumnIndex("type"));
			}
			query.close();
		}
		db.close();
		return type;
	}

	/**
	 * 查询数据库中的所有记录
	 * 
	 * @return List集合
	 */
	public List<InterceptInfo> findAll() {
		List<InterceptInfo> infos = new ArrayList<InterceptInfo>();

		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor query = db.query(Constants.DB.TABLE_NAME, null, null, null, null, null, "id desc");

		if (query != null) {
			while (query.moveToNext()) {
				InterceptInfo info = new InterceptInfo();
				info.setId(query.getInt(query.getColumnIndex("id")));
				info.setPhone(query.getString(query.getColumnIndex("phone")));
				info.setType(query.getInt(query.getColumnIndex("type")));

				infos.add(info);
			}
			query.close();
		}
		db.close();

		return infos;
	}

	/**
	 * 查询数据库中是否存在某条记录
	 * 
	 * @param phone 电话号码
	 * @return 真或假
	 */
	public boolean isExists(String phone) {
		SQLiteDatabase db = mHelper.getReadableDatabase();

		Cursor query = db.query(Constants.DB.TABLE_NAME, null, "phone=?", new String[] { phone }, null, null, null);
		if (query != null) {
			while (query.moveToNext()) {
				query.close();
				db.close();
				return true;
			} 
			query.close();
		}
		db.close();
		return false;
	}

	/**
	 * 清空表中的数据
	 * 
	 * @return 真或假
	 */
	public boolean clear() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int delete = db.delete(Constants.DB.TABLE_NAME, null, null);
		db.close();

		return delete != 0;
	}

}
